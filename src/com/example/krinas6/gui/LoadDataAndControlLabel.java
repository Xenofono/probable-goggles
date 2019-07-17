// LoadDataAndControlLabel ansvarar för att bygga de tabeller som ritas ut och samtidit justera
// Label längst upp för att visa vad man ser
// @author Kristoffer Näsström, krinas-6

package com.example.krinas6.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;

// Klassen ärver JTable för att det ska bli lite lättare att skapa
class LoadDataAndControlLabel extends JTable {

  private JLabel label;

  // Privat DefaultTableModel som vi tilldelar inne i de olika metoderna
  private DefaultTableModel tblModel;

  // När objektet skapas så laddar den automatiskt alla kunder via loadAllCustomers, vi kopplar
  // också in MainGuis label
  LoadDataAndControlLabel(JLabel label) {
    this.label = label;
    this.setModel(loadAllCustomers());
  }

  // Skapar tabell med alla kunder
  DefaultTableModel loadAllCustomers() {
    String[] columns = {"Förnamn", "Efternamn", "Personnummer"};
    tblModel = new DefaultTableModel(columns, 0);
    this.setModel(tblModel);

    // Hämtar lista med alla kunder
    ArrayList<String> customers = MainGui.bankAccessObject.getAllCustomers();

    // Ändrar text på label, skapar en 2d-array stor nog att hålla alla kunder och skickar allting
    // till generateTableData
    label.setText("Alla kunder");
    String[][] tableValues = new String[customers.size()][3];
    return generateTableData(columns, customers, tableValues, 0);
  }

  // Skapar tabell med överblick av en kund
  DefaultTableModel loadCustomer(ArrayList<String> customer) {

    String[] columns = {"Kontonummer", "Saldo", "Kontotyp", "Ränteprocent"};
    tblModel = new DefaultTableModel(columns, 0);
    this.setModel(tblModel);

    // Ändrar label till kundens namn och personummer
    // Sedan skapas 2d-array tableValues som är stor nog att hålla alla kundens konton, den skickas
    // till generateTableData
    label.setText(customer.get(0));
    String[][] tableValues = new String[customer.size() - 1][4];
    return generateTableData(columns, customer, tableValues, 1);
  }

  // Skapar en tabell som visar en överblick på en raderad kunds alla konton
  DefaultTableModel loadDeletedCustomer(ArrayList<String> customer) {

    String[] columns = {"Kontonummer", "Saldo", "Kontotyp", "Ränteprocent", "Slutränta"};
    tblModel = new DefaultTableModel(columns, 0);
    this.setModel(tblModel);

    // Ändrar label till kundens namn och personummer
    // Sedan skapas 2d-array tableValues som är stor nog att hålla alla kundens konton inkl
    // slutränta, den skickas till generateTableData
    label.setText(customer.get(0) + " slutrapport");
    String[][] tableValues = new String[customer.size() - 1][5];
    return generateTableData(columns, customer, tableValues, 1);
  }

  // Skapar en tabell med alla transaktioner i ett visst konto
  DefaultTableModel loadTransactions(ArrayList<String> transactions, String account) {
    String[] columns = {"Datum", "Tid", "Transaktion", "Saldo"};
    tblModel = new DefaultTableModel(columns, 0);
    this.setModel(tblModel);

    label.setText("Kontonummer: " + account);
    String[][] tableValues = new String[transactions.size()][4];
    return generateTableData(columns, transactions, tableValues, 0);
  }

  // Skapar en tabell av det stängda kontots info
  DefaultTableModel loadClosedAccount(String closedAccount, String account) {
    String[] columns = {"Kontonummer", "Saldo", "Kontotyp", "Ränteprocent", "Slutränta"};
    tblModel = new DefaultTableModel(columns, 0);
    this.setModel(tblModel);

    // Label visar vilket konto som har stängts, sedan omvandlar vi infon till en ArrayList för att
    // den ska bli kompatibel med generateTableData
    label.setText("Konto: " + account + " har stängts, vänliga kontrollera slutränta");
    ArrayList<String> accountInfoList =  new ArrayList<>(Arrays.asList(closedAccount));
    String[][] tableValues = new String[1][5];

    return generateTableData(columns, accountInfoList, tableValues, 0);
  }

  // Den här metoden tar indata från de andra och bygger DefaultTableModel som skickas tillbaka.
  // Variabeln startindex är för att vi vill börja på 0 i vissa models och 1 när vi vill skala bort
  // första värdet t ex när
  // loadCustomer kallar så vill vi bara ha kontoinformationen, inte index 0 som är kunduppgifter.
  private DefaultTableModel generateTableData(
      String[] columns, ArrayList<String> list, String[][] tableValues, int startIndex) {
    // Värdena plockas ut ur listan och förs in i tableValues

    for (int i = startIndex; i < list.size(); i++) {
      // Varje värde i list är en hel rad så vi använder split för att göra en array av varje värde
      // i list
      String[] values = list.get(i).split(" ");
      for (int k = 0; k < values.length; k++) {
        tableValues[i - startIndex][k] = values[k];
      }
    }
    return new DefaultTableModel(tableValues, columns);
  }
}
