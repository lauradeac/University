package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.controller.LogInController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.FriendRequestValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;

import socialnetwork.repository.db.FriendRequestsDbRepository;
import socialnetwork.repository.db.MessageDbRepository;
import socialnetwork.repository.db.PrietenieDbRepository;
import socialnetwork.repository.db.UtilizatorDbRepository;
import socialnetwork.repository.file.PrieteniiFile;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.domain.Utilizator;
import socialnetwork.controller.LogInController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.db.FriendRequestsDbRepository;
import socialnetwork.repository.db.MessageDbRepository;
import socialnetwork.repository.db.PrietenieDbRepository;
import socialnetwork.repository.db.UtilizatorDbRepository;
import socialnetwork.repository.file.PrieteniiFile;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.repository.memory.InMemoryRepository;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/hello-view.fxml"));
        AnchorPane root=loader.load();


        Repository<Long, Utilizator> repoDb = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/ReteaSocializare",
                "postgres", "postgres", new UtilizatorValidator());

        Repository<Tuple<Long,Long>, Prietenie> repoDbPrietenii=new PrietenieDbRepository("jdbc:postgresql://localhost:5432/ReteaSocializare",
                "postgres", "postgres", new PrietenieValidator());

        Repository<Long, Message> repoDbMesage = new MessageDbRepository("jdbc:postgresql://localhost:5432/ReteaSocializare",
                "postgres", "postgres", new MessageValidator(), repoDb);
        Repository<Tuple<Long, Long>, FriendRequest> repoDbFriendRequests= new FriendRequestsDbRepository("jdbc:postgresql://localhost:5432/ReteaSocializare",
                "postgres", "postgres", new FriendRequestValidator(), repoDb);

        UtilizatorService servU=new UtilizatorService(repoDb);

        PrietenieService servP=new PrietenieService(repoDbPrietenii, repoDb);

       MessageService servM = new MessageService(repoDbPrietenii, repoDb, repoDbMesage, servU);
       FriendRequestService serv_fr=new FriendRequestService(repoDbFriendRequests,repoDbPrietenii);


       LogInController ctrl=loader.getController();

        ctrl.setService(servU,servP,serv_fr,servM);

        primaryStage.setScene(new Scene(root, 700, 500));

        primaryStage.getIcons().add(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/1024px-Instagram_icon.png"));
        primaryStage.setTitle("Log in:");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

