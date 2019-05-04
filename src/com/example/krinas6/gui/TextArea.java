/*
JDialog som skapas när en kundöverblick laddas, den går inte att redigera eller lägga i bakgrunden.
@author Kristoffer Näsström, krinas-6
 */

package com.example.krinas6.gui;

import javax.swing.*;

public class TextArea extends JDialog {

    private JTextArea textArea;

    public TextArea(){
        super();
        this.setTitle("Kundöverblick");
        this.setSize(500, 650);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


        textArea = new JTextArea(35, 45);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }


    public JTextArea getTextArea() {
        return textArea;
    }

}
