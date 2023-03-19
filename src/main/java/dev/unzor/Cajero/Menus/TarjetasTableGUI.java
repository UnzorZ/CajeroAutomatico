package dev.unzor.Cajero.Menus;

import dev.unzor.Cajero.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TarjetasTableGUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public TarjetasTableGUI() {
        super("Tarjetas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuración de la tabla
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        model = new DefaultTableModel(new Object[]{"ID", "PIN", "NOMBRE", "SALDO", "BLOQUEADA"}, 0);
        table.setModel(model);

        // Botones
        JButton refreshBtn = new JButton("Recargar tabla");
        JButton clearBtn = new JButton("Borrar base de datos");
        JButton addBtn = new JButton("Añadir");
        JButton deleteBtn = new JButton("Eliminar");
        JButton editBtn = new JButton("Editar");
        JButton unlockBtn = new JButton("Alternar bloqueo");

        // Configuración del panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(unlockBtn);

        // Configuración del panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Añadir evento para el botón "Recargar tabla"
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData();
            }
        });

        // Añadir evento para el botón "Borrar base de datos"
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearDatabase();
                loadTableData();
            }
        });

        // Añadir evento para el botón "Añadir"
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
                loadTableData();
            }
        });

        // Añadir evento para el botón "Eliminar"
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
                loadTableData();
            }
        });

        // Añadir evento para el botón "Alternar bloqueo"
        unlockBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unlockRecord();
                loadTableData();
            }
        });

        // Añadir evento para el botón "Editar"
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRecord();
                loadTableData();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        loadTableData();
    }

    public void loadTableData() {
        try {
            // Obtener los datos de la base de datos
            Connection conn = DriverManager.getConnection(Constants.DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TARJETAS");

            // Crear un modelo de tabla para mostrar los datos
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "PIN", "Nombre", "Saldo", "Bloqueada"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("pin"), rs.getString("nombre"), rs.getDouble("saldo"), rs.getBoolean("bloqueada")});
            }
            table.setModel(model);

            // Ordenar la tabla por ID de forma ascendente
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            sorter.toggleSortOrder(0);
            table.setRowSorter(sorter);

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error reloading table: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al recargar la tabla: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearDatabase() {
        //Ask for confirmation
        if (JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea borrar la base de datos?", "Borrar base de datos", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection conn = DriverManager.getConnection(Constants.DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM TARJETAS";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRecord() {
        // Ventana de diálogo para ingresar los datos
        int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID de la tarjeta:", "Agregar registro", JOptionPane.PLAIN_MESSAGE));
        String name = JOptionPane.showInputDialog(null, "Ingrese el nombre del titular de la tarjeta:", "Agregar registro", JOptionPane.PLAIN_MESSAGE);
        String pinStr = JOptionPane.showInputDialog(null, "Ingrese el PIN de la tarjeta:", "Agregar registro", JOptionPane.PLAIN_MESSAGE);
        int pin = Integer.parseInt(pinStr);
        String balanceStr = JOptionPane.showInputDialog(null, "Ingrese el saldo de la tarjeta:", "Agregar registro", JOptionPane.PLAIN_MESSAGE);
        double balance = Double.parseDouble(balanceStr);
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

    public void deleteRecord() {
        // Ventana de diálogo para ingresar el ID a eliminar
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del registro a eliminar:", "Eliminar registro", JOptionPane.PLAIN_MESSAGE);
        int id = Integer.parseInt(idStr);

        // Ventana de confirmación
        int confirmation = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Eliminar el registro de la base de datos
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DriverManager.getConnection(Constants.DB_URL);
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM TARJETAS WHERE id = ?");
                stmt.setInt(1, id);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted == 1) {
                    JOptionPane.showMessageDialog(null, "Registro eliminado exitosamente", "Eliminar registro", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el registro", "Eliminar registro", JOptionPane.ERROR_MESSAGE);
                }

                stmt.close();
                conn.close();

            } catch (SQLException e) {
                System.err.println("Error deleting record: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error al eliminar el registro: " + e.getMessage(), "Eliminar registro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void unlockRecord() {
        // Ventana de diálogo para ingresar el ID a desbloquear
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del registro para alternar el bloqueo:", "Alternar bloqueo de registro", JOptionPane.PLAIN_MESSAGE);

        //alternar el estado de bloqueo de la tarjeta
        try {
            Connection conn = DriverManager.getConnection(Constants.DB_URL);
            PreparedStatement stmt = conn.prepareStatement("UPDATE TARJETAS SET bloqueada = NOT bloqueada WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(idStr));
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 1) {
                JOptionPane.showMessageDialog(null, "Registro actualizado exitosamente", "Actualizar registro", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el registro", "Actualizar registro", JOptionPane.ERROR_MESSAGE);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro: " + e.getMessage(), "Actualizar registro", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void editRecord() {
        // Ventana de diálogo para ingresar el ID a editar
        String idStr = JOptionPane.showInputDialog(null, "Ingrese el ID del registro a editar:", "Editar registro", JOptionPane.PLAIN_MESSAGE);
        int id = Integer.parseInt(idStr);

        // Ventana de diálogo para ingresar los datos
        String name = JOptionPane.showInputDialog(null, "Ingrese el nombre del titular de la tarjeta:", "Editar registro", JOptionPane.PLAIN_MESSAGE);
        String pinStr = JOptionPane.showInputDialog(null, "Ingrese el PIN de la tarjeta:", "Editar registro", JOptionPane.PLAIN_MESSAGE);
        int pin = Integer.parseInt(pinStr);
        String balanceStr = JOptionPane.showInputDialog(null, "Ingrese el saldo de la tarjeta:", "Editar registro", JOptionPane.PLAIN_MESSAGE);
        double balance = Double.parseDouble(balanceStr);
        boolean bloqueada = JOptionPane.showConfirmDialog(null, "¿Desea bloquear la tarjeta?", "Editar registro", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

        // Editar el registro en la base de datos
        try {
            Connection conn = DriverManager.getConnection(Constants.DB_URL);
            PreparedStatement stmt = conn.prepareStatement("UPDATE TARJETAS SET nombre = ?, pin = ?, saldo = ?, bloqueada = ? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setInt(2, pin);
            stmt.setDouble(3, balance);
            if (bloqueada) {
                stmt.setInt(4, 1);
            } else {
                stmt.setInt(4, 0);
            }
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 1) {
                JOptionPane.showMessageDialog(null, "Registro actualizado exitosamente", "Editar registro", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el registro", "Editar registro", JOptionPane.ERROR_MESSAGE);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al actualizar el registro: " + e.getMessage(), "Editar registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
