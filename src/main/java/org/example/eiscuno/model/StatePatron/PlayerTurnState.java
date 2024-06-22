package org.example.eiscuno.model.StatePatron;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;

public class PlayerTurnState implements GameState {
    private GameUnoController controller;

    public PlayerTurnState(GameUnoController controller) {
        this.controller = controller;
    }
    @Override
    public void playCard(Card card) {
        controller.humanPlayCard(card);
        controller.setState(controller.getMachineTurnState());
    }

    @Override
    public void drawCard() {
        controller.setState(controller.getMachineTurnState());
    }

    @Override
    public void pressUnoButton() {
        controller.humanPressUnoButton();
    }
}
