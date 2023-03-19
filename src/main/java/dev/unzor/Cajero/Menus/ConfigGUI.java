package dev.unzor.Cajero.Menus;

import dev.unzor.Cajero.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigGUI extends JFrame {

    private JPanel mainPanel;
    private JTextField urlBaseDatosTextField;
    private JTextField maxIntentosTextField;
    private JTextField monedaTextField;
    private JButton changeConfigButton;
    private JLabel messageLabel;

    public ConfigGUI() {
        super("Cambiar Configuracion");

        // Main panel
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);

        // Card ID label
        JLabel urlBaseDatosLabel = new JLabel("URL de la base de datos:");
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(urlBaseDatosLabel, c);

        // Card ID text field
        urlBaseDatosTextField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(urlBaseDatosTextField, c);

        // Current PIN label
        JLabel maxIntentosLabel = new JLabel("Maximo de intentos:");
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(maxIntentosLabel, c);

        // Current PIN password field
        maxIntentosTextField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(maxIntentosTextField, c);

        // New PIN label
        JLabel monedaLabel = new JLabel("Moneda:");
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(monedaLabel, c);

        // New PIN password field
        monedaTextField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(monedaTextField, c);

        // Change PIN button
        changeConfigButton = new JButton("Guardar Cambios");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        mainPanel.add(changeConfigButton, c);

        // Add main panel to frame
        add(mainPanel);

        // Set frame properties
        setSize(450, 275);
        setLocationRelativeTo(null);
        setDefaultText();

        // Change PIN button action listener
        changeConfigButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String urlBaseDatos = urlBaseDatosTextField.getText();
                String maxIntentos = maxIntentosTextField.getText();
                String moneda = monedaTextField.getText();

                if (urlBaseDatos.isEmpty() || maxIntentos.isEmpty() || moneda.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, rellene todos los campos.");
                } else {
                    Constants.DB_URL = urlBaseDatos;
                    Constants.MAX_ATTEMPTS = Integer.parseInt(maxIntentos);
                    Constants.currency = moneda;
                    JOptionPane.showMessageDialog(null, "Configuracion cambiada con exito. Reinicie el programa para que los cambios surtan efecto.");
                }
            }
        });
    }
    //metodo que convierte un array de char a un string
    public void setDefaultText() {
        urlBaseDatosTextField.setText(Constants.DB_URL);
        maxIntentosTextField.setText(String.valueOf(Constants.MAX_ATTEMPTS));
        monedaTextField.setText(Constants.currency);
    }

}
