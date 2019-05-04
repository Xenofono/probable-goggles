/*
Ingångspunkten för programmet, skapar en static instans av BankLogic och skapar sedan den JFrame som håller resten
@author Kristoffer Näsström, krinas-6
 */
package com.example.krinas6.gui;

import com.example.krinas6.businesslogic.BankLogic;

import javax.swing.*;

public class Main {

  // bank tilldelas den statiska instansen av BankLogic så vi kan komma åt den smidigt inuti
  // GUI-klasserna
  static BankLogic bank = BankLogic.getInstance();


  public static void main(String[] args) {
    // Frame skapas i en egen tråd
    SwingUtilities.invokeLater(
        () -> {
          JFrame frame = new MainGui("Kristoffers Banksystem");
          frame.setSize(900, 800);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
        });
  }



}
