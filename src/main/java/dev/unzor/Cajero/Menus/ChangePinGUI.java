package dev.unzor.Cajero.Menus;

import dev.unzor.Cajero.Constructors.Card;
import dev.unzor.Cajero.Util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ChangePinGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField cardIdTextField;
    private JPasswordField currentPinPasswordField;
    private JPasswordField newPinPasswordField;
    private JPasswordField confirmPinPasswordField;
    private JButton changePinButton;
    private JLabel messageLabel;

    public ChangePinGUI() {
        super("Change PIN");

        // Main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);

        // Card ID label
        JLabel cardIdLabel = new JLabel("ID de tarjeta:");
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(cardIdLabel, c);

        // Card ID text field
        cardIdTextField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(cardIdTextField, c);

        // Current PIN label
        JLabel currentPinLabel = new JLabel("Pin actual:");
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(currentPinLabel, c);

        // Current PIN password field
        currentPinPasswordField = new JPasswordField(20);
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(currentPinPasswordField, c);

        // New PIN label
        JLabel newPinLabel = new JLabel("Nuevo PIN:");
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(newPinLabel, c);

        // New PIN password field
        newPinPasswordField = new JPasswordField(20);
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(newPinPasswordField, c);

        // Confirm PIN label
        JLabel confirmPinLabel = new JLabel("Confirmar el nuevo PIN:");
        c.gridx = 0;
        c.gridy = 3;
        mainPanel.add(confirmPinLabel, c);

        // Confirm PIN password field
        confirmPinPasswordField = new JPasswordField(20);
        c.gridx = 1;
        c.gridy = 3;
        mainPanel.add(confirmPinPasswordField, c);

        // Change PIN button
        changePinButton = new JButton("Cambiar PIN");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        mainPanel.add(changePinButton, c);

        // Message label
        messageLabel = new JLabel();
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        mainPanel.add(messageLabel, c);

        // Add main panel to frame
        add(mainPanel);

        // Set frame properties
        setSize(450, 300);
        setLocationRelativeTo(null);

        // Change PIN button action listener
        changePinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card target = Util.getCardByID(Integer.parseInt(cardIdTextField.getText()));
                try {
                    target.changePin(Integer.parseInt(charToString(currentPinPasswordField.getPassword())), charToString(newPinPasswordField.getPassword()), charToString(confirmPinPasswordField.getPassword()));
                    JOptionPane.showMessageDialog(null, "Ha actualizado su PIN correctamente");
                    dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
    }
    //metodo que convierte un array de char a un string
    public String charToString(char[] array){
        String string = "";
        for (int i = 0; i < array.length; i++) {
            string += array[i];
        }
        return string;
    }

}
