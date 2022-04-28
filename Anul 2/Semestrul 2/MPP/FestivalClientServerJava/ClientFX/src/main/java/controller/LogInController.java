package controller;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Angajat;
import services.FestivalException;
import services.IFestivalServices;

import java.io.IOException;

public class LogInController {
    private IFestivalServices server;

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    private Angajat ang;

    private ShowsController showsController;

    Parent mainParent;

    public void setParent(Parent p){
        mainParent=p;
    }

    public void setServer(IFestivalServices server) {
        this.server = server;
    }

    public void setShowsController (ShowsController showsController){
        this.showsController = showsController;
    }

    @FXML
    public void logIn(ActionEvent actionEvent){
        String nume = textUsername.getText();
        String passwd = textPassword.getText();
        ang = new Angajat(nume, passwd);

        try{
            server.login(ang, showsController);
            Stage stage=new Stage();
            stage.setTitle("Welcome back " + ang.getUserName() + "!");
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        showsController.logOut();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
            });

            stage.show();
            showsController.setAngajat(ang);
            showsController.initModel();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }   catch (FestivalException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Logare esuata!","Nu exista utilizator cu acest username sau parola e gresita!");
        }

    }

    @FXML
    private void initialize() {
    }

}
