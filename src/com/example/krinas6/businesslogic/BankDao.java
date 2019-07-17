package com.example.krinas6.businesslogic;

import java.util.ArrayList;

public interface BankDao {

    ArrayList<String> getAllCustomers();

    boolean createCustomer(String name, String surName, String pNo);

    ArrayList<String> getCustomer(String pNo);

    int createSavingsAccount(String pNo);

    int createCreditAccount(String pNo);

    boolean changeCustomerName(String name, String surName, String pNo);

    ArrayList<String> deleteCustomer(String pNo);

    String getAccount(String pNo, int accountNr);

    boolean deposit(String pNo, int accountId, double amount);

    boolean withdraw(String pNo, int accountId, double amount);

    String closeAccount(String pNo, int accountId);

    ArrayList<String> getTransactions(String pNo, int accountId);

    void saveClients();

    void loadClients();

    void clientSummaryToFile(ArrayList<String> customerToSave);


}
