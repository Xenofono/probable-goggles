

package com.example.krinas6.businesslogic;

import com.example.krinas6.businesslogic.account.Account;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kristoffer Näsström
 * Class is entrypoint to all business logic in the program, it's responsible for finding and validating that the customers exist before performing operations
 */
public class BankLogic {

    // Vi kommer bara ha en av denna klass så vi skapar den som en singleton för att se till att bara
    // en skapas
    private static BankLogic instance = new BankLogic();

    // private constructor gör så att enda sättet att komma åt klassen är via metoden getInstance()
    private BankLogic() {
        loadClients();
    }

    private ArrayList<Customer> customers = new ArrayList<>();

    public ArrayList<String> getAllCustomers() {
        return customers.stream().map(customer -> customer.toString() + "\n").collect(Collectors.toCollection(ArrayList::new));
    }

    //If optionalCustomer is present then map returns false, otherwise a new customer is created
    public boolean createCustomer(String name, String surName, String pNo) {
        return getOptionalCustomer(pNo).map(customer -> false)
                .orElseGet(() -> customers.add(new Customer(name, surName, pNo)));
    }

    public ArrayList<String> getCustomer(String pNo) {
        return getOptionalCustomer(pNo)
                .map((customer) -> {
                    ArrayList<String> customerInfo = new ArrayList<>();
                    customerInfo.add(customer.toString());
                    customer.getAccounts().forEach(account -> customerInfo.add(account.toString()));
                    return customerInfo;
                }).orElse(null);

    }

    public int createSavingsAccount(String pNo) {
        return getOptionalCustomer(pNo).map(Customer::createAccount).orElse(-1);
    }

    public int createCreditAccount(String pNo) {
        return getOptionalCustomer(pNo).map(Customer::createCreditAccount).orElse(-1);
    }

    public boolean changeCustomerName(String name, String surName, String pNo) {
        return getOptionalCustomer(pNo).map(customer -> {
            customer.setName(name);
            customer.setSurName(surName);
            return true;
        }).orElse(false);

    }

    /**
     *
     * @param pNo personal identification number
     * @return ArrayList with the customers closing statement, incl final interests. returns null if no customer is found
     */
    public ArrayList<String> deleteCustomer(String pNo) {

        return getOptionalCustomer(pNo).map(customer -> {
            ArrayList<String> customerInfo = new ArrayList<>();
            customerInfo.add(customer.toString());
            for (Account account : customer.getAccounts()) {
                customerInfo.add(
                        account.toString() + " " + account.calculateInterest());
            }
            customers.remove(customer);
            return customerInfo;
        }).orElse(null);

    }

    public String getAccount(String pNo, int accountNr) {
        return getOptionalCustomer(pNo).map(customer -> customer.getAccount(accountNr)).orElse(null);
    }

    public boolean deposit(String pNo, int accountId, double amount) {
        return getOptionalCustomer(pNo).map(customer -> customer.deposit(accountId, amount)).orElse(false);
    }

    public boolean withdraw(String pNo, int accountId, double amount) {
        return getOptionalCustomer(pNo).map(customer -> customer.withdraw(accountId, amount)).orElse(false);
    }

    public String closeAccount(String pNo, int accountId) {
        return getOptionalCustomer(pNo).map(customer -> customer.closeAccount(accountId)).orElse(null);
    }

    public ArrayList<String> getTransactions(String pNo, int accountId) {
        return getOptionalCustomer(pNo).map(customer -> customer.getTransactions(accountId)).orElse(null);
    }

    private Optional<Customer> getOptionalCustomer(String pNo) {
        return customers.parallelStream().filter(customer -> customer.getpNo().equals(pNo)).findAny();
    }

    //Skriver över clientlist.dat med nuvarande customerslistan
    public void saveClients() {
        try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("krinas6_Files/clientlist.dat")))) {
            for (Customer customer : customers) {
                output.writeObject(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Laddar alla objekt från filen clientlist.dat
    public void loadClients() {

        File directory = new File("krinas6_Files");
        if (!directory.exists())
            directory.mkdir();

        try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("krinas6_Files/clientlist.dat")))) {
            boolean eof = false;
            while (!eof) {
                try {
                    Customer customer = (Customer) input.readObject();
                    customers.add(customer);

                    //Jämför alla kunders kontonr mot nästa nummer som kommer tilldelas så att den inte återställs till 1001.
                    //T.ex om loopen hittar att ett konto med större eller liknande nummer som nästa nummer som ska tilldelas så
                    //korrigerar vi klassvariabeln currentId till nästa lämpliga nummer.
                    List<Account> currentLoadingCustomersAccounts = customer.getAccounts();
                    for (Account currentLoadingCustomersAccount : currentLoadingCustomersAccounts) {
                        if (currentLoadingCustomersAccount.getAccountId() >= Account.getCurrentId()) {
                            Account.setCurrentId(currentLoadingCustomersAccount.getAccountId() + 1);
                        }
                    }
                } catch (EOFException e) {
                    eof = true;
                }
            }
            //Innan användaren har sparat finns ingen fil att ladda in när programmet startar så då visas det här meddelandet.
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Kunde inte ladda kunder, förväntat vid första körning.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Sparar all kundens data till en läslig textfil
    public void clientSummaryToFile(ArrayList<String> customerToSave) {
        //namnet på filen blir kundens personnummer + kundöverblick
        String[] customerInfo = customerToSave.get(0).split(" ");
        String fileName = customerInfo[2] + " kundöverblick";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("krinas6_Files/" + fileName + ".txt"))) {
            writer.write("KUND\n=========================\n");
            writer.write(customerInfo[0] + "\n" + customerInfo[1] + "\n" + customerInfo[2]);
            writer.write("\n\n");

            //Kundens uppgifter är inskrivna, nu loopas alla konton igenom, inre loopen skriver in transaktionerna
            for (int i = 1; i < customerToSave.size(); i++) {
                String[] account = customerToSave.get(i).split(" ");
                writer.write("\n\nKONTOINFORMATION" +
                        "\n=========================\n\n" +
                        "Kontonummer: " + account[0] +
                        "\nKontosaldo: " + account[1] +
                        "\nKontotyp: " + account[2] +
                        "\nRänta: " + account[3]);

                //Vi hämtar alla transaktioner från nuvarande kontot och skriver in dem i filen.
                ArrayList<String> transactionsList = getTransactions(customerInfo[2], Integer.parseInt(account[0]));
                writer.write("\n\nTRANSAKTIONER FÖR KONTO " + account[0] + "\n=========================\n\n");
                for (String s : transactionsList) {
                    String[] transaction = s.split(" ");
                    writer.write("\n\tDatum: " + transaction[0]);
                    writer.write("\n\tTid: " + transaction[1]);
                    writer.write("\n\tSumma: " + transaction[2]);
                    writer.write("\n\tSaldo: " + transaction[3] + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Något gick fel");
        }


        JOptionPane.showMessageDialog(null, "Sparat till 'fil " + fileName + ".txt'");
    }

    // Getter för vår singletonklass
    public static BankLogic getInstance() {
        return instance;
    }
}
