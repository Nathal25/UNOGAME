package org.example.eiscuno.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.example.eiscuno.model.GameState;
import org.example.eiscuno.model.MachineTurnState;
import org.example.eiscuno.model.PlayerTurnState;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;


import java.net.URL;
import java.util.Stack;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    @FXML
    private Button ButtonExit;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GameUno gameUno;
    @FXML
    private Button ButtonUno;

    @FXML
    private Button ButtonDeck;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;


    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;
    private Stack<Card> deckOfCards = new Stack<>();

    private GameState playerTurnState;
    private GameState machineTurnState;
    private GameState currentState;

    private boolean unoButtonPressed = false;
    private PauseTransition unoTimer;

    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        addImageButtonUno();
        addImageButtonExit();
        addImageButtonDecks();
        setBackgroundImagePane(borderPane, "/org/example/eiscuno/images/background_uno.png");

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView);
        threadPlayMachine.start();
        printCardMachinePlayer();
        reStoreCards();

        // Inicializar el temporizador
        unoTimer = new PauseTransition(Duration.seconds(2));
        unoTimer.setOnFinished(event -> {
            if (!unoButtonPressed) {
                addTwoCardsToHumanPlayer();
            }
        });
    }

    /**
     *
     */
    public void reStoreCards() {
        int size = table.getCardsTable().size();
        if (size != 0) {
            Card currentCard = table.getCurrentCardOnTheTable();
            if (currentCard != null) {
                deckOfCards.add(currentCard);
                System.out.println("Carta en el tablero " + currentCard);
                newCards(deckOfCards);
            }
        }
    }

    /**
     *
     * @param deckOfCards
     */
    public void newCards(Stack<Card> deckOfCards) {
        if (deck.isEmpty()) {
            deck.setDeckOfCards(deckOfCards);
        }
    }
    private void addTwoCardsToHumanPlayer() {
        gameUno.eatCard(humanPlayer,2);
//        humanPlayer.addCard(deck.takeCard());
//        humanPlayer.addCard(deck.takeCard());
        printCardsHumanPlayer();
        setState(machineTurnState);
        startMachineTurn();
        System.out.println("No se presionó UNO a tiempo. Se agregaron dos cartas.");
    }

    /**
     *
     */
    public void forceHumanPlayerToTakeCard() {
        Platform.runLater(() -> {
            Card card = deck.takeCard();
//            humanPlayer.addCard(card);
//            System.out.println("Se añadio una carta");
            printCardsHumanPlayer();
            System.out.println("Cartas del jugador: " + humanPlayer.getCardsPlayer().size());
            System.out.println("Carta tomada: " + card);
            // Ya no cambia el turno aquí
        });
    }

    /**
     *
     */
    public void addImageButtonUno() {
        URL urlImageUno = getClass().getResource("/org/example/eiscuno/images/button_uno.png");
        Image imagenNuevo = new Image(urlImageUno.toString());
        ImageView imageView = new ImageView(imagenNuevo);
        imageView.setFitWidth(65);
        imageView.setFitHeight(65);
        ButtonUno.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ButtonUno.setGraphic(imageView);
        ButtonUno.setMinSize(80, 80);
        ButtonUno.setMaxSize(80, 80);
    }

    /**
     *
     */
    public void addImageButtonExit() {
        URL urlImageExit = getClass().getResource("/org/example/eiscuno/images/ButtonExit.png");
        Image imagenNuevo = new Image(urlImageExit.toString());
        ImageView imageView = new ImageView(imagenNuevo);
        imageView.setFitWidth(55);
        imageView.setFitHeight(55);
        ButtonExit.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ButtonExit.setGraphic(imageView);
        ButtonExit.setMinSize(50, 50);
        ButtonExit.setMaxSize(50, 50);
    }

    /**
     *
     */
    public void addImageButtonDecks() {
        URL linkNuevoExitBaraja = getClass().getResource("/org/example/eiscuno/cards-uno/deck_of_cards.png");
        Image imagenNuevo = new Image(linkNuevoExitBaraja.toString());
        ImageView imageView = new ImageView(imagenNuevo);
        imageView.setFitWidth(110);
        imageView.setFitHeight(165);
        ButtonDeck.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ButtonDeck.setGraphic(imageView);
        ButtonDeck.setMinSize(80, 80);
        ButtonDeck.setMaxSize(80, 80);
    }

    /**
     *
     * @param borderPane
     * @param imagePath
     */
    private void setBackgroundImagePane(BorderPane borderPane, String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        Background background = new Background(backgroundImage);
        borderPane.setBackground(background);
    }

    /**
     *
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;

        playerTurnState = new PlayerTurnState(this);
        machineTurnState = new MachineTurnState(this);
        currentState = playerTurnState; // El juego comienza con el turno del jugador
    }

    /**
     *
     * @param state
     */
    public void setState(GameState state) {
        this.currentState = state;
    }

    /**
     *
     * @return
     */
    public GameState getPlayerTurnState() {
        return playerTurnState;
    }

    /**
     *
     * @return
     */
    public GameState getMachineTurnState() {
        return machineTurnState;
    }

    /**
     *
     * @param card
     */
    public void humanPlayCard(Card card) {
        gameUno.playCard(card);
        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
        printCardsHumanPlayer();
        setState(machineTurnState);
        startMachineTurn();
    }

    /**
     *
     */
    public void drawCard() {
        Card card = deck.takeCard();
        humanPlayer.addCard(card);
        printCardsHumanPlayer();
        // Detener el temporizador si está corriendo
        if (unoTimer.getStatus() == PauseTransition.Status.RUNNING) {
            unoTimer.stop();
        }
        System.out.println("Se tomó una carta.");
        System.out.println("Cartas del jugador: " + humanPlayer.getCardsPlayer().size());
        System.out.println("Carta tomada: " + card);
        System.out.println("Turno de " + playerTurnState);
    }

    /**
     *
     */
    private void startMachineTurn() {
        new Thread(() -> {
            threadPlayMachine.setHasPlayerPlayed(true);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadPlayMachine.putCardOnTheTable();
            Platform.runLater(() -> setState(playerTurnState));
        }).start();
    }

    /**
     *
     * @param card
     */
    public void playCard(Card card) {
        currentState.playCard(card);
    }

    /**
     *
     */



    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            // Configurar el evento del clic en la imagen de la carta
            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                if (canHumanPlay()) { // Verificar si el jugador puede jugar
                    if (table.getCardsTable().isEmpty()) { // Si no hay cartas en la mesa
                        handleFirstCardPlay(card);
                    } else if (isPlayable(card, table.getCurrentCardOnTheTable())) { // Si la carta es jugable
                        handlePlayableCard(card);
                    } else {
                        System.out.println("No se puede jugar esa carta en este momento.");
                    }
                } else {
                    System.out.println("No es tu turno para jugar.");
                }
            });

            // Agregar la imagen de la carta al gridPane
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }

        // Inicia el temporizador si al jugador le quedan dos cartas
        if (humanPlayer.getCardsPlayer().size() == 1) {
            startUnoTimer();
        }
    }
    /**
     * Maneja el evento cuando el jugador juega la primera carta en el juego.
     *
     * @param card la carta que el jugador quiere jugar
     */
    private void handleFirstCardPlay(Card card) {
        gameUno.playCard(card);
        tableImageView.setImage(card.getImage());
        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
        printCardsHumanPlayer();
        if (card.getValue().equals("+4") || card.getValue().equals("+2") || card.getValue().equals("SKIP")) {
            threadPlayMachine.setHasPlayerPlayed(false);
        } else {
            threadPlayMachine.setHasPlayerPlayed(true);
        }
        System.out.println("Cartas de la máquina: " + machinePlayer.getCardsPlayer().size());
        System.out.println("Cartas del jugador: " + humanPlayer.getCardsPlayer().size());
        printCardMachinePlayer();
    }

    /**
     * Handles the moment when the human player plays a card that follows the rules of the game
     *
     * @param card la carta que el jugador quiere jugar
     */
    private void handlePlayableCard(Card card) {
        gameUno.playCard(card);
        tableImageView.setImage(card.getImage());
        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
        if (card.getValue().equals("+4") || card.getValue().equals("+2") || card.getValue().equals("SKIP")) {
            threadPlayMachine.setHasPlayerPlayed(false);
        } else {
            threadPlayMachine.setHasPlayerPlayed(true);
        }
        printCardsHumanPlayer();
        System.out.println("Cartas de la máquina: " + machinePlayer.getCardsPlayer().size());
        System.out.println("Cartas del jugador: " + humanPlayer.getCardsPlayer().size());
        setState(machineTurnState); // Cambiar el turno aquí después de que se juegue la carta
        startMachineTurn();
    }

    /**
     * Verifica si el jugador puede jugar en este momento.
     *
     * @return true si es el turno del jugador y puede jugar; false en caso contrario
     */
    private boolean canHumanPlay() {
        return currentState == playerTurnState;
    }

    /**
     * Verifica si una carta es jugable según las reglas del juego.
     *
     * @param cardToBePlayed la carta que el jugador quiere jugar
     * @param cardOnTable    la carta actualmente en la mesa
     * @return true si la carta es jugable; false en caso contrario
     */
    private boolean isPlayable(Card cardToBePlayed, Card cardOnTable) {
        return (cardToBePlayed.getValue().equals(cardOnTable.getValue()) || cardToBePlayed.getColor().equals(cardOnTable.getColor()) || cardToBePlayed.getColor().equals("NON_COLOR") || cardOnTable.getColor().equals("NON_COLOR"));
    }

    /**
     * Encuentra la posición de una carta en la mano del jugador humano.
     *
     * @param card la carta de la cual se quiere encontrar la posición
     * @return la posición de la carta en la mano del jugador, o -1 si no se encuentra
     */
    private int findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }
    /**
     *
     */
    private void printCardMachinePlayer() {
        this.gridPaneCardsMachine.getChildren().clear();
        Card[] currentVisibleCardsMachinePlayer = this.gameUno.getCurrentVisibleCardsMachinePlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++) {
            ImageView newCardImageView = new ImageView(new Image(getClass().getResourceAsStream("/org/example/eiscuno/cards-uno/card_uno.png")));
            newCardImageView.setFitHeight(90);
            newCardImageView.setFitWidth(70);
            this.gridPaneCardsMachine.add(newCardImageView, i, 0);
        }
        reStoreCards();
    }
    /**
     * Handles the event to take cards from the Deck
     * @param event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        drawCard();
    }

    /**
     * Handles the event when the human player presses the button
     *
     * @param event el evento de acción
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        if (humanPlayer.getCardsPlayer().size() == 2) {
            unoButtonPressed = true;
            System.out.println("UNO presionado a tiempo.");
            // Detener el temporizador si está corriendo
            if (unoTimer.getStatus() == PauseTransition.Status.RUNNING) {
                unoTimer.stop();
            }
        }
        pressUnoButton();
    }

    /**
     * Method called when the UNO button is pressed
     */
    public void pressUnoButton() {
        humanPressUnoButton();
        currentState.pressUnoButton();
    }

    /**
     * Marca que el jugador humano ha presionado el botón UNO
     */
    public void humanPressUnoButton() {
        unoButtonPressed = true;
    }

    /**
     * Inicia el temporizador para presionar el botón UNO
     */
    public void startUnoTimer() {
        unoButtonPressed = false;
        unoTimer.playFromStart();
    }
    /**
     *  Closes the game when the button Exit is clicked
     * @param event
     */
    @FXML
    void onHandleButtonCloseGame(ActionEvent event) {
        GameUnoStage.deleteInstance();
        threadSingUNOMachine.stop();
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }
}