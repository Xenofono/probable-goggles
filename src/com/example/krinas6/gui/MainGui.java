// MainGui är huvudfönstret i programmet, den håller allt annat vi ser
// @author Kristoffer Näsström, krinas-6

package com.example.krinas6.gui;

import com.example.krinas6.businesslogic.BankLogic;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class MainGui extends JFrame {

  // Static table eftersom den används på många ställen så det blev ganska förvirrande att hålla
  // reda på utan static.
  static LoadDataAndControlLabel table;

  // Objektet skapas och vi metoden graphicSetup() sköter all setup
  MainGui(String title) {
    super(title);
    graphicSetup();
  }

  // Skapar menyknappen "Hantera kunder" och dess knappar, samt kopplar ihop dem till rätt metoder
  // och klasser
  private JMenu createMenu() {

    JMenu menu = new JMenu("Hantera kunder");

    JMenuItem getCustomers = new JMenuItem("Hämta alla kunder");
    menu.add(getCustomers);
    getCustomers.addActionListener(e -> table.setModel(table.loadAllCustomers()));

    JMenuItem getCustomer = new JMenuItem("Hämta kund");
    menu.add(getCustomer);
    getCustomer.addActionListener(e -> table.setModel(getCustomer()));

    JMenuItem addNewCustomer = new JMenuItem("Lägg till kund");
    menu.add(addNewCustomer);
    addNewCustomer.addActionListener(e -> new ControlCustomerFrame("Lägg till ny kund"));

    JMenuItem changeCustomerName = new JMenuItem("Ändra namn på kund");
    menu.add(changeCustomerName);
    changeCustomerName.addActionListener(e -> new ControlCustomerFrame("Ändra namn på kund"));

    JMenuItem deleteCustomer = new JMenuItem("Ta bort kund");
    menu.add(deleteCustomer);
    deleteCustomer.addActionListener(e -> table.setModel(deleteCustomer()));

    return menu;
  }

  // Skapar menyknappen "Hantera konton" och dess knappar, samt kopplar ihop dem till rätt metoder
  // och klasser
  private JMenu createAccountMenu() {
    JMenu accountMenu = new JMenu("Hantera konton");

    JMenuItem deposit = new JMenuItem("Sätt in pengar");
    accountMenu.add(deposit);
    deposit.addActionListener(e -> new ControlAccountFrame("Insättning"));

    JMenuItem withdrawl = new JMenuItem("Ta ut pengar");
    accountMenu.add(withdrawl);
    withdrawl.addActionListener(e -> new ControlAccountFrame("Uttag"));

    JMenuItem getTransactions = new JMenuItem("Se transaktioner");
    accountMenu.add(getTransactions);
    getTransactions.addActionListener(e -> new ControlAccountFrame("Hämta transaktioner"));

    JMenuItem createAccount = new JMenuItem("Öppna konto");
    accountMenu.add(createAccount);
    createAccount.addActionListener(e -> new ControlAccountFrame("Öppna konto"));

    JMenuItem closeAccount = new JMenuItem("Ta bort konto");
    accountMenu.add(closeAccount);
    closeAccount.addActionListener(e -> new ControlAccountFrame("Stänga konto"));

    return accountMenu;
  }

  // Skapar menyknappen "HFiler" och dess knappar, de gör ingenting i nuläget
  private JMenu createFileMenu() {
    JMenu fileMenu = new JMenu("Filer");

    JMenuItem save = new JMenuItem("Spara kundinformation");
    fileMenu.add(save);
    save.addActionListener(e -> Main.bank.saveClients());

    JMenuItem load = new JMenuItem("Ladda kundinformation");
    fileMenu.add(load);
    load.addActionListener(e -> {
      Main.bank.loadClients();
      table.setModel(table.loadAllCustomers());
    } );

    //Knapp som tar ett personnummer och om kunden hittas så skickas det till BankLogics metod clientSummaryToFile
    JMenuItem customerSummary = new JMenuItem("Spara kundöverblick");
    fileMenu.add(customerSummary);
    customerSummary.addActionListener(e -> {
      String pNo = JOptionPane.showInputDialog(null, "Personnummer på kunden som ska sparas").strip();
      ArrayList<String> customerToSave = Main.bank.getCustomer(pNo);
      if(customerToSave == null){
        JOptionPane.showMessageDialog(null,"Kunde inte hitta kunden, kontrollera personnummer");
      }
      else{
        Main.bank.clientSummaryToFile(customerToSave);
      }


    });

    JMenuItem loadCustomerSummary = new JMenuItem("Ladda kundöverblick");
    fileMenu.add(loadCustomerSummary);
    loadCustomerSummary.addActionListener(e -> fileChooser());

    return fileMenu;
  }

  //Denna metod öppnar upp filväljaren i filmappen och visar bara .txt-filer, när användaren valt en fil så laddas den in
  //i en nytt objekt av klassen TextArea
  private void fileChooser(){
    JFileChooser chooser = new JFileChooser("krinas6_Files");
    chooser.setDialogTitle("Välj en kundsammanfattning att ladda");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt");
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setFileFilter(filter);
    int open = chooser.showOpenDialog(null);


    if(open == JFileChooser.APPROVE_OPTION){
      File selectedFile = chooser.getSelectedFile();
      TextArea textArea = new TextArea();

      try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))){
        textArea.getTextArea().read(reader, "Kundöverblick");
      }catch(IOException e){
        JOptionPane.showMessageDialog(null, "Kunde inte ladda in fil");
      }

    }

  }

  // Metoden begär personummer på kunden som ska hämtas via en JOptionPane, sedan kallas
  // table.loadCustomer om kunden hittas
  private DefaultTableModel getCustomer() {
    String pNo = JOptionPane.showInputDialog("Kundens personnummer");
    ArrayList<String> customer = Main.bank.getCustomer(pNo.strip());
    if (customer != null) {
      // Tables loadCustomer kallas om denne hittades
      return table.loadCustomer(customer);
    } else {
      JOptionPane.showMessageDialog(null, "Kunde inte hitta kund, kolla personnummer");
      return table.loadAllCustomers();
    }
  }

  // Metoden begär personummer på kunden som ska raderas via en JOptionPane, sedan kallas
  // table.deleteCustomer om kunden hittas
  private DefaultTableModel deleteCustomer() {
    String pNo = JOptionPane.showInputDialog("Personnummer på kunden du vill ta bort");
    ArrayList<String> deletedCustomer = Main.bank.deleteCustomer(pNo.strip());
    if (deletedCustomer != null) {
      // tables loadDeletedCustomer kallas med raderade kunden om denne hittades
      return table.loadDeletedCustomer(deletedCustomer);
    } else {
      JOptionPane.showMessageDialog(null, "Kunde inte hitta kund, kolla personnummer");
      return table.loadAllCustomers();
    }
  }

  // Här sätts hela GUI ihop med menyn först och sedan en GridBagLayout där vi lägger label överst
  // och tabellen under
  private void graphicSetup() {
    JMenuBar bar = new JMenuBar();
    this.setJMenuBar(bar);
    bar.add(createMenu());
    bar.add(createAccountMenu());
    bar.add(createFileMenu());

    setLayout(new GridBagLayout());
    GridBagConstraints gc = new GridBagConstraints();

    gc.weightx = 0.5;
    gc.weighty = 0.5;
    gc.insets = new Insets(5, 5, 5, 5);

    // Sapa label som kommer sitta högst upp och förklara vad man tittar på
    JLabel label = new JLabel();
    label.setFont(label.getFont().deriveFont(Font.BOLD, 30));
    // Skapa label som förklarar vad som visas i tabellen, typsnittet ställs till bold och storlek
    // 30.
    table = new LoadDataAndControlLabel(label);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setBackground(Color.white);
    table.setFont(table.getFont().deriveFont(Font.BOLD));

    gc.gridx = 1;
    gc.gridy = 0;
    add(label, gc);
    // GridBagConstraints.BOTH gör att vi fyller ut tillgängliga utrymmet båda på X och Y axeln.
    gc.fill = GridBagConstraints.BOTH;

    gc.gridx = 1;
    gc.gridy = 1;
    add(scrollPane, gc);
  }


}
