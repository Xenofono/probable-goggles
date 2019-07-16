// BankLogic är ingångspunkten för att skapa kunder, konton och transaktioner, skapas som en singleton
// @author Kristoffer Näsström, krinas-6

package com.example.krinas6.businesslogic;

import com.example.krinas6.businesslogic.account.Account;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BankLogic {

    // Vi kommer bara ha en av denna klass så vi skapar den som en singleton för att se till att bara
    // en skapas
    private static BankLogic instance = new BankLogic();

    // private constructor gör så att enda sättet att komma åt klassen är via metoden getInstance()
    private BankLogic() {
        loadClients();
    }

    // ArrayList customers skapas som håller bankens kunder.
    // Comparator comp skapas så att Collections vet att den ska sortera efter personnummer, det
    // behövs för att kunna binärsöka
    private ArrayList<Customer> customers = new ArrayList<>();

    // Skapar och returnerar en ArrayList med alla kunder genom att använda kundens egna toString
    public ArrayList<String> getAllCustomers() {
        return customers.stream().map(customer -> customer.toString()+"\n").collect(Collectors.toCollection(ArrayList::new));
    }

    //If optionalCustomer is empty then there is no such customer
    public boolean createCustomer(String name, String surName, String pNo) {

        Optional<Customer> optionalCustomer = customers.parallelStream().filter(customer -> customer.getpNo().equals(pNo))
                .findAny();

        if(optionalCustomer.isPresent()){
            return false;
        }
        customers.add(new Customer(name, surName, pNo));
        return true;
    }

    // Försöker hitta pNo genom binärsökning, om index är mindre än 0 så hittades inte kunden.
    // Returnerar en lista med kundens uppgifter och konton
    public ArrayList<String> getCustomer(String pNo) {

        Optional<Customer> optionalCustomer = customers.stream()
                .filter(customer -> customer.getpNo().equals(pNo))
                .findFirst();

        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            ArrayList<String> customerInfo = new ArrayList<>();
            customerInfo.add(customer.toString());
            customer.getAccounts().forEach(account -> customerInfo.add(account.toString()));
            return customerInfo;
        }
        else{
            return null;
        }

    }

    // Försöker hitta pNo genom binärsökning.
    // Returnerar nya sparkontots kontonr eller -1 om något gick fel.
    public int createSavingsAccount(String pNo) {
        return getOptionalCustomer(pNo).map(Customer::createAccount).orElse(-1);
    }

    // Försöker hitta pNo genom binärsökning.
    // Returnerar nya kreditkontots kontonr eller -1 om något gick fel.
    public int createCreditAccount(String pNo) {
        return getOptionalCustomer(pNo).map(Customer::createCreditAccount).orElse(-1);
    }

    // Försöker hitta pNo genom binärsökning.
    // Returnerar true om kundens namn ändrades annars false.
    public boolean changeCustomerName(String name, String surName, String pNo) {

        return getOptionalCustomer(pNo).map(customer -> {
            customer.setName(name);
            customer.setSurName(surName);
            return true;
        }).orElse(false);

    }

    // Försöker hitta pNo genom binärsökning.
    // Om kunden hittas så returneras en kopia på kunden och dennes konton, sedan raderas kunden.
    public ArrayList<String> deleteCustomer(String pNo) {

        Optional<Customer> optionalCustomer = getOptionalCustomer(pNo);

        if(optionalCustomer.isPresent()){
            List<Account> accountsToBeDeleted = optionalCustomer.get().getAccounts();
            String customerDetails = optionalCustomer.get().toString();
            ArrayList<String> customerInfo = new ArrayList<>();
            customerInfo.add(customerDetails);
            for (Account account : accountsToBeDeleted) {
                customerInfo.add(
                        account.toString() + " " + account.calculateInterest());
            }
            customers.remove(optionalCustomer.get());
            return customerInfo;
        }
        else{
            return null;
        }

    }

    // Om en kund hittas via binärsökning så anropas kundens getAccountmetod som försöker hitta
    // kontot, annars null.
    public String getAccount(String pNo, int accountNr) {
        return getOptionalCustomer(pNo).map(customer -> customer.getAccount(accountNr)).orElse(null);
    }

    // Om kunden hittas så anropas dennes depositmetod som försöker hitta kontot, om kontot hittas så
    // läggs pengarna in.
    public boolean deposit(String pNo, int accountId, double amount) {
        return getOptionalCustomer(pNo).map(customer -> customer.deposit(accountId, amount)).orElse(false);
    }

    // Om kunden hittas så anropas dennes withdrawmetod som försöker hitta kontot, om kontot hittas så
    // returneras kontots withdrawmetod som kontrollerar så saldot räcker för uttag.
    public boolean withdraw(String pNo, int accountId, double amount) {
        return getOptionalCustomer(pNo).map(customer -> customer.withdraw(accountId, amount)).orElse(false);
    }

    // Om kunden hittas så anropas kundens closeAccountmetod som returnerar en kopia på kundens
    // uppgifter och
    // de konton som stängdes med räntan beräknad. Annars returneras null.
    public String closeAccount(String pNo, int accountId) {
        return getOptionalCustomer(pNo).map(customer -> customer.closeAccount(accountId)).orElse(null);
    }

    // Binärsöker efter pNo, returnerar null om den inte finns, annars returneras resultatet av
    // kundens getTransactions()
    public ArrayList<String> getTransactions(String pNo, int accountId) {
        return getOptionalCustomer(pNo).map(customer -> customer.getTransactions(accountId)).orElse(null);
    }

    // Privat metod som bara anropas inom klassen, Collections.binarySearch letar igenom listan
    // customers
    // efter ett Customerobjekt som har samma pNo som metoden anropats med. Använder Comparatorn comp
    // som referens.
    private int searchIndex(String pNo) {
        return Collections.binarySearch(customers, new Customer(null, null, pNo), Comparator.comparing(Customer::getpNo));
    }

    private Optional<Customer> getOptionalCustomer(String pNo){
        return customers.stream().filter(customer -> customer.getpNo().equals(pNo)).findFirst();
    }

    //Skriver över clientlist.dat med nuvarande customerslistan
    public void saveClients() {
        try(ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("krinas6_Files/clientlist.dat")))){
            for(Customer customer : customers){
                output.writeObject(customer);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //Laddar alla objekt från filen clientlist.dat
    public void loadClients() {

        File directory = new File("krinas6_Files");
        if(!directory.exists())
            directory.mkdir();

        try(ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("krinas6_Files/clientlist.dat")))){
            boolean eof = false;
            while(!eof){
                try{
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
                }  catch(EOFException e){
                    eof = true;
                }
            }
            //Innan användaren har sparat finns ingen fil att ladda in när programmet startar så då visas det här meddelandet.
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Kunde inte ladda kunder, förväntat vid första körning.");
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Sparar all kundens data till en läslig textfil
    public void clientSummaryToFile(ArrayList<String> customerToSave){
        //namnet på filen blir kundens personnummer + kundöverblick
        String[] customerInfo = customerToSave.get(0).split(" ");
        String fileName = customerInfo[2] + " kundöverblick";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("krinas6_Files/" + fileName + ".txt"))){
            writer.write("KUND\n=========================\n");
            writer.write(customerInfo[0] + "\n" + customerInfo[1] + "\n" + customerInfo[2]);
            writer.write("\n\n");

            //Kundens uppgifter är inskrivna, nu loopas alla konton igenom, inre loopen skriver in transaktionerna
            for(int i = 1; i < customerToSave.size(); i++){
                String[] account = customerToSave.get(i).split(" ");
                writer.write("\n\nKONTOINFORMATION");
                writer.write("\n=========================\n\n");
                writer.write("Kontonummer: " + account[0]);
                writer.write("\nKontosaldo: " + account[1]);
                writer.write("\nKontotyp: " + account[2]);
                writer.write("\nRänta: " + account[3]);

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
        } catch(IOException e){
            JOptionPane.showMessageDialog(null, "Något gick fel");
        }


        JOptionPane.showMessageDialog(null, "Sparat till 'fil " + fileName + ".txt'");
    }

    // Getter för vår singletonklass
    public static BankLogic getInstance() {
        return instance;
    }
}
