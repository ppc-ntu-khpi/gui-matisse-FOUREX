package com.mybank.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.mybank.data.DataSource;
import com.mybank.domain.*;
import com.mybank.reporting.CustomerReport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

public class Main extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> clientsComboBox;
    private JTextPane textArea;
    private JButton showButton;
    private JButton reportButton;
    private JButton aboutButton;

    public Main() {
        setContentPane(mainPanel);
        setTitle("Super ultra mega ultimate powered bank application by. FOUREX_dot_py from Brainfuck on top inc.");
        setVisible(true);
        setSize(500, 400);

        showButton.addActionListener(new ShowButtonListener());
        reportButton.addActionListener(new ReportButtonListener());
        aboutButton.addActionListener(new AboutButtonListener());

        for (int i=0; i < Bank.getNumberOfCustomers(); i++) {
            clientsComboBox.addItem(String.format(
                "%s %s", Bank.getCustomer(i).getLastName(), Bank.getCustomer(i).getFirstName()
            ));
        }
    }

    class ShowButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Customer customer = Bank.getCustomer(clientsComboBox.getSelectedIndex());
            StringBuilder text;

            text = new StringBuilder(String.format(
                "<b>%s %s</b>, customer #%d<br>%s<br>",
                customer.getFirstName(), customer.getLastName(), clientsComboBox.getSelectedIndex() + 1,
                "-".repeat(30)
            ));

            for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
                Account account = customer.getAccount(i);

                String accountType = account instanceof CheckingAccount ? "Checking" : "Savings";

                text.append(String.format(
                    "#%d - <b>%s</b>: $%.2f<br>", i, accountType, account.getBalance()
                ));
            }

            textArea.setText(text.toString());
        }
    }

    class ReportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteStream);

            System.setOut(printStream);

            new CustomerReport().generateReport();  // Як так, він виводить все в консоль :(

            System.out.flush();
            System.setOut(System.out);

            textArea.setText(byteStream.toString().replace("\n", "<br>"));
        }
    }

    class AboutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textArea.setText(
                "<h2>Bank application by FOUREX_dot_py<h2>"
            );
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException {
        Locale.setDefault(Locale.US);

        new DataSource("./data/test.dat").loadData();

        UIManager.setLookAndFeel(new FlatDarculaLaf());
        new Main();
    }
}
