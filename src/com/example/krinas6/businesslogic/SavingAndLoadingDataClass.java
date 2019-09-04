package com.example.krinas6.businesslogic;

import com.example.krinas6.businesslogic.account.Account;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class SavingAndLoadingDataClass {


    //Skriver över clientlist.dat med nuvarande customerslistan
    void saveClients(List<Customer> customers) {
        try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("krinas6_Files/clientlist.dat")))) {
            for (Customer customer : customers) {
                output.writeObject(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Laddar alla objekt från filen clientlist.dat
    void loadClients(List<Customer> customers) {

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

            saveAccountToFile(customerInfo[2], customerToSave, writer);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Något gick fel");
        }


        JOptionPane.showMessageDialog(null, "Sparat till 'fil " + fileName + ".txt'");
    }

    private void saveAccountToFile(String customerPNO, ArrayList<String> accountsToSave, BufferedWriter writer) throws IOException {
        for (int i = 1; i < accountsToSave.size(); i++) {
            String[] account = accountsToSave.get(i).split(" ");
            writer.write("\n\nKONTOINFORMATION" +
                    "\n=========================\n\n" +
                    "Kontonummer: " + account[0] +
                    "\nKontosaldo: " + account[1] +
                    "\nKontotyp: " + account[2] +
                    "\nRänta: " + account[3]);

            ArrayList<String> allTransactions = BankDaoImpl.getInstance().getTransactions(customerPNO, Integer.parseInt(account[0]));

            writer.write("\n\nTRANSAKTIONER FÖR KONTO " + account[0] + "\n=========================\n\n");
            if (allTransactions == null || allTransactions.size() == 0) {
                writer.write("\n\tInga transaktioner ännu");
            } else {
                for (String transaction : allTransactions) {
                    saveCustomerTransactionsToFile(transaction, writer);
                }
            }

        }
    }

    private void saveCustomerTransactionsToFile(String transactionPreSplit, BufferedWriter writer) throws IOException {
        String[] transaction = transactionPreSplit.split(" ");

        writer.write("\n\tDatum: " + transaction[0]);
        writer.write("\n\tTid: " + transaction[1]);
        writer.write("\n\tSumma: " + transaction[2]);
        writer.write("\n\tSaldo: " + transaction[3] + "\n");
    }
}
