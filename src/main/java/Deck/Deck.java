package Deck;

import Card.*;
import Player.PlayerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for a uno deck (contains 108 cards as per uno rules).
 */
public class Deck {
	private static Logger logger = LoggerFactory.getLogger(Deck.class);

	private List<CardInterface> drawPile = new ArrayList<>(108);
	private CardInterface topCard = null;
	private List<CardInterface> discardPile = new ArrayList<>(108);

	public Deck() {
		create();
	}

	/**
	 * Generate deck of cards based on game rules
	 */
	void create() {
		// Iterate through every color
		for (UnoColor unoColor : UnoColor.values()) {
			// For all regular colors
			if (unoColor != UnoColor.BLACK) {
				// Create one number card with number: 0
				drawPile.add(new NumberCard(CardType.NUMBERCARD, unoColor, 0));

				// Create two number cards with numbers: 1-9
				for (int i = 1; i <= 9; i++) {
					drawPile.add(new NumberCard(CardType.NUMBERCARD, unoColor, i));
					drawPile.add(new NumberCard(CardType.NUMBERCARD, unoColor, i));
				}

				// Create two skip cards 
				drawPile.add(new ActionCard(CardType.SKIP, unoColor, 99));
				drawPile.add(new ActionCard(CardType.SKIP, unoColor, 99));

				// Create two reverse cards 
				drawPile.add(new ActionCard(CardType.REVERSE, unoColor, 33));
				drawPile.add(new ActionCard(CardType.REVERSE, unoColor, 33));

				// Create two draw two cards 
				drawPile.add(new ActionCard(CardType.DRAWTWO, unoColor, 22));
				drawPile.add(new ActionCard(CardType.DRAWTWO, unoColor, 22));
			} else {
				// Create four wild and draw four cards
				for (int i = 1; i <= 4; i++) {
					drawPile.add(new ActionCard(CardType.WILD, UnoColor.BLACK, 88));
					drawPile.add(new ActionCard(CardType.WILDDRAWFOUR, UnoColor.BLACK, 44));
				}
			}
		}

		Collections.shuffle(drawPile);

		topCard = drawPile.remove(0);

		logger.info("{} cards in draw pile", drawPile.size());
	}

	/**
	 * Distribute cards to players
	 *
	 * @param players ArrayList<IPlayer>
	 */
	public void distribute(List<PlayerInterface> players) {
		// Iterate through every player
		for (PlayerInterface player : players) {
			// Move 7 cards to hand of player
			drawPile.subList(0, 7).forEach(player::addCard);
			drawPile.subList(0, 7).clear();
			logger.info("Distributed {} cards to {}", player.getHand().size(), player.getName());
		}
		logger.info("{} cards in draw pile", drawPile.size());
	}

	public CardInterface getTopCardOfDiscardPile() {
		// Get the last card of the discard pile
		return topCard;
	}

	public CardInterface drawCard() {
		if (drawPile.isEmpty()) {
			logger.info("Draw Pile is exhausted, reshuffling piles...");
			drawPile.addAll(discardPile);
			discardPile.clear();
			drawPile.stream().filter(c -> c.getNumber() == 88).forEach(c -> ((ActionCard) c).reset());
			Collections.shuffle(drawPile);
		}
		logger.info("Drawing a card");
		return drawPile.remove(0);
	}

	public void addCardToDiscardPile(CardInterface card) {
		discardPile.add(topCard);
		topCard = card;
	}

	int getDrawPileSize() {
		return drawPile.size();
	}

	int getDiscardPileSize() {
		return discardPile.size();
	}
}
