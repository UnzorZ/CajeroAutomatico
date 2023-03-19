package dev.unzor.Cajero.Util;

import dev.unzor.Cajero.Constants;

import java.sql.*;

public class GetCardsCount {
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
}
