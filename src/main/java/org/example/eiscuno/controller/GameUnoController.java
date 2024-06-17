package org.example.eiscuno.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;


import java.net.URL;
import java.util.ResourceBundle;



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
    private Button ButtonSalir;

    @FXML
    private BorderPane borderPane;


    @FXML
    private Button ButtonSiguiente;

    @FXML
    private GameUno gameUno;
    @FXML
    private Button ButtonUno;

    @FXML
    private Button ButtonBaraja;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;



    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */

    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        printCardMachinePlayer();
        addImageButtonUno();
        addImageButtonExit();
        addImageButtonDecks();
        setBackgroundImagePane(borderPane, "/org/example/eiscuno/images/background_uno.png");

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView);
        threadPlayMachine.start();

    }

    //Imagen ButtonUno
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

    //Imagen ButtonExit
    public void addImageButtonExit() {
        URL urlImageExit = getClass().getResource("/org/example/eiscuno/images/ButtonExit.png");

        Image imagenNuevo = new Image(urlImageExit.toString());
        ImageView imageView = new ImageView(imagenNuevo);
        imageView.setFitWidth(45);
        imageView.setFitHeight(45);
        ButtonSalir.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ButtonSalir.setGraphic(imageView);
        ButtonSalir.setMinSize(40, 40);
        ButtonSalir.setMaxSize(40, 40);

    }

    //Imagen ButtonBaraja
    public void addImageButtonDecks() {
        URL linkNuevoExitBaraja = getClass().getResource("/org/example/eiscuno/cards-uno/deck_of_cards.png");

        Image imagenNuevo = new Image(linkNuevoExitBaraja.toString());
        ImageView imageView = new ImageView(imagenNuevo);
        imageView.setFitWidth(110);
        imageView.setFitHeight(165);
        ButtonBaraja.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ButtonBaraja.setGraphic(imageView);
        ButtonBaraja.setMinSize(80, 80);
        ButtonBaraja.setMaxSize(80, 80);

    }

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
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    //Hacer que verifique las reglas
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                // Aqui deberian verificar si pueden en la tabla jugar esa carta
                gameUno.playCard(card);
                tableImageView.setImage(card.getImage());
                humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                threadPlayMachine.setHasPlayerPlayed(true);
                printCardsHumanPlayer();
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }
    private void printCardMachinePlayer(){
        this.gridPaneCardsMachine.getChildren().clear();
        System.out.println("MACHINE_PLAYER: " + this.machinePlayer.getCardsPlayer());
        Card[] currentVisibleCardsMachinePlayer = this.gameUno.getCurrentVisibleCardsMachinePlayer(this.posInitCardToShow);
        System.out.println("Tamaño "+currentVisibleCardsMachinePlayer.length);
        for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++) {
            Card card = currentVisibleCardsMachinePlayer[i];
            machinePlayer.removeCard(findPosCardsMachinePlayer(card));
            ImageView newCardImageView=new ImageView(new Image(getClass().getResourceAsStream("/org/example/eiscuno/cards-uno/card_uno.png")));
            newCardImageView.setFitHeight(90);
            newCardImageView.setFitWidth(70);
            this.gridPaneCardsMachine.add(newCardImageView,i,0);
        }
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    private Integer findPosCardsMachinePlayer(Card card){
        for (int i=0;i<this.machinePlayer.getCardsPlayer().size();i++){
            if (this.machinePlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
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
    @FXML
    void onHandleButtonCloseGame(ActionEvent event) {
        GameUnoStage.deleteInstance();

    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        // Implement logic to take a card here
        System.out.println("BotonBaraja");
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        System.out.println("BotonUno");
    }
}
