//Abstrakt klass som måste ärvas, basklassen för SavingsAccount och CreditAccount
//@author Kristoffer Näsström, krinas-6


//Flyttat alla accountklasser och Transaction in i eget paket för att skärma av basklassen bättre.
//Klassen Account får variabler utan modifier så de endast är åtkomliga inom samma paket och av subklasser inom samma paket.
package com.example.krinas6.businesslogic.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;


abstract public class Account implements Serializable {

    //currentId tillämpas bara i denna klass via SavingsAccount och CreditAccounts användande av super() så den är private.
    //Alla andra variabler är package-private
    private static int currentId = 1001;
    private final int accountId;
    private double balance;
    private final String accountType;
    private double interest;
    private ArrayList<Transaction> transactions;

    //Constructor som de olika subklasserna använder sig av
    Account (double balance, String accountType, double interest){
        this.accountId = currentId;
        this.balance = balance;
        this.accountType = accountType;
        this.interest = interest;
        this.transactions = new ArrayList<>();
        currentId++;
    }


    //Om listan transactions är tom så returneras null, annars skapas en Stringlista som kopierar alla transaktions.
    public ArrayList<String> getTransactions(){
        if(transactions.isEmpty())
            return null;
        return transactions.stream().map(Transaction::toString).collect(Collectors.toCollection(ArrayList::new));
    }

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        Account.currentId = currentId;
    }

    //Beräknar räntan genom saldo * ränta / 100
    public double calculateInterest(){
        return this.getBalance() * this.getInterest() / 100;
    }

    //getter för kontonummer
    public int getAccountId() {
        return accountId;
    }

    //getters och setters med default modifier så de bara kan användas av subklasser inom paketet.


    double getBalance() {
        return balance;
    }

    void setBalance(double amount) {
        this.balance += amount;
    }

    String getAccountType() {
        return accountType;
    }

    double getInterest() {
        return interest;
    }

    void setInterest(double interest) {
        this.interest = interest;
    }

    void setTransactions(Transaction transaction) {
        this.transactions.add(transaction);
    }

  // Alla klasser som ärver account ska implementera dessa metoder, då vet accountslistan i Customer
  // att de alltid finns.
  // Subklassen definerar metodkroppen eftersom varje konto implementerar dem olika, den abstrakta
  // klassen bryr sig inte om det.

  public abstract boolean withdraw(double amount);

    public abstract void deposit(double amount);

    @Override
    abstract public String toString();
}
