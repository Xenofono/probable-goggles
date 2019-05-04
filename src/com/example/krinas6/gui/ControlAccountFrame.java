/*
Denna klass skapar en frame som är till för att mata in den data som behövs för att anropa metoderna i BankLogic som
relaterar till konton.
@author Kristoffer Näsström, krinas-6
 */

package com.example.krinas6.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Klassen ärver JFrame så vi kan skapa den som vi vill ha den lättare
class ControlAccountFrame extends JFrame {

  // Alla varianter av klassen har samma constructor till en början
  ControlAccountFrame(String title) {
    super(title);

    this.setSize(300, 250);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);
    Container c = getContentPane();
    c.setLayout(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();
    gc.anchor = GridBagConstraints.LINE_START;

    gc.weightx = 0.5;
    gc.weighty = 0.5;
    gc.insets = new Insets(5, 5, 5, 5);

    // Här börjar de skilja sig åt, vi använder titeln på JFrame som switchvillkor för att bestämma
    // vilken vi ska skapa
    switch (title) {
      case "Insättning":
        {
          JLabel pNoLabel = new JLabel("Personnummer: ");
          JLabel accountId = new JLabel("Kontonummer: ");
          JLabel deposit = new JLabel("Insättning: ");

          JTextField accountIdField = new JTextField(10);
          JTextField depositField = new JTextField(10);
          JTextField pNoField = new JTextField(10);

          JButton createButton = new JButton("Sätt in pengar");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(pNoLabel, gc);
          gc.gridy = 2;
          add(accountId, gc);
          gc.gridy = 3;
          add(deposit, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(pNoField, gc);
          gc.gridy = 2;
          add(accountIdField, gc);
          gc.gridy = 3;
          add(depositField, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(createButton, gc);

          // Knappen är bunden till den här kodsnutten som anropar den statiska instansen av banks
          // depositmetod.
          // Om kunden hittades så sätts pengarna in och bilden ändras till en överblick på kundens
          // konton, annars felmeddelande.
          createButton.addActionListener(
              e -> {
                String pNo = pNoField.getText().strip();
                // Try/catch för att fånga ifall någon skriver in något annat än nummer i
                // nummerfälten.
                try {
                  int accountIdValue = Integer.parseInt(accountIdField.getText().strip());
                  double amount = Double.parseDouble(depositField.getText().strip());
                  boolean foundCustomer = Main.bank.deposit(pNo, accountIdValue, amount);
                  if (foundCustomer) {
                    // Om kunden hittas så hämtar vi en arraylist av kunden och skickar med till
                    // tabellen så
                    // att vi kan skriva alla uppgifter längst upp
                    ArrayList<String> customer = Main.bank.getCustomer(pNo);
                    MainGui.table.setModel(MainGui.table.loadCustomer(customer));
                  } else {
                    JOptionPane.showMessageDialog(null, "Kontonummer eller personnummer felaktigt");
                  }

                  this.dispose();

                } catch (NumberFormatException error) {
                  JOptionPane.showMessageDialog(null, "Felaktiga värden!");
                }
              });

          break;
        }
      case "Uttag":
        {
          JLabel pNoLabel = new JLabel("Personnummer: ");
          JLabel accountId = new JLabel("Kontonummer: ");
          JLabel deposit = new JLabel("Uttag: ");

          JTextField accountIdField = new JTextField(10);
          JTextField depositField = new JTextField(10);
          JTextField pNoField = new JTextField(10);

          JButton createButton = new JButton("Ta ut pengar");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(pNoLabel, gc);
          gc.gridy = 2;
          add(accountId, gc);
          gc.gridy = 3;
          add(deposit, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(pNoField, gc);
          gc.gridy = 2;
          add(accountIdField, gc);
          gc.gridy = 3;
          add(depositField, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(createButton, gc);

          // Knappen är bunden till denna kodsnutt som tar värdena och försöker anropa bankens
          // withdrawmetod.
          // Om den lyckas så skickas vi till en överblick av kundens konton och annars så visas
          // felmeddelande
          createButton.addActionListener(
              e -> {
                String pNo = pNoField.getText().strip();

                // Try/catch för att fånga felaktig datatyp
                try {
                  int accountIdValue = Integer.parseInt(accountIdField.getText().strip());
                  double amount = Double.parseDouble(depositField.getText().strip());
                  boolean foundCustomer = Main.bank.withdraw(pNo, accountIdValue, amount);
                  if (foundCustomer) {
                    // Om kunden hittas så hämtar vi en arraylist av kunden och skickar med till
                    // tabellen så
                    // att vi kan skriva alla uppgifter längst upp
                    ArrayList<String> customer = Main.bank.getCustomer(pNo);
                    MainGui.table.setModel(MainGui.table.loadCustomer(customer));
                  } else {
                    JOptionPane.showMessageDialog(
                        null, "Konto hittades ej eller otillräckligt saldo.");
                  }

                  this.dispose();

                } catch (NumberFormatException error) {
                  JOptionPane.showMessageDialog(null, "Felaktiga värden!");
                }
              });

          break;
        }
      case "Stänga konto":
        {
          JLabel pNoLabel = new JLabel("Personnummer: ");
          JLabel accountId = new JLabel("Kontonummer: ");

          JTextField accountIdField = new JTextField(10);
          JTextField pNoField = new JTextField(10);

          JButton closeAccount = new JButton("Stäng konto");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(pNoLabel, gc);
          gc.gridy = 2;
          add(accountId, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(pNoField, gc);
          gc.gridy = 2;
          add(accountIdField, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(closeAccount, gc);

          // Knappen är bunden till metod som anropar bankens closeAccountmetod
          // Om den lyckas skickas värdet till metoden loadClosedAccount som visar all info inkl
          // slutränta
          closeAccount.addActionListener(
              e -> {
                // Try/catch för att fånga felaktig inmatning
                try {
                  String pNo = pNoField.getText().strip();
                  int accountIdValue = Integer.parseInt(accountIdField.getText().strip());
                  String foundCustomerAccount = Main.bank.closeAccount(pNo, accountIdValue);
                  if (foundCustomerAccount != null) {
                    // Här plockas kontonumret ut och skickas med till nya tabellen så den kan
                    // skrivas ut högst upp
                    String closedAccount = foundCustomerAccount.split(" ")[0];
                    MainGui.table.setModel(
                        MainGui.table.loadClosedAccount(foundCustomerAccount, closedAccount));

                  } else {
                    JOptionPane.showMessageDialog(
                        null, "Konto hittades ej, kontrollera personnummer och kontonummer");
                  }

                  this.dispose();

                } catch (NumberFormatException error) {
                  JOptionPane.showMessageDialog(null, "Felaktiga värden!");
                }
              });

          break;
        }
      case "Öppna konto":
        {
          JLabel pNoLabel = new JLabel("Personnummer: ");

          // Båda radiobuttons placeras i samma ButtonGroup så bara en kan väljas i taget
          ButtonGroup buttonGroup = new ButtonGroup();
          JRadioButton savingsAccount = new JRadioButton("Sparkonto");
          JRadioButton creditAccount = new JRadioButton("Kreditkonto");
          buttonGroup.add(savingsAccount);
          buttonGroup.add(creditAccount);

          JTextField pNoField = new JTextField(10);

          JButton openAccount = new JButton("Öppna konto");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(pNoLabel, gc);
          gc.gridy = 2;
          add(savingsAccount, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(pNoField, gc);
          gc.gridy = 2;
          add(creditAccount, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(openAccount, gc);

          // Knappen är bunden till denna kodsnutt som tar personnumret på en kund och sedan får man
          // välja med en radiobutton
          // om man vill skapa ett sparkonto eller kreditkonto
          openAccount.addActionListener(
              e -> {
                try {
                  String pNo = pNoField.getText().strip();
                  if (savingsAccount.isSelected()) {
                    int newAccountId = Main.bank.createSavingsAccount(pNo);
                    if (newAccountId != -1) {
                      // Om kontot skapas så meddelas användaren och skickas till kundöverblicken
                      JOptionPane.showMessageDialog(
                          null, "Sparkonto skapat med id: " + newAccountId);
                      ArrayList<String> customer = Main.bank.getCustomer(pNo);
                      MainGui.table.setModel(MainGui.table.loadCustomer(customer));
                    } else {
                      JOptionPane.showMessageDialog(
                          null, "Kund hittades ej, kontrollera personnummer");
                    }
                  }
                  if (creditAccount.isSelected()) {
                    int newAccountId = Main.bank.createCreditAccount(pNo);
                    if (newAccountId != -1) {
                      // Om kontot skapas så meddelas användaren och skickas till kundöverblicken
                      JOptionPane.showMessageDialog(
                          null, "Kreditkonto skapat med id: " + newAccountId);
                      ArrayList<String> customer = Main.bank.getCustomer(pNo);
                      MainGui.table.setModel(MainGui.table.loadCustomer(customer));
                    } else {
                      JOptionPane.showMessageDialog(
                          null, "Kund hittades ej, kontrollera personnummer");
                    }
                  }

                  this.dispose();

                } catch (NumberFormatException error) {
                  JOptionPane.showMessageDialog(null, "Felaktiga värden!");
                }
              });

          break;
        }
      case "Hämta transaktioner":
        {
          JLabel pNoLabel = new JLabel("Personnummer: ");
          JLabel accountNumber = new JLabel("Kontonummer: ");

          JTextField pNoField = new JTextField(10);
          JTextField accountNumberField = new JTextField(10);

          JButton getTransactions = new JButton("Hämta transaktioner");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(pNoLabel, gc);
          gc.gridy = 2;
          add(accountNumber, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(pNoField, gc);
          gc.gridy = 2;
          add(accountNumberField, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(getTransactions, gc);

          // Knappen är bunden till den här ActionListenern som anropar Main.bank.getTransactions
          // och sedan visas ett meddelande
          // med resultatet
          getTransactions.addActionListener(
              e -> {
                String pNo = pNoField.getText().strip();
                int accountId = Integer.parseInt(accountNumberField.getText().strip());
                ArrayList<String> transactions = Main.bank.getTransactions(pNo, accountId);
                if (transactions != null) {
                  // Skapar String av kontonumret som skickas med till table.loadTransactions
                  String account = Main.bank.getAccount(pNo.strip(), accountId).split(" ")[0];
                  MainGui.table.setModel(MainGui.table.loadTransactions(transactions, account));

                } else {
                  JOptionPane.showMessageDialog(
                      null, "Kunde inte hitta personnumret eller kontonumret");
                }
                this.dispose();
              });
          break;
        }
    }
  }
}
