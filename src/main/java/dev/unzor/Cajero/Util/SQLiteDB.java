package dev.unzor.Cajero.Util;

import dev.unzor.Cajero.Constants;

import java.sql.*;

public class SQLiteDB {
    private static Connection connect(){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(Constants.DB_URL);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        return c;
    }

    public static void createTable() {
        Connection c = connect();
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS TARJETAS " +
                    "(ID INT PRIMARY KEY NOT NULL," +
                    " PIN TEXT NOT NULL, " +
                    " NOMBRE TEXT NOT NULL, " +
                    " SALDO INT NOT NULL, " +
                    " BLOQUEADA BIT NOT NULL)";
            stmt.executeUpdate(sql);

            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS TRANSACCIONES (" +
                    "ID_TRANSACCION INTEGER PRIMARY KEY, " +
                    "ID_USUARIO INTEGER, " +
                    "FECHA_HORA TEXT, " +
                    "TIPO TEXT, " +
                    "CANTIDAD REAL, " +
                    "SALDO_RESTANTE REAL)";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (SQLException ignored) {}
    }
}
