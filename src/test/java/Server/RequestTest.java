package Server;

import Card.CardInterface;
import Card.CardType;
import Card.NumberCard;
import Card.UnoColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Request Test")
class RequestTest {

	@Test
	@DisplayName("Get Command Test")
	void getCommand() {
		// Given
		Request request = new Request(Request.Command.START, Request.Direction.CLIENT_TO_SERVER);

		// When

		// Then
		assertEquals(Request.Command.START, request.getCommand());
	}

	@Test
	void getCardWithValidCard() {
		// Given
		CardInterface card = new NumberCard(CardType.NUMBERCARD, UnoColor.RED, 0);
		Request request = new Request(Request.Command.PLAY, Request.Direction.CLIENT_TO_SERVER, "Jen", card);

		// When

		// Then
		assertEquals(card, request.getCard());
	}

}