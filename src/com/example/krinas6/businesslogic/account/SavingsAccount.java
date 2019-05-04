// Ärver Account, konto som bara tillåter uttag så länge saldot inte går under 0
// @author Kristoffer Näsström, krinas-6

// Flyttat alla accountklasser och Transaction in i eget paket för att skärma av basklassen bättre.
package com.example.krinas6.businesslogic.account;

import java.io.Serializable;

public class SavingsAccount extends Account implements Serializable {

  // Enda egna variabeln i denna klass, är true så länge inget uttag har gjorts.
  private boolean freeWithdrawl = true;

  // Constructor för kontot kallar på superklassens constructor.
  public SavingsAccount(double balance, String accountType, double interest) {
    super(balance, accountType, interest);
  }

  // Voidmetod som lägger till amount till saldot och skapar ny transaktion
  public void deposit(double amount) {
    this.setBalance(amount);
    this.setTransactions(new Transaction(amount, this.getBalance()));
  }

  // Ta ut pengarmetod, först anropas setFee för att sätta avgiften och sedan korrigeras amount.
  // Sedan kontrolleras om saldot räcker, om inte så return false, annars korrigera balance,
  // spara det negativa värdet av amount tillsammans med nuvarande saldot och freeWithdrawl ställs
  // till false.
  public boolean withdraw(double amount) {
    double fee = setFee();
    amount *= fee;

    if ((this.getBalance() - amount) >= 0) {
      this.setBalance(-(amount));
      this.setTransactions(new Transaction(-(amount), this.getBalance()));

      if (freeWithdrawl) freeWithdrawl = false;
      return true;
    }
    return false;
  }

  // Privat metod som kollar om årets gratis uttag har gjorts och bestämmer sedan avgiften från det.
  private double setFee() {
    double fee;

    if (freeWithdrawl) fee = 1;
    else fee = 1.02;
    return fee;
  }

  // Override på toString så programmet kan skriva ut all info utan att använda getters eller
  // variablerna direkt.
  @Override
  public String toString() {
    return this.getAccountId()
        + " "
        + this.getBalance()
        + " "
        + this.getAccountType()
        + " "
        + this.getInterest();
  }
}
