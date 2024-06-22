package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;


public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private volatile boolean unoAnnounced;

    //private volatile boolean twoAnnounced;
    private volatile boolean running;
    private GameUnoController controller; // Referencia al controlador



    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, GameUnoController controller) {
        this.cardsPlayer = cardsPlayer;
        this.unoAnnounced = false;
        this.running = true; // Variable para controlar la ejecución del hilo
        this.controller = controller; // Asignar la referencia al controlador
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                break;
            }
            hasOneCardTheHumanPlayer();
        }
    }

    private void hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1 && !unoAnnounced) {
            System.out.println("UNO PARA USUARIO");
            unoAnnounced = true;
            controller.forceHumanPlayerToTakeCard();
        } else if (cardsPlayer.size() != 1 && unoAnnounced) {
            unoAnnounced = false;
        }
    }

    /* private void machinePlayerToTakeCard(){
        if (controller.getMachinePlayer().getCardsPlayer().size() == 1 && !twoAnnounced) {
            System.out.println("UNO PARA MAQUINA");
            twoAnnounced = true; // Marcar que el anuncio ya se hizo
            controller.machinePlayerToTakeCard();
        } else if (controller.getMachinePlayer().getCardsPlayer().size() != 1 && twoAnnounced) {
            twoAnnounced = false; // Reiniciar la bandera si el jugador no tiene exactamente una carta
        }
    }

    */

    public void stop() {
        running = false; // Detener la ejecución del hilo
    }
}