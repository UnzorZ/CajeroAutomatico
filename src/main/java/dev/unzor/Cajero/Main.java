package dev.unzor.Cajero;

import dev.unzor.Cajero.Menus.ConfigGUI;
import dev.unzor.Cajero.Menus.LoginGUI;
import dev.unzor.Cajero.Menus.TarjetasTableGUI;
import dev.unzor.Cajero.Menus.TransactionsTableGUI;
import dev.unzor.Cajero.Util.GetCardsAsArrayList;
import dev.unzor.Cajero.Util.SQLiteDB;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-admin":
                        TransactionsTableGUI transactions = new TransactionsTableGUI();
                        transactions.setVisible(true);

                        TarjetasTableGUI tarjetas = new TarjetasTableGUI();
                        tarjetas.setVisible(true);

                        break;
                    case "-ayuda":
                        System.out.println("[-admin] - Abre la interfaz de administrador");
                        System.out.println("[-ayuda] - Muestra esta ayuda");
                        break;
                    case "-config":
                        System.out.println("Configuración de la aplicación");
                        ConfigGUI configGUI = new ConfigGUI();
                        configGUI.setVisible(true);
                        break;
                }
            }
        }

        SQLiteDB.createTable();
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.setVisible(true);
        GetCardsAsArrayList.getCardsAsArrayList();
    }
}