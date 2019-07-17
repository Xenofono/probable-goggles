// Ärver Account, konto som kan få ett saldo på -5000 innan uttag stoppas.
// @author Kristoffer Näsström, krinas-6

// Flyttat alla accountklasser och Transaction in i eget paket för att skärma av basklassen bättre.
package com.example.krinas6.businesslogic.account;

import java.io.Serializable;

public class CreditAccount extends Account implements Serializable {

    // Enda egna variablen är privata credit som är så mycket man får ligga minus innan withdraw
    // slutar funka.
    private final double credit = -5000;

    // Constructor för kontot kallar på superklassens constructor.
    public CreditAccount(double balance, String accountType, double interest) {
        super(balance, accountType, interest);
    }

    // Voidmetod som lägger till amount till saldot och skapar ny transaktion
    // Till sist så korrigeras räntan via changeInterest()
    public void deposit(double amount) {
        this.setBalance(amount);
        this.setTransactions(new Transaction(amount, this.getBalance()));
        changeInterest();
    }

    // Om saldo minus amount är större än -5000 så dras summan av och negativa värdet av amount sparas
    // i transactions,
    // nuvarande saldot sparas också.
    // Till sist efter ett lyckat uttag så anropas changeInterest() för att se om räntan behöver
    // korrigeras.
    // Misslyckas uttaget så returneras false.
    public boolean withdraw(double amount) {
        if ((this.getBalance() - amount) >= credit) {
            this.setBalance(-(amount));
            this.setTransactions(new Transaction(-(amount), this.getBalance()));
            changeInterest();
            return true;
        } else {
            return false;
        }
    }

    // Privat metod som kollar om räntan behöver korrigeras efter ett uttag.
    // om kreditkontot ligger under 0 så är räntan 7%, annars är räntan 0.5%.
    //Använder en tertiary operator.
    private void changeInterest() {
        double newInterest = this.getBalance() < 0 ? 7 : 0.5;
        this.setInterest(newInterest);

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
