package GUI.Events;

import State.State;

public interface RequestEventHandler {
	void playerJoined(State state);

	void gameStarted(State state);

	void played(State state);

	void finished(State state);

	void restarted(State state);
}
