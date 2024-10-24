package Game;

import Card.ActionCard;
import Card.CardInterface;
import Card.CardType;
import Card.UnoColor;
import Deck.Deck;
import Player.PlayerInterface;
import State.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
	private static Logger logger = LoggerFactory.getLogger(Game.class);
	private State state;
	private Deck deck;
	private boolean isRunning;

	public Game(Deck deck) {
		this.deck = deck;
		initialize();
	}

	boolean isRunning() {
		return isRunning;
	}

	public synchronized State addPlayer(String playerName) {
		if (!isRunning) {
			state.addPlayer(playerName);
		}
		return state;
	}

	synchronized void initialize() {
		logger.info("Starting the game");
		state = new State(new ArrayList<>(), "Initial state");
	}

	public synchronized State start() {
		if (state.getPlayers().isEmpty()) {
			throw new IllegalStateException("no player joined the game");
		}

		if (!isRunning && state.getPlayers().size() >= 2) {
			// Distribute cards
			deck.distribute(state.getPlayers());

			// Setting a randomized order of Turns
			int randNumber = ThreadLocalRandom.current().nextInt(0, state.getPlayers().size() - 1);
			state.getPlayers().get(randNumber).setCurrentTurn(true);

			// Write player info to state
			state.setPlayers(state.getPlayers());

			// Placing the topCard only when it isn't a black card 
			CardInterface topCard;
			do {
				topCard = deck.getTopCardOfDiscardPile();
			} while (topCard.getColor().equals(UnoColor.BLACK));
			state.setTopDiscardPileCard(topCard);
			// state.setTopDiscardPileCard(deck.getTopCardOfDiscardPile());
			isRunning = true;
			state.setMessage("Game initialized");
			return state;
		}
		return new State();
	}

	public synchronized State restart() {
		isRunning = false;
		deck = new Deck();
		return state.restart();
	}

	// Method in charge of playing a card, validating, changing sequence and placing in discard pile 
	public synchronized void playCard(String playerName, CardInterface card, boolean uno, UnoColor chosenColor) {
		PlayerInterface player;
		Optional<PlayerInterface> optionalOfPlayer = state.getPlayerByName(playerName);
		if (optionalOfPlayer.isPresent()) {
			player = optionalOfPlayer.get();
		} else {
			throw new IllegalArgumentException("playerName");
		}
		
		// Check if it's the player's turn and if the player's hand contains the mentioned card
		if (player.isCurrentTurn() && playersHandContainsExactCard(player, card)) {
			// Check if card matches current top card
			if (card.getColor().equals(state.getTopDiscardPileCard().getColor()) ||
					card.getNumber() == state.getTopDiscardPileCard().getNumber() ||
					card.getType().equals(CardType.WILD) ||
					state.getTopDiscardPileCard().getType().equals(CardType.WILD)
			) {
				// Remove from players hand
				removeCardFromPlayersHand(player, card);
				player.setUno(uno);

				// If it's a wildcard change the color
				if (card.getType().equals(CardType.WILD)) {
					((ActionCard) card).chooseColor(chosenColor);
				}

				logger.info("Player {} played card {} / {}", player.getName(), card.getColor(), card.getNumber());
				logger.info("Player {} has {} cards remaining in hand", player.getName(), player.getHand().size());

				// Check if player has won the game
				if (player.getHand().size() == 0) {
					state.setWinner(player.getName());
					state.setMessage("Player " + player.getName() + " has won the game");
					logger.info("Player {} has won the game", player.getName());
				} else {
					// Make new top card
					deck.addCardToDiscardPile(card);
					state.setTopDiscardPileCard(card);
					logger.info("Top card is {} / {}", card.getColor(), card.getNumber());

					// Handle the skip card 
					if(card.getType().equals(CardType.SKIP)){
						logger.info("Player {} played a SKIP card, next player's turn is skipped", player.getName());
						state.toggleCurrentTurn();		// Toggle the turn twice 
					}

					// Handle the reverse card 
					if(card.getType().equals(CardType.REVERSE)){
						logger.info("Player {} played a REVERSE card, play direction changed", player.getName());
						state.togglePlayDirection();	// Change the direction
					}

					// Toggle current turn flags
					checkUno();
					state.toggleCurrentTurn();
				}
			} else {
				state.setMessage("Invalid turn");
			}
		} else {
			state.setMessage("Invalid turn");
		}
	}		

	public synchronized void check(String playerName) {
		// Only Check and Play can trigger the next players turn
		state.toggleCurrentTurn();
	}

	public synchronized void drawCard(String playerName) {
		PlayerInterface player;
		Optional<PlayerInterface> optionalOfPlayer = state.getPlayerByName(playerName);

		if (optionalOfPlayer.isPresent()) {
			player = optionalOfPlayer.get();
		} else {
			throw new IllegalArgumentException("playerName");
		}

		// Check if is the players turn and he didn't already draw
		if (player.isCurrentTurn() && player.canDraw()) {
			player.addCard(deck.drawCard());
			player.setCanDraw(false);

			logger.info("Player {} has {} cards remaining in hand", player.getName(), player.getHand().size());

		} else {
			state.setMessage("Invalid turn");
		}
	}

	public synchronized State getState() {
		return Objects.requireNonNullElseGet(state, State::new);
	}

	// Function in charge of checking if the card is in the player's hand 
	private boolean playersHandContainsExactCard(PlayerInterface player, CardInterface playedCard) {
		return player.getHand().stream().anyMatch(
				card -> (card.getColor().equals(playedCard.getColor()) && card.getNumber() == playedCard.getNumber()));
	}

	// Once used remove the card from the player's hand 
	private void removeCardFromPlayersHand(PlayerInterface player, CardInterface playedCard) {
		player.getHand().stream().filter(
				card -> card.getColor().equals(playedCard.getColor()) && card.getNumber() == playedCard.getNumber())
				.findFirst().ifPresent(card -> player.getHand().remove(card));
	}
	
	private void checkUno() {
		Optional<PlayerInterface> currentPlayerOptional = state.getCurrentPlayer();
		if (currentPlayerOptional.isPresent()) {
			PlayerInterface currentPlayer = currentPlayerOptional.get();
			// Check if player forgot to say UNO
			if (currentPlayer.getHand().size() == 1 && !currentPlayer.isUno()) {
				currentPlayer.addCard(deck.drawCard());
				logger.info("{} forgot to say UNO and therefore drawn 1 penalty card", currentPlayer.getName());
			} else if (currentPlayer.getHand().size() > 1 && currentPlayer.isUno()) {
				currentPlayer.addCard(deck.drawCard());
				logger.info("{} said UNO but had more than 1 card and therefore drawn 1 penalty card",
						currentPlayer.getName());
			}
		}
	}

	public enum PlayDirection {
		BOTTOM_UP,
		TOP_DOWN
	}
}
