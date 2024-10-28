package Player;

import Card.CardType;
import Card.NumberCard;
import Card.UnoColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Player")
class PlayerTest {

	@Test
	@DisplayName("Test creation of player")
	void testCreationOfPlayer() {
		// Given
		PlayerInterface player1 = new Player("Jen");
		PlayerInterface player2 = new Player("Dani", 1L);

		// When

		// Then
		assertEquals(player1.getName(), "Jen");
		assertEquals(player2.getId(), 1L);
	}

	@Test
	@DisplayName("Test setting current turn of the player")
	void testSettingPlayerCurrentTurn() {
		// Given
		PlayerInterface player = new Player("Jen");

		// When
		player.setCurrentTurn(true);

		// Then
		assertTrue(player.isCurrentTurn());
	}

	@Test
	@DisplayName("Test toggle current turn of the player")
	void testTogglePlayerCurrentTurn() {
		// Given
		PlayerInterface player = new Player("Jen");

		// When
		player.setCurrentTurn(false);
		player.toggleCurrentTurn();

		// Then
		assertTrue(player.isCurrentTurn());
	}

	@Test
	@DisplayName("Test uno of player")
	void testSettingPlayerUno() {
		// Given
		PlayerInterface player = new Player("Jen");

		// When
		player.setUno(true);

		// Then
		assertTrue(player.isUno());
	}

	@Test
	@DisplayName("Test add card to player")
	void testAddCardToPlayer() {
		// Given
		PlayerInterface player = new Player("Jen");

		// When
		player.addCard(new NumberCard(CardType.NUMBERCARD, UnoColor.RED, 1));

		// Then
		assertEquals(1, player.getHand().size());
	}

	@Test
	@DisplayName("Test set can draw flag")
	void testSetCanDrawFlag() {
		// Given
		PlayerInterface player = new Player("Jen");

		// When
		player.setCanDraw(true);

		// Then
		assertTrue(player.canDraw());
	}
}