package dev.unzor.Cajero.Util;

import dev.unzor.Cajero.Constants;
import dev.unzor.Cajero.Constructors.Card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExportCardToDatabase {
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
}
