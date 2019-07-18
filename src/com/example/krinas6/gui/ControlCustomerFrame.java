// Den här klassen skapar en frame som har flera textfält där vi kan t ex lägga in nya kunder och
// ändra kunder
// @author Kristoffer Näsström, krinas-6

package com.example.krinas6.gui;

import javax.swing.*;
import java.awt.*;

// Klassen ärver JFrame för att underlätta skapandet
class ControlCustomerFrame extends JFrame {



  // Gemensamma egenskaper oavsett vilken sort vi skapar



  ControlCustomerFrame(String title) {
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

    // title används som switchvillkor för att bestämma vilken variant av klassen som ska produceras
    switch (title) {
      case "Lägg till ny kund":
        {
          JLabel firstNameLabel = new JLabel("Förnamn: ");
          JLabel lastNameLabel = new JLabel("Efternamn: ");
          JLabel pNoLabel = new JLabel("Personnummer: ");

          CustomTextField firstNameField = new CustomTextField(10, CustomTextField.PATTERNS.NAMES);
          CustomTextField lastNameField = new CustomTextField(10, CustomTextField.PATTERNS.NAMES);
          CustomTextField pNoField = new CustomTextField(10, CustomTextField.PATTERNS.ONLY_DIGITS);

          JButton createButton = new JButton("Lägg till ny kund");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(firstNameLabel, gc);
          gc.gridy = 2;
          add(lastNameLabel, gc);
          gc.gridy = 3;
          add(pNoLabel, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(firstNameField, gc);
          gc.gridy = 2;
          add(lastNameField, gc);
          gc.gridy = 3;
          add(pNoField, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(createButton, gc);

          // Knappen är bunden till den här ActionListenern som anropar MainGui.bankAccessObject.createCustomer och
          // sedan visas ett meddelande
          // med resultatet
          createButton.addActionListener(
              e -> {
                if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||pNoField.getText().isEmpty()){
                  JOptionPane.showMessageDialog(null, "Ett eller flera fält är tomma");
                }
                else{


                String fName = firstNameField.getFormatted();
                String eName = lastNameField.getFormatted();
                String pNo = pNoField.getFormatted();

                boolean foundCustomer = MainGui.bankAccessObject.createCustomer(fName, eName, pNo);
                if (foundCustomer) {
                  JOptionPane.showMessageDialog(
                      null, "Ny kund med personnummer " + pNo + " inlagd");
                  MainGui.table.setModel(MainGui.table.loadAllCustomers());

                } else {
                  JOptionPane.showMessageDialog(null, "Finns redan någon med det personnumret");
                }
                this.dispose();
                }
              });


          break;
        }
      case "Ändra namn på kund":
        {
          JLabel firstNameLabel = new JLabel("Förnamn: ");
          JLabel lastNameLabel = new JLabel("Efternamn: ");
          JLabel pNoLabel = new JLabel("Personnummer: ");

          CustomTextField firstNameField = new CustomTextField(10, CustomTextField.PATTERNS.NAMES);
          CustomTextField lastNameField = new CustomTextField(10, CustomTextField.PATTERNS.NAMES);
          CustomTextField pNoField = new CustomTextField(10, CustomTextField.PATTERNS.ONLY_DIGITS);

          JButton changeButton = new JButton("Ändra namn på kund");

          // Labels
          gc.gridx = 0;
          gc.gridy = 1;
          add(firstNameLabel, gc);
          gc.gridy = 2;
          add(lastNameLabel, gc);
          gc.gridy = 3;
          add(pNoLabel, gc);

          // Fields
          gc.anchor = GridBagConstraints.CENTER;
          gc.gridy = 1;
          gc.gridx = 1;
          add(firstNameField, gc);
          gc.gridy = 2;
          add(lastNameField, gc);
          gc.gridy = 3;
          add(pNoField, gc);

          // Knapp
          gc.anchor = GridBagConstraints.PAGE_END;
          gc.weighty = 10;
          gc.gridx = 0;
          gc.gridy = 4;
          add(changeButton, gc);

          // Knappen är bunden till den här ActionListenern som anropar MainGui.bankAccessObject.changeCustomerName
          // och sedan visas ett meddelande
          // med resultatet
          changeButton.addActionListener(
              e -> {
                if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||pNoField.getText().isEmpty()){
                  JOptionPane.showMessageDialog(null, "Ett eller flera fält är tomma");
                }
                else {

                  String fName = firstNameField.getFormatted();
                  String eName = lastNameField.getFormatted();
                  String pNo = pNoField.getFormatted();
                  boolean changed = MainGui.bankAccessObject.changeCustomerName(fName, eName, pNo);
                  if (changed) {
                    JOptionPane.showMessageDialog(null, "Namnet ändrades");
                    MainGui.table.setModel(MainGui.table.loadAllCustomers());
                  } else {
                    JOptionPane.showMessageDialog(
                            null, "Kunde inte hitta kunden, kontrollera personnummer");
                  }
                  this.dispose();
                }
              });
          break;
        }
    }
  }

}
