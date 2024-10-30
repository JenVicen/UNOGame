package Deck;

import Card.*;
import java.util.ArrayList;
import java.util.List;

public class DeckFactory {

    public List<CardInterface> createUnoDeck() {
        List<CardInterface> cards = new ArrayList<>(108);

        for (UnoColor unoColor : UnoColor.values()) {
            if (unoColor != UnoColor.BLACK) {
                cards.add(new NumberCard(CardType.NUMBERCARD, unoColor, 0)); // Single 0 card

                // Two cards for numbers 1-9
                for (int i = 1; i <= 9; i++) {
                    cards.add(new NumberCard(CardType.NUMBERCARD, unoColor, i));
                    cards.add(new NumberCard(CardType.NUMBERCARD, unoColor, i));
                }

                // Action cards: 2 Skips, 2 Reverses, 2 Draw Twos
                addActionCards(cards, unoColor, CardType.SKIP, 99, 2);
                addActionCards(cards, unoColor, CardType.REVERSE, 33, 2);
                addActionCards(cards, unoColor, CardType.DRAWTWO, 22, 2);

            } else {
                // Four Wild and Wild Draw Four cards
                addActionCards(cards, UnoColor.BLACK, CardType.WILD, 88, 4);
                addActionCards(cards, UnoColor.BLACK, CardType.WILDDRAWFOUR, 44, 4);
            }
        }

        return cards;
    }

    private void addActionCards(List<CardInterface> cards, UnoColor color, CardType type, int number, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new ActionCard(type, color, number));
        }
    }
}