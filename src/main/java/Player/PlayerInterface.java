package Player;

import Card.CardInterface;

import java.io.Serializable;
import java.util.List;

public interface PlayerInterface extends Serializable {
    long getId();
    String getName();
    List<CardInterface> getHand();
    void setCurrentTurn(boolean currentTurn);
    boolean isCurrentTurn();
    void toggleCurrentTurn();
    void setUno(boolean uno);
    boolean isUno();
    boolean canDraw();
    void setCanDraw(boolean canDraw);
    void addCard(CardInterface card);
}
