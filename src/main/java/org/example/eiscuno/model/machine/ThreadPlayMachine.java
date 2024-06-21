package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private volatile Card lastPlayedCard;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
    }
    public void run() {
        while (true) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    // Coloca la carta en la mesa si es válida
    private void putCardOnTheTable() {
        Card card = getValidCardToPlay();

        if (card != null) {
            table.addCardOnTheTable(card);
            tableImageView.setImage(card.getImage());
            machinePlayer.getCardsPlayer().remove(card); // Suponiendo que remove() acepta un objeto Card
            System.out.println("Se añadió " + tableImageView.getImage());
            setLastPlayedCard(card);
        } else {
            // Si no hay carta válida para jugar, tomar una carta del mazo
            Card drawnCard = new Deck().takeCard();
            if (validCardToPlay(drawnCard)) {
                table.addCardOnTheTable(drawnCard);
                tableImageView.setImage(drawnCard.getImage());
                setLastPlayedCard(drawnCard);
            } else {
                // Si la carta del mazo tampoco es válida, agregarla al mazo del jugador de la máquina
                machinePlayer.addCard(drawnCard);
                setHasPlayerPlayed(false);
            }
        }
    }

    // Verifica si la carta puede ser jugada según las reglas del juego
    private Card getValidCardToPlay() {
        try {
            for (Card card : machinePlayer.getCardsPlayer()) {
                if (validCardToPlay(card)) {
                    return card;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null; // No se encontró ninguna carta válida para jugar
    }

    // Verifica si la carta puede ser jugada según las reglas del juego
    private boolean validCardToPlay(Card card) {
        Card cardOnTable = table.getCurrentCardOnTheTable();
        if (cardOnTable.getUrl().contains("skip")) {
            return false; // Si la carta en la mesa es "skip", no se puede jugar ninguna carta
        } else if (card.getColor().equals(cardOnTable.getColor()) || card.getValue().equals(cardOnTable.getValue()) || card.getColor().equals("NON_COLOR")) {
            return true; // La carta tiene el mismo color o valor que la carta en la mesa, se puede jugar
        }

        return false; // En cualquier otro caso, la carta no se puede jugar
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public void setLastPlayedCard(Card lastPlayedCard) {this.lastPlayedCard = lastPlayedCard;}

    public Card getLastPlayedCard() {return lastPlayedCard;}

    public boolean isHasPlayerPlayed() {
        return hasPlayerPlayed;
    }
}
