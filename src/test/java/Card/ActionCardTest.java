package Card;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionCardTest {

    @Test
    @DisplayName("Create valid action card")
    void testCreationOfValidActionCard() {
        // Given
        ActionCard card = new ActionCard(CardType.WILD, UnoColor.BLACK, 88);

        // When

        // Then
        assertEquals(UnoColor.BLACK, card.getColor());
        assertEquals("Black", UnoColor.BLACK.getColor());
        assertEquals(88, card.getNumber());
        assertEquals(CardType.WILD, card.getType());
        assertEquals("Wild", CardType.WILD.getType());
    }

    @Test
    @DisplayName("Create valid action card")
    void testValidActionCardNumbers() {
        // Given
        ActionCard card = new ActionCard(CardType.WILD, UnoColor.BLACK, 88);

        // When

        // Then
        assertTrue(card.isValidCardNumber(22));
        assertTrue(card.isValidCardNumber(33));
        assertTrue(card.isValidCardNumber(44));
        assertTrue(card.isValidCardNumber(88));
        assertTrue(card.isValidCardNumber(99));
    }

    @Test
    @DisplayName("Choose color for wildcard")
    void testChooseActionCardColor() {
        // Given
        ActionCard card = new ActionCard(CardType.WILD, UnoColor.BLACK, 88);

        // When
        card.chooseColor(UnoColor.RED);

        // Then
        assertEquals(UnoColor.RED, card.getColor());
    }

    @Test
    @DisplayName("Reset color for wildcard")
    void testResetActionCardColor() {
        // Given
        ActionCard card = new ActionCard(CardType.WILD, UnoColor.BLACK, 88);

        // When
        card.chooseColor(UnoColor.RED);
        card.reset();

        // Then
        assertEquals(UnoColor.BLACK, card.getColor());
    }

    @Test
    @DisplayName("Test creation of SKIP card")
    void testCreationOfSkipCard() {
        ActionCard skipCard = new ActionCard(CardType.SKIP, UnoColor.RED, 99);

        assertEquals(CardType.SKIP, skipCard.getType());
        assertEquals(UnoColor.RED, skipCard.getColor());
        assertEquals("Skip", skipCard.getType().getType());
    }

    @Test
    @DisplayName("Test creation of REVERSE card")
    void testCreationOfReverseCard() {
        ActionCard reverseCard = new ActionCard(CardType.REVERSE, UnoColor.GREEN, 33);

        assertEquals(CardType.REVERSE, reverseCard.getType());
        assertEquals(UnoColor.GREEN, reverseCard.getColor());
        assertEquals("Reverse", reverseCard.getType().getType());
    }

    @Test
    @DisplayName("Test creation of DRAWTWO card")
    void testCreationOfDrawTwoCard() {
        ActionCard drawTwoCard = new ActionCard(CardType.DRAWTWO, UnoColor.BLUE, 22);

        assertEquals(CardType.DRAWTWO, drawTwoCard.getType());
        assertEquals(UnoColor.BLUE, drawTwoCard.getColor());
        assertEquals("Draw 2", drawTwoCard.getType().getType());
    }

    @Test
    @DisplayName("Test creation of WILDDRAWFOUR card")
    void testCreationOfWildDrawFourCard() {
        ActionCard wildDrawFour = new ActionCard(CardType.WILDDRAWFOUR, UnoColor.BLACK, 44);

        assertEquals(CardType.WILDDRAWFOUR, wildDrawFour.getType());
        assertEquals(UnoColor.BLACK, wildDrawFour.getColor());
        assertEquals("Wild Draw 4", wildDrawFour.getType().getType());
    }

    @Test
    @DisplayName("Test WILD card color change and reset")
    void testWildCardColorChangeAndReset() {
        ActionCard wildCard = new ActionCard(CardType.WILD, UnoColor.BLACK, 88);

        // Color change
        wildCard.chooseColor(UnoColor.BLUE);
        assertEquals(UnoColor.BLUE, wildCard.getColor());

        // Reset color
        wildCard.reset();
        assertEquals(UnoColor.BLACK, wildCard.getColor());
    }

    @Test
    @DisplayName("Test WILDDRAWFOUR color change and reset")
    void testWildDrawFourColorChangeAndReset() {
        ActionCard wildDrawFour = new ActionCard(CardType.WILDDRAWFOUR, UnoColor.BLACK, 44);

        // Color change
        wildDrawFour.chooseColor(UnoColor.GREEN);
        assertEquals(UnoColor.GREEN, wildDrawFour.getColor());

        // Reset color
        wildDrawFour.reset();
        assertEquals(UnoColor.BLACK, wildDrawFour.getColor());
    }
}