package practic.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }

    static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }

    static void showConfirmationWindow(Stage owner, String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(owner);
        alert.setTitle("Atentie!");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
