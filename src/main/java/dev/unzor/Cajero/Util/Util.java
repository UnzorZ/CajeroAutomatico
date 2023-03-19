package dev.unzor.Cajero.Util;

import dev.unzor.Cajero.Constants;
import dev.unzor.Cajero.Constructors.Card;
import dev.unzor.Cajero.Constructors.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String DB_URL = Constants.DB_URL;
    public static ArrayList<Card> getCardsAsArrayList() throws SQLException {
        Connection connection = DriverManager.getConnection(Constants.DB_URL);

        String query = "SELECT * FROM TARJETAS";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();


        ArrayList<Card> cards = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int pin = resultSet.getInt("pin");
            String name = resultSet.getString("nombre");
            double balance = resultSet.getDouble("saldo");
            boolean blocked = resultSet.getBoolean("bloqueada");

            Card card = new Card(id, pin, name, balance, blocked);
            cards.add(card);
        }
        return cards;
    }
    public static Card getCardByID(int id) {
        try {
            ArrayList<Card> cards = getCardsAsArrayList();
            for (Card card : cards) {
                if (card.getId() == id) {
                    return card;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static void exportCardToDatabase(Card card) throws SQLException {
        Connection connection = DriverManager.getConnection(Constants.DB_URL);

        String query = "UPDATE TARJETAS SET pin = ?, nombre = ?, saldo = ?, bloqueada = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, card.getPin());
        statement.setString(2, card.getNombre());
        statement.setDouble(3, card.getBalance());
        statement.setBoolean(4, card.isBlocked());
        statement.setString(5, String.valueOf(card.getId()));

        statement.executeUpdate();
        connection.close();

    }

    public static int getCardsCount() {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(Constants.DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT COUNT(*) FROM TARJETAS";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(DB_URL);

            String query = "SELECT * FROM TRANSACCIONES WHERE ID_USUARIO = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("ID_TRANSACCION"),
                        rs.getInt("ID_USUARIO"),
                        rs.getString("FECHA_HORA"),
                        rs.getString("TIPO"),
                        rs.getDouble("CANTIDAD"),
                        rs.getDouble("SALDO_RESTANTE"));
                transactions.add(transaction);
            }

            // Cerrar recursos
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }


}
