package dev.unzor.Cajero.Menus;

import dev.unzor.Cajero.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TransactionsTableGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public TransactionsTableGUI() {
        super("Transacciones");
        setLayout(new BorderLayout());

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
        JButton clearButton = new JButton("Borrar base de datos");
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar ActionListener para el botón de recargar tabla
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpiar la tabla
                model.setRowCount(0);
                // Cargar datos desde la base de datos
                loadTransactions();
            }
        });

        // Agregar ActionListener para el botón de borrar base de datos
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de que deseas borrar la base de datos?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    clearDatabase();
                    model.setRowCount(0);
                }
            }
        });

        // Establecer tamaño y visibilidad
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Cargar los datos desde la base de datos
        loadTransactions();
    }

    private void loadTransactions() {
        try {
            // Conectarse a la base de datos
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection(Constants.DB_URL);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TRANSACCIONES");
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
    public void clearDatabase() {
        try (Connection conn = DriverManager.getConnection(Constants.DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM TRANSACCIONES";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

