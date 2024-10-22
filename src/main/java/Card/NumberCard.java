package Card;

/**
 * Class for number card (has color and number).
 */
public class NumberCard extends AbstractCard {
    /**
     * Constructor
     *
     * @param type   Type of the card
     * @param color  Color of the card
     * @param number Number of the card
     */
    public NumberCard(CardType type, UnoColor color, int number) {
        super(type, color, number);
    }

    @Override
    boolean isValidCardNumber(int number) {
        return number < 10 && number >= 0;
    }
}
