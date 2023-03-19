package dev.unzor.Cajero.Menus;

import dev.unzor.Cajero.Constructors.Card;
import dev.unzor.Cajero.Constants;
import dev.unzor.Cajero.Util.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ATMInterface {
    public static JLabel balanceLabel;

    public ATMInterface(Card card) throws SQLException {
        showMainMenu(card);
    }

    public void showMainMenu(Card card) {
        JFrame frame = new JFrame("Cajero Automático");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el panel para el menú principal
        JPanel mainMenuPanel = new JPanel(new GridLayout(4, 1));

        // Añade un label con el nombre del usuario y el saldo de la tarjeta
        JLabel userLabel = new JLabel("Bienvenido, " + card.getNombre() + "!");
        balanceLabel = new JLabel("Su saldo es: " + card.getBalance() + "€");
        mainMenuPanel.add(userLabel);
        mainMenuPanel.add(balanceLabel);

        // Crear los botones para las opciones del menú
        JButton withdrawButton = new JButton("Retirar dinero");
        JButton depositButton = new JButton("Depositar dinero");
        JButton transferButton = new JButton("Transferir dinero");
        JButton historyButton = new JButton("Ver historial de transacciones");
        JButton changePinButton = new JButton("Cambiar PIN");
        JButton logoutButton = new JButton("Cerrar sesión");

        // Añadir los botones al panel del menú principal
        mainMenuPanel.add(withdrawButton);
        mainMenuPanel.add(depositButton);
        mainMenuPanel.add(transferButton);
        mainMenuPanel.add(historyButton);
        mainMenuPanel.add(changePinButton);
        mainMenuPanel.add(logoutButton);

        // Añadir un ActionListener a cada botón
        withdrawButton.addActionListener(e -> showWithdrawMenu(card));
        depositButton.addActionListener(e -> showDepositMenu(card));
        transferButton.addActionListener(e -> showTransferMenu(card));
        historyButton.addActionListener(e -> showTransactionHistory(card));
        changePinButton.addActionListener(e -> showChangePinMenu(card));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginGUI();
        });

        // Añadir el panel del menú principal al frame y mostrarlo
        frame.getContentPane().add(mainMenuPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showWithdrawMenu(Card card) {
        JLabel availableBalanceLabel = new JLabel("Saldo Disponible: " + card.getBalance() + Constants.currency);
        JTextField withdrawalAmountField = new JTextField();

        Object[] message = {
                availableBalanceLabel,
                new JLabel("Cantidad a Retirar:"),
                withdrawalAmountField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Retiro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double withdrawalAmount = Double.parseDouble(withdrawalAmountField.getText());
                if (card.getBalance() >= withdrawalAmount) {
                    card.withdraw(withdrawalAmount);
                    JOptionPane.showMessageDialog(null, "Retiro Exitoso. Su Saldo Actual es: " + card.getBalance() + Constants.currency);
                    ATMInterface.balanceLabel.setText("Saldo Disponible: " + card.getBalance() + "€");
                } else {
                    JOptionPane.showMessageDialog(null, "No hay suficiente saldo en su cuenta", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void showDepositMenu(Card card) {
        JTextField depositAmountField = new JTextField();

        Object[] message = {
                new JLabel("Cantidad a Depositar:"),
                depositAmountField
        };

        int option2 = JOptionPane.showConfirmDialog(null, message, "Depósito", JOptionPane.OK_CANCEL_OPTION);
        if (option2 == JOptionPane.OK_OPTION) {
            try {
                double depositAmount = Double.parseDouble(depositAmountField.getText());
                card.deposit(depositAmount);
                JOptionPane.showMessageDialog(null, "Depósito Exitoso. Su Saldo Actual es: " + card.getBalance() + Constants.currency);
                ATMInterface.balanceLabel.setText("Saldo Disponible: " + card.getBalance() + "€");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showTransferMenu(Card card) {
        JLabel availableBalanceLabel = new JLabel("Saldo Disponible: " + card.getBalance() + Constants.currency);
        JTextField transferAmountField = new JTextField();
        JTextField receiverIdField = new JTextField();

        Object[] message2 = {
                availableBalanceLabel,
                new JLabel("Cantidad a Transferir:"),
                transferAmountField,
                new JLabel("ID del Destinatario:"),
                receiverIdField
        };

        int option3 = JOptionPane.showConfirmDialog(null, message2, "Transferencia", JOptionPane.OK_CANCEL_OPTION);
        if (option3 == JOptionPane.OK_OPTION) {
            try {
                double transferAmount = Double.parseDouble(transferAmountField.getText());
                int receiverId = Integer.parseInt(receiverIdField.getText());

                if (card.getBalance() >= transferAmount && Util.getCardByID(receiverId) != null) {
                    card.transfer(transferAmount, Util.getCardByID(receiverId));
                    int option4 = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea transferir " + transferAmount + Constants.currency + " a la cuenta con ID " + receiverId + "? (" + Util.getCardByID(receiverId).getNombre() + ")", "Confirmar Transferencia", JOptionPane.YES_NO_OPTION);

                    if (option4 == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "Transferencia Exitosa. Su Saldo Actual es: " + card.getBalance() + Constants.currency);
                        ATMInterface.balanceLabel.setText("Saldo Disponible: " + card.getBalance() + Constants.currency);
                    } else {
                        JOptionPane.showMessageDialog(null, "Transferencia Cancelada");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No hay suficiente saldo en su cuenta o el ID de Destinatario no es válido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void showChangePinMenu(Card card) {
        ChangePinGUI changePinGUI = new ChangePinGUI();
        changePinGUI.setVisible(true);
    }

    public static void showTransactionHistory(Card card) {
        TransactionHistoryGUI transactionHistoryGUI = new TransactionHistoryGUI(card);
        transactionHistoryGUI.setVisible(true);
    }

    private static class TransactionHistoryGUI extends JFrame {
        JTable table;
        DefaultTableModel model;
        public TransactionHistoryGUI(Card card) {
                setLayout(new BorderLayout());
                setTitle("Historial de Transacciones");

                // Crear la tabla
                table = new JTable();
                model = new DefaultTableModel();
                model.addColumn("ID_TRANSACCION");
                model.addColumn("ID_USUARIO");
                model.addColumn("FECHA_HORA");
                model.addColumn("TIPO");
                model.addColumn("CANTIDAD");
                model.addColumn("SALDO_RESTANTE");
                table.setModel(model);

                // Agregar la tabla a un JScrollPane
                JScrollPane scrollPane = new JScrollPane(table);
                add(scrollPane, BorderLayout.CENTER);

                // Agregar botones
                JPanel buttonPanel = new JPanel();
                JButton refreshButton = new JButton("Recargar tabla");
                buttonPanel.add(refreshButton);
                add(buttonPanel, BorderLayout.SOUTH);

                // Agregar ActionListener para el botón de recargar tabla
                refreshButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Limpiar la tabla
                        model.setRowCount(0);
                        // Cargar datos desde la base de datos
                        loadTransactions(card);
                    }
                });
                // Establecer tamaño y visibilidad
                setSize(800, 600);
                setLocationRelativeTo(null);
                setVisible(true);

                // Cargar los datos desde la base de datos
                loadTransactions(card);
            }

            private void loadTransactions(Card card) {
                try {
                    // Conectarse a la base de datos
                    Class.forName("org.sqlite.JDBC");
                    Connection con = DriverManager.getConnection(Constants.DB_URL);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM TRANSACCIONES WHERE ID_USUARIO = " + card.getId() + "");
                    // Agregar filas a la tabla
                    while (rs.next()) {
                        Object[] row = new Object[6];
                        row[0] = rs.getInt("ID_TRANSACCION");
                        row[1] = rs.getInt("ID_USUARIO");
                        row[2] = rs.getString("FECHA_HORA");
                        row[3] = rs.getString("TIPO");
                        row[4] = rs.getDouble("CANTIDAD");
                        row[5] = rs.getDouble("SALDO_RESTANTE");
                        model.addRow(row);
                    }
                    // Cerrar la conexión a la base de datos
                    rs.close();
                    stmt.close();
                    con.close();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            private Component clearDatabase() {
                try {
                    // Conectarse a la base de datos
                    Class.forName("org.sqlite.JDBC");
                    Connection con = DriverManager.getConnection(Constants.DB_URL);
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate("DELETE FROM TRANSACCIONES");
                    // Cerrar la conexión a la base de datos
                    stmt.close();
                    con.close();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }

