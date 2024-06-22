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
    private volatile boolean running;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
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

    public void stopRunning() {
        this.running = false;
    }

    public void putCardOnTheTable() {
        Card card = getValidCardToPlay();

        if (card != null) {
            table.addCardOnTheTable(card);
            tableImageView.setImage(card.getImage());
            machinePlayer.getCardsPlayer().remove(card);
            System.out.println("Se añadió " + tableImageView.getImage());
            setLastPlayedCard(card);
        } else {
            Card drawnCard = new Deck().takeCard();
            if (validCardToPlay(drawnCard)) {
                table.addCardOnTheTable(drawnCard);
                tableImageView.setImage(drawnCard.getImage());
                setLastPlayedCard(drawnCard);
            } else {
                machinePlayer.addCard(drawnCard);
                setHasPlayerPlayed(false);
            }
        }
    }

    private Card getValidCardToPlay() {
        for (Card card : machinePlayer.getCardsPlayer()) {
            if (validCardToPlay(card)) {
                return card;
            }
        }
        return null;
    }

    private boolean validCardToPlay(Card card) {
        Card cardOnTable = table.getCurrentCardOnTheTable();
        if (cardOnTable.getUrl().contains("skip")) {
            return false;
        } else if (card.getColor().equals(cardOnTable.getColor()) || card.getValue().equals(cardOnTable.getValue()) || card.getColor().equals("NON_COLOR")) {
            return true;
        }
        return false;
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public void setLastPlayedCard(Card lastPlayedCard) {this.lastPlayedCard = lastPlayedCard;}

    public Card getLastPlayedCard() {return lastPlayedCard;}

}