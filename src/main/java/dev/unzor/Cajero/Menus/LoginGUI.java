package dev.unzor.Cajero.Menus;

import dev.unzor.Cajero.Constants;
import dev.unzor.Cajero.Util.GetCardsCount;
import dev.unzor.Cajero.Util.GetCardByID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginGUI extends JFrame implements ActionListener {
    private String DB_URL = Constants.DB_URL;
    private final int MAX_PIN_ATTEMPTS = 3;
    private int pinAttempts = 0;
    private JTextField idField, pinField;
    private JButton loginButton;
    private JButton registerButton;
    private Connection conn;

    public LoginGUI() {
        super("Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 150);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel para campos de entrada y botón de inicio de sesión
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel idLabel = new JLabel("ID de tarjeta:");
        inputPanel.add(idLabel);

        idField = new JTextField();
        inputPanel.add(idField);

        JLabel pinLabel = new JLabel("PIN:");
        inputPanel.add(pinLabel);

        pinField = new JPasswordField();
        inputPanel.add(pinField);

        loginButton = new JButton("Iniciar sesión");
        loginButton.addActionListener(this);

        registerButton = new JButton("Registrarse");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RegisterGUI();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        inputPanel.add(registerButton);
        inputPanel.add(loginButton);

        add(inputPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtener el PIN del campo de entrada
        String id = idField.getText();
        String pin = pinField.getText();

        try {
            if (conn == null) {
                conn = DriverManager.getConnection(DB_URL);
            }

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TARJETAS WHERE id='" + id + "'");

            if (rs.next()) {
                String correctPin = rs.getString("pin");
                double balance = rs.getDouble("saldo");
                String name = rs.getString("nombre");

                if (pin.equals(correctPin)) {
                    // Inicio de sesión exitoso
                    JOptionPane.showMessageDialog(this, "Bienvenido, " + name + "! Su saldo es: " + balance + Constants.currency);
                    dispose();
                    new ATMInterface(GetCardByID.getCardByID(Integer.parseInt(id)));
                } else {
                    // PIN incorrecto
                    pinAttempts++;
                    if (pinAttempts >= MAX_PIN_ATTEMPTS) {
                        // Bloquear la tarjeta después de intentos fallidos
                        stmt.executeUpdate("UPDATE TARJETAS SET bloqueada=1 WHERE id='" + id + "'");
                        GetCardByID.getCardByID(Integer.parseInt(id)).setBlocked(true);
                        JOptionPane.showMessageDialog(this, "La tarjeta ha sido bloqueada. Por favor, contacte al banco.");
                    } else {
                       JOptionPane.showMessageDialog(this, "PIN incorrecto. Intentos restantes: " + (MAX_PIN_ATTEMPTS - pinAttempts));
                    }
                }
            } else {
                // ID de tarjeta no encontrada
                JOptionPane.showMessageDialog(this, "ID de tarjeta no encontrada.");
            }

            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            System.err.println("Error de SQL: " + ex.getMessage());
        }

        if (e.getSource() == registerButton) {
            dispose();
            try {
                RegisterGUI();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void RegisterGUI() throws SQLException {
        int id = GetCardsCount.getCardsCount() + 1;
        String name = JOptionPane.showInputDialog(null, "Ingrese el nombre del titular de la tarjeta:", "Agregar registro", JOptionPane.PLAIN_MESSAGE);
        String pinStr = JOptionPane.showInputDialog(null, "Ingrese el PIN de la tarjeta:", "Agregar registro", JOptionPane.PLAIN_MESSAGE);
        int pin = Integer.parseInt(pinStr);
        double balance = 0;
        boolean bloqueada = JOptionPane.showConfirmDialog(null, "¿Desea bloquear la tarjeta?", "Agregar registro", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        // Insertar el nuevo registro en la base de datos
        try {
            Connection conn = DriverManager.getConnection(Constants.DB_URL);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO TARJETAS(id ,nombre, pin, saldo, bloqueada) VALUES (? ,?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, pin);
            stmt.setDouble(4, balance);
            if (bloqueada) {
                stmt.setInt(5, 1);
            } else {
                stmt.setInt(5, 0);
            }

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted == 1) {
                JOptionPane.showMessageDialog(null, "Registro agregado exitosamente", "Agregar registro", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar el registro", "Agregar registro", JOptionPane.ERROR_MESSAGE);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error adding record: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al agregar el registro: " + e.getMessage(), "Agregar registro", JOptionPane.ERROR_MESSAGE);
        }
    }


    }




