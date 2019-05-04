// Klass för att skapa objekt av varje enskild transaktion
// @author Kristoffer Näsström, krinas-6

// Flyttat alla accountklasser och Transaction in i eget paket för att skärma av basklassen bättre.
package com.example.krinas6.businesslogic.account;

// Importer för att kunna formatera datum och tid rätt
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Transaction implements Serializable {

  // Bara privata variabler som sätts av constructorn.
  private double amount;
  private String date;
  private double currentBalance;

  // Constructorn, mest anmärkningsvärt är väl att ett nytt Dateobjekt skapas och sedan formatteras
  // enligt ovan.
  Transaction(double amount, double currentBalance) {
    // Datumformatterare som gör att vi får år/månad/dag och 24-timmarsklocka
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    this.date = formatter.format(new Date());
    this.amount = amount;
    this.currentBalance = currentBalance;
  }

  // Formatterar transaktionen till en String.
  @Override
  public String toString() {
    return this.date + " " + this.amount + " " + this.currentBalance;
  }
}
