// Kundklassen som instansieras inne i klassen BankDaoImpl, den agerar mellanhand mellan BankDaoImpl och
// Accounts
// @author Kristoffer Näsström, krinas-6

package com.example.krinas6.businesslogic;

// Importerar de olika Accountklasserna som har skärmats av
import com.example.krinas6.businesslogic.account.Account;
import com.example.krinas6.businesslogic.account.CreditAccount;
import com.example.krinas6.businesslogic.account.SavingsAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

class Customer implements Serializable {

  // Variabler deklareras och en ArrayList skapas som ska hålla kundens konton.
  // Bara privata variabler förutom listan som är package-private.
  // Lista av abstrakta klassen Account som är gjord så alla anrop här ska funka på alla slags
  // konton.
  private String name, surName, pNo;
  private ArrayList<Account> accounts;

  //Ser till att objekt vi laddar från fil är av samma version som objektet i koden.
  private static final long serialVersionUID = 1L;

  // Constructor
  Customer(String name, String surName, String pNo) {
    this.name = name;
    this.surName = surName;
    this.pNo = pNo;
    accounts = new ArrayList<>();
  }

  // Setters för kundens namn, används av metoden changeCustomerName i klassen BankDaoImpl.
  void setName(String name) {
    this.name = name;
  }

  void setSurName(String surName) {
    this.surName = surName;
  }

  // Getter för personnummer
  String getpNo() {
    return pNo;
  }

  List<Account> getAccounts(){
    return List.copyOf(accounts);
  }

  String getAccount(int accountNr) {
    return getOptionalAccount(accountNr)
            .map(Account::toString)
            .orElse(null);
  }

  boolean deposit(int accountNr, double amount) {
    return getOptionalAccount(accountNr)
            .map(account -> {
              account.deposit(amount);
              return true;
            }).orElse(false);
  }

  boolean withdraw(int accountNr, double amount) {
    return getOptionalAccount(accountNr)
            .map(account -> account.withdraw(amount)).orElse(false);
  }

  // Skapar ett nytt konto som läggs i kundens ArrayList accounts, returnerar kontonumret, annars
  // -1.
  int createAccount() {
    if (accounts.add(new SavingsAccount(0, "Sparkonto", 1)))
      return accounts.get(accounts.size() - 1).getAccountId();
    else return -1;
  }

  // Skapar ett nytt kreditkonto som läggs i kundens ArrayList accounts, returnerar kontonumret,
  // annars -1.
  int createCreditAccount() {
    if (accounts.add(new CreditAccount(0, "Kreditkonto", 0.5)))
      return accounts.get(accounts.size() - 1).getAccountId();
    else return -1;
  }

  // Tar ett kontonr och om kontot tillhör kuden så görs en tillfällig kopia av kontot som
  // returneras, sedan raderas kontot.
  String closeAccount(int accountNr) {

    return getOptionalAccount(accountNr).map(account -> {
      String copyOfAccount = account.toString() + " " + account.calculateInterest();
      accounts.remove(account);
      return copyOfAccount;
    }).orElse(null);

  }

  ArrayList<String> getTransactions(int accountNr) {
    return getOptionalAccount(accountNr).map(Account::getTransactions).orElse(null);
  }

  ArrayList<String> createCustomerDetailsList(){
    var listToBeReturned = new ArrayList<String>();
    listToBeReturned.add(this.toString());
    this.accounts.forEach(account -> listToBeReturned.add(account.toString()));
    return listToBeReturned;
  }


  private Optional<Account> getOptionalAccount(int accountNr) {
    return this.accounts.stream().filter(account -> account.getAccountId() == accountNr).findAny();
  }

  // Gör så man lättare kan skriva ut kundens uppgifter.
  public String toString() {
    return this.name + " " + this.surName + " " + this.pNo;
  }
}
