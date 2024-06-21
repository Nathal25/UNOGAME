package org.example.eiscuno.model;

import org.example.eiscuno.model.card.Card;

public interface GameState {
    void playCard(Card card);
    void drawCard();
    void pressUnoButton();
}
