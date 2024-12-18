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

	private final DeckFactory deckFactory;

    public Deck() {
        this.deckFactory = new DeckFactory();
        create();
    }

    void create() {
        drawPile = deckFactory.createUnoDeck();
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
