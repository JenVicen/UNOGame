package GUI.Events;

public interface EventListener {
	void addEventHandler(RequestEventHandler requestEventHandler);

	void update(Object event);
}
