package org.example.eiscuno.model.StatePatron;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;

public class MachineTurnState implements GameState{
    private GameUnoController controller;

    public MachineTurnState(GameUnoController controller) {
        this.controller = controller;
    }
    @Override
    public void playCard(Card card) {
        throw new IllegalStateException("It's not the player's turn to play a card.");
    }

    @Override
    public void drawCard() {
        throw new IllegalStateException("It's not the player's turn to draw a card.");
    }

    @Override
    public void pressUnoButton() {
        controller.humanPressUnoButton();
    }
}
