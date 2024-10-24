package State;

import Card.CardInterface;
import Game.Game;
import Player.Player;
import Player.PlayerInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class State implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<PlayerInterface> players;
	private CardInterface topDiscardPileCard;
	private Game.PlayDirection playDirection;
	private String winner;
	private String message;

	public State() {
		this.playDirection = Game.PlayDirection.TOP_DOWN;
	}

	public State(List<PlayerInterface> players, String message) {
		this.players = players;
		this.message = message;
		this.playDirection = Game.PlayDirection.TOP_DOWN;
	}

	public synchronized Optional<PlayerInterface> getPlayerByName(String name) {
		return players.stream().filter(p -> name.equals(p.getName())).findFirst();
	}

	public synchronized Optional<PlayerInterface> getCurrentPlayer() {
		return players.stream().filter(PlayerInterface::isCurrentTurn).findFirst();
	}

	private synchronized Optional<PlayerInterface> getNextPlayer() {
		Optional<PlayerInterface> optionalCurrentPlayer = getCurrentPlayer();
		if (optionalCurrentPlayer.isPresent()) {
			PlayerInterface currentPlayer = optionalCurrentPlayer.get();
			if (this.playDirection == Game.PlayDirection.TOP_DOWN) {
				return getNextPlayer(currentPlayer);
			} else if (this.playDirection == Game.PlayDirection.BOTTOM_UP) {
				return getPreviousPlayer(currentPlayer);
			}
		}
		return Optional.empty();
	}

	private Optional<PlayerInterface> getNextPlayer(PlayerInterface currentPlayer) {
		int idx = players.indexOf(currentPlayer);
		if (idx < 0) {
			return Optional.empty();
		}
		if (idx + 1 == players.size()) {
			return Optional.of(players.get(0));
		}
		return Optional.of(players.get(idx + 1));
	}

	private Optional<PlayerInterface> getPreviousPlayer(PlayerInterface currentPlayer) {
		int idx = players.indexOf(currentPlayer);
		if (idx < 0) {
			return Optional.empty();
		}
		if (idx == 0) {
			return Optional.of(players.get(players.size() - 1));
		}
		return Optional.of(players.get(idx - 1));
	}
	
	// Function to toggle playDirection when reverse is called 
	public synchronized void togglePlayDirection() {
		if (playDirection == Game.PlayDirection.BOTTOM_UP) {
			playDirection = Game.PlayDirection.TOP_DOWN;
		} else {
			playDirection = Game.PlayDirection.BOTTOM_UP;
		}
	}

	public synchronized void toggleCurrentTurn() {
		if (players.size() == 2) {
			players.forEach(p -> p.setCanDraw(true));
			players.forEach(PlayerInterface::toggleCurrentTurn);
		} else {
			Optional<PlayerInterface> optionalNextPlayer = getNextPlayer();
			if (optionalNextPlayer.isPresent()) {
				PlayerInterface nextPlayer = optionalNextPlayer.get();
				Optional<PlayerInterface> optionalCurrentPlayer = getCurrentPlayer();
				if(optionalCurrentPlayer.isPresent()){
					optionalCurrentPlayer.get().toggleCurrentTurn();
				}
				nextPlayer.toggleCurrentTurn();
				nextPlayer.setCanDraw(true);
			}
		}
	}

	public synchronized void addPlayer(String playerName) {
		if (this.players == null) {
			this.players = new ArrayList<>();
		}
		this.players.add(new Player(playerName, (long) this.players.size()));
	}

	public synchronized void removePlayers(){
		this.players = new ArrayList<>();
	}

	public synchronized Boolean containsPlayer(String playerName) {
		if (this.players != null) {
			return this.players.stream().anyMatch(p -> p.getName().equals(playerName));
		}
		return false;
	}

	public synchronized List<PlayerInterface> getPlayers() {
		return this.players;
	}

	public synchronized void setPlayers(List<PlayerInterface> players) {
		this.players = players;
	}

	public synchronized String getWinner() {
		return winner;
	}

	public synchronized void setWinner(String winner) {
		this.winner = winner;
	}

	public synchronized String getMessage() {
		return message;
	}

	public synchronized void setMessage(String message) {
		this.message = message;
	}

	public synchronized CardInterface getTopDiscardPileCard() {
		return topDiscardPileCard;
	}

	public synchronized void setTopDiscardPileCard(CardInterface topDiscardPileCard) {
		this.topDiscardPileCard = topDiscardPileCard;
	}

	public State restart() {
		removePlayers();
		setWinner("");
		return this;
	}
}
