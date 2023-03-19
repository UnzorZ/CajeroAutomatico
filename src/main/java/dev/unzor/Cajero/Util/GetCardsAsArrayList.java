package dev.unzor.Cajero.Util;

import dev.unzor.Cajero.Constants;
import dev.unzor.Cajero.Constructors.Card;

import java.sql.*;
import java.util.ArrayList;

public class GetCardsAsArrayList {
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
}
