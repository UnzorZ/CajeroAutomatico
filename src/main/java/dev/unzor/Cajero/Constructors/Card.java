package dev.unzor.Cajero.Constructors;

import dev.unzor.Cajero.Constants;
import dev.unzor.Cajero.Util.Util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Card {
    private int id;
    private int pin;
    private String nombre;
    private double balance;
    private boolean isBlocked = false;
    private ArrayList<String> transactions = new ArrayList<>();

    public Card(int id, int pin, String nombre, double balance, boolean isBlocked) {
        this.id = id;
        this.pin = pin;
        this.nombre = nombre;
        this.balance = balance;
        this.isBlocked = isBlocked;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) throws SQLException {
        this.id = id;
        Util.exportCardToDatabase(this);
    }

    public int getPin() {
        return pin;
    }
    public void setPin(int pin) throws SQLException {
        this.pin = pin;
        Util.exportCardToDatabase(this);
    }
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) throws SQLException {
        this.balance = balance;
        Util.exportCardToDatabase(this);
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) throws SQLException {
        this.isBlocked = blocked;
        Util.exportCardToDatabase(this);
    }

    public boolean checkPin(int pin) {
        if (this.pin == pin) {
            return true;
        } else {
            return false;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws SQLException {
        this.nombre = nombre;
        Util.exportCardToDatabase(this);
    }

    // Otros métodos de la clase Card, como depositar, retirar, transferir, etc.

    public void withdraw(double amount) throws SQLException {
        if (amount > 0 && amount <= this.balance && !this.isBlocked) {
            this.balance -= amount;
            addTransaction(id, "Retirada", amount, balance);
        }
        Util.exportCardToDatabase(this);
    }

    public void deposit(double amount) throws SQLException {
        if (amount > 0 && !this.isBlocked) {
            this.balance += amount;
            addTransaction(id, "Depósito", amount, balance);
        }
        Util.exportCardToDatabase(this);
    }

    public void transfer(double amount, Card recipient) throws SQLException {
        if (amount > 0 && amount <= this.balance && !this.isBlocked) {
            this.balance -= amount;
            recipient.balance += amount;
            addTransaction(id, "Transferencia -"  + Constants.currency, amount, balance);
            addTransaction(recipient.id, "Transferencia +" + Constants.currency, amount, recipient.balance);
        }
        Util.exportCardToDatabase(this);
    }

    public static void addTransaction(int id_usuario, String tipo, double cantidad, double saldo_restante) throws SQLException {
        Connection connection = DriverManager.getConnection(Constants.DB_URL);
        //get a formatted date and time
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int id_transaccion = getTransactionCount() + 1;

        String query = "INSERT INTO TRANSACCIONES (id_transaccion, id_usuario, fecha_hora, tipo, cantidad, saldo_restante) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id_transaccion);
        statement.setInt(2, id_usuario);
        statement.setString(3, fecha.format(new java.util.Date()));
        statement.setString(4, tipo);
        statement.setDouble(5, cantidad);
        statement.setDouble(6, saldo_restante);
        statement.executeUpdate();
        connection.close();
    }
    public static int getTransactionCount() {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(Constants.DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT COUNT(*) FROM TRANSACCIONES";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
    public void changePin(int currentPin, String newPin, String confirmPin) throws SQLException {
        if (!this.isBlocked() && currentPin == this.getPin()) {
            if (newPin.equals(confirmPin)) {
                this.setPin(Integer.parseInt(newPin));
                // actualiza el pin en la base de datos
                Util.exportCardToDatabase(this);
                System.out.println("Pin actualizado correctamente.");
            } else {
                System.out.println("El nuevo pin no coincide con la confirmación.");
            }
        } else {
            System.out.println("El pin actual no es correcto.");
            System.out.println("Pin actual: " + currentPin);
            System.out.println("Pin de la tarjeta: " + this.getPin());
            System.out.println("Pin confirmacion1: " + newPin);
            System.out.println("Pin confirmacion2: " + confirmPin);
        }
    }

}
