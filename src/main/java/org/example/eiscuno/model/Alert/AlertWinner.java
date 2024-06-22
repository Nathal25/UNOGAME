package org.example.eiscuno.model.Alert;

import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

public class AlertWinner implements IAlertWinner {
    @Override
    public void showMessageWinner(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setGraphic(new ImageView(String.valueOf(getClass().getResource("/org/example/eiscuno/images/winnerImg.jpg"))));
        alert.showAndWait();
    }
}
