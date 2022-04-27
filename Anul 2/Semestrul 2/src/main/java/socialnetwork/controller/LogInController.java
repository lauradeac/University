package socialnetwork.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import javafx.scene.control.Alert;

//import java.awt.*;
import java.io.IOException;

public class LogInController {
    private UtilizatorService serviceU;
    private PrietenieService serviceP;
    private FriendRequestService serviceC;
    private MessageService serviceM;

    @FXML
    private TextField textFieldId;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button buttonImg;

    @FXML
    private ImageView imageView1=new ImageView("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png");
    @FXML
    public void logIn(){

        imageView1.setVisible(true);


        if(serviceU.getUtilizatorByUserName(textFieldId.getText())==null || (!passwordField.getText().equals("pass")))
        {
            textFieldId.setText("");
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Logare esuata!","Nu exista utilizator cu acest username!");

        }
        else{


            showFriendshipsDialog();
        }
    }

    public void setService(UtilizatorService serviceU, PrietenieService serviceP, FriendRequestService serviceC, MessageService serviceM) {

        this.serviceU=serviceU;
        this.serviceP=serviceP;
        this.serviceC=serviceC;
        this.serviceM=serviceM;


    }

    @FXML
    private void initialize() {
    }

    public void showFriendshipsDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendshipsView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Welcome back "+serviceU.getUtilizatorByUserName(textFieldId.getText()).getLastName()+" "+serviceU.getUtilizatorByUserName(textFieldId.getText()).getFirstName()+"!");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendshipsController friendshipsController = loader.getController();
            friendshipsController.setFriendshipsService(serviceU, serviceP,serviceC, serviceM, textFieldId.getText().toString());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
