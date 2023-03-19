package dev.unzor.Cajero.Util;

import dev.unzor.Cajero.Constructors.Card;

import java.sql.*;
import java.util.ArrayList;

public class GetCardByID {
    public static Card getCardByID(int id) {
        try {
            ArrayList<Card> cards = GetCardsAsArrayList.getCardsAsArrayList();
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
}
