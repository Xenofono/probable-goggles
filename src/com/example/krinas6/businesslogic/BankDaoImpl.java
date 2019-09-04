

package com.example.krinas6.businesslogic;

import com.example.krinas6.businesslogic.account.Account;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kristoffer Näsström
 * Class is entrypoint to all business logic in the program, it's responsible for finding and validating that the customers exist before performing operations
 */
public class BankDaoImpl implements BankDao {

    private ArrayList<Customer> customers = new ArrayList<>();


    private final SavingAndLoadingDataClass savingAndLoadingDataClass;


    //Bill Pugh singleton design pattern. Only accessible by the getInstance-method
    private static class CreateBankDaoImplInstance {
        private final static BankDaoImpl instance = new BankDaoImpl();
    }

    // private constructor gör så att enda sättet att komma åt klassen är via metoden getInstance()
    private BankDaoImpl() {
        savingAndLoadingDataClass = new SavingAndLoadingDataClass();
        loadClients();
    }


    public ArrayList<String> getAllCustomers() {
        return customers.stream().map(customer -> customer.toString() + "\n").collect(Collectors.toCollection(ArrayList::new));
    }

    //If optionalCustomer is present then map returns false, otherwise a new customer is created
    public boolean createCustomer(String name, String surName, String pNo) {
        return getOptionalCustomer(pNo).map(customer -> false)
                .orElseGet(() -> customers.add(new Customer(name, surName, pNo)));
    }

    //if the customer is found (optional present) then the details list is created, otherwise null
    public ArrayList<String> getCustomer(String pNo) {
        return getOptionalCustomer(pNo)
                .map(Customer::createCustomerDetailsList).orElse(null);

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
        savingAndLoadingDataClass.saveClients(customers);
    }

    //Laddar alla objekt från filen clientlist.dat
    public void loadClients() {
        savingAndLoadingDataClass.loadClients(customers);
    }

    //Sparar all kundens data till en läslig textfil
    public void clientSummaryToFile(ArrayList<String> customerToSave) {
        savingAndLoadingDataClass.clientSummaryToFile(customerToSave);
    }

    // Getter för vår singletonklass
    public static BankDaoImpl getInstance() {
        return CreateBankDaoImplInstance.instance;
    }
}
