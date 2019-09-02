/*
Ingångspunkten för programmet, skapar en static instans av BankDaoImpl och skapar sedan den JFrame som håller resten
@author Kristoffer Näsström, krinas-6
 */
package com.example.krinas6.gui;


import javax.swing.*;

public class Main {


    public static void main(String[] args) {
        // Frame skapas i en egen tråd
        SwingUtilities.invokeLater(
                () -> {
                    JFrame frame = new MainGui("Kristoffers Banksystem");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                });
    }


}
