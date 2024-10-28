package Game;

import Card.CardInterface;
import Card.CardType;
import Card.NumberCard;
import Card.UnoColor;
import Deck.Deck;
import Player.PlayerInterface;
import State.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Test")
class GameTest {

    @Test
    @DisplayName("Initialize Game")
    void testInitilaizeGame() {
        // Given 
        Deck deck = new Deck();
        Game game = new Game(deck);

        // When 
        State initialState = game.getState();

        // Then 
        assertNotNull(initialState);
        assertEquals("Initial state", initialState.getMessage());
        assertFalse(game.isRunning());
    }

    @Test
    @DisplayName("Start Game With Players")
    void testStartGame() {
        // Given 
        Deck deck = new Deck();
        Game game = new Game(deck);
        game.addPlayer("Jennifer");
        game.addPlayer("Daniel");

        // When 
        State state = game.start();
        
        // Then 
        assertTrue(game.isRunning());
        assertEquals(2, state.getPlayers().size());
        assertNotNull(state.getTopDiscardPileCard());
        assertEquals("Game initialized", state.getMessage());
    }

    @Test
    @DisplayName("Start Game without players")
    void testStartGameWithoutPlayers() {
        // Given 
        Deck deck = new Deck();
        Game game = new Game(deck);

        // When 
        boolean exceptionThrown = false;
        try {
            game.start();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
            assertEquals("no player joined the game", e.getMessage());
        }

        // Then 
        assertTrue(exceptionThrown, "Expected IllegalStateException was not thrown");
    }

    @Test
    @DisplayName("playCard - playing the same color card")
    void testPlayCardSameColor() {
        // Given 
        Deck deck = new Deck();
        Game game = new Game(deck);
        game.addPlayer("Alice");
        game.addPlayer("Bob");
        game.start();

        // Get the color of the top discard pile card for a valid play
        UnoColor color = game.getState().getTopDiscardPileCard().getColor();

        // Create a valid card for Alice to play
        CardInterface validCard = new NumberCard(CardType.NUMBERCARD, color, 3);
        PlayerInterface alice = game.getState().getPlayers().get(0);
        alice.addCard(validCard); // Alice adds the valid card

        // When
        game.playCard("Alice", validCard, false, null);

        // Then
        CardInterface topDiscard = game.getState().getTopDiscardPileCard();
        assertEquals(color, topDiscard.getColor(), "The top discard pile card color should match the played card color");
        assertEquals(CardType.NUMBERCARD, topDiscard.getType(), "The top discard pile card type should be NUMBERCARD");
        assertEquals(3, ((NumberCard) topDiscard).getNumber(), "The top discard pile card number should be 3");
    }

    @Test
    @DisplayName("playCard - playing the same number different color")
    void testPlayCardSameNum() {
        // Given 
        Deck deck = new Deck();
        Game game = new Game(deck);
        game.addPlayer("Alice");
        game.addPlayer("Bob");
        game.start();

        // Get the top card of the discard pile
        CardInterface topCard = game.getState().getTopDiscardPileCard();
        int topCardNumber = topCard.getNumber();

        // Create a valid card for Alice to play (same number but different color)
        UnoColor differentColor = (topCard.getColor().equals(UnoColor.GREEN)) ? UnoColor.RED : UnoColor.GREEN; // Example of choosing a different color
        CardInterface differentColorCard = new NumberCard(CardType.NUMBERCARD, differentColor, topCardNumber);
        
        PlayerInterface alice = game.getState().getPlayers().get(0);
        alice.addCard(differentColorCard); // Alice adds the valid card

        // When Alice plays the card
        game.playCard("Alice", differentColorCard, false, null);

        // Then
        assertEquals(differentColorCard, game.getState().getTopDiscardPileCard(), "The top discard pile card should be the card played by Alice");
    }
}