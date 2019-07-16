// Kundklassen som instansieras inne i klassen BankLogic, den agerar mellanhand mellan BankLogic och
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

  // Setters för kundens namn, används av metoden changeCustomerName i klassen BankLogic.
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

  // Tar ett kontonr och letar igenom kundens konton om kontot finns, returnerar om det finns.
  String getAccount(int accountNr) {
    for (Account account : accounts) {
      if (account.getAccountId() == accountNr) {
        return account.toString();
      }
    }
    return null;
  }

  // Tar kontonr och summa, om kontot finns hos kunden så läggs summan in på kontot och metoden
  // returnerar true.
  boolean deposit(int accountNr, double amount) {
    for (Account account : accounts) {
      if (account.getAccountId() == accountNr) {
        account.deposit(amount);
        return true;
      }
    }
    return false;
  }

  // Tar kontonr och summa, om kontot finns hos kunden så anropas kontots withdrawmetod med summan,
  // om den lyckas
  // ta ut pengarna så returnerar den true, finns inte tillräckligt med saldo returnerar den false.
  boolean withdraw(int accountNr, double amount) {
    for (Account account : accounts) {
      if (account.getAccountId() == accountNr) {
        return account.withdraw(amount);
      }
    }
    return false;
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
    int index = -1;
    String temp = "";

    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).getAccountId() == accountNr) {
        temp = accounts.get(i).toString();
        temp += " " + accounts.get(i).calculateInterest();
        index = i;
      }
    }
    if (index != -1) {
      accounts.remove(index);
      return temp;
    }
    return null;
  }

  // Letar igenom alla konton efter en som stämmer med accountNr, om den finns så anropas kontots
  // getTransactions().
  ArrayList<String> getTransactions(int accountNr) {
    for (Account account : accounts) {
      if (accountNr == account.getAccountId()) {
        return account.getTransactions();
      }
    }
    return null;
  }

  // Gör så man lättare kan skriva ut kundens uppgifter.
  public String toString() {
    return this.name + " " + this.surName + " " + this.pNo;
  }
}
