package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;

    }

    public void run() {
        while (true){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }
    //Verificar que la carta a poner sigue las reglas del juego
    private void putCardOnTheTable(){
        int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        Card card = machinePlayer.getCard(index);
        Card carOnTable=table.getCurrentCardOnTheTable();
        if(!carOnTable.getUrl().contains("cards-uno/skip")){
            if(card.getValue().equals(carOnTable.getValue())||card.getColor().equals(carOnTable.getColor())){
                System.out.println("Son iguales");
            }
        } else {
            System.out.println("Es saltar turno");
        }
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
        machinePlayer.getCardsPlayer().remove(index);
        System.out.println("Se añadió "+tableImageView.getImage());
    }


    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public boolean isHasPlayerPlayed() {
        return hasPlayerPlayed;
    }
}
