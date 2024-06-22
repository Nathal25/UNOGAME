package org.example.eiscuno.model.card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
class GameUnoTest extends ApplicationTest {
    @Override
    public void start(Stage stage){}
    @Test
    public void test() {
        Player humanPlayer = new Player("HUMAN_PLAYER");
        Player machinePlayer = new Player("MACHINE_PLAYER");
        Deck deck = new Deck();
        Table table = new Table();
        GameUno myGameUno = new GameUno(humanPlayer, machinePlayer, deck, table);
        myGameUno.eatCard(humanPlayer, 4);
        myGameUno.eatCard(machinePlayer, 4);
        int playerInitialCards = humanPlayer.getCardsPlayer().size();
        int machineInitialCards = machinePlayer.getCardsPlayer().size();
        Assertions.assertEquals(playerInitialCards, machineInitialCards);
    }
}
