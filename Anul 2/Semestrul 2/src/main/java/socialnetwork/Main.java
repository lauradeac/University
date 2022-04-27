package socialnetwork;

import socialnetwork.domain.Utilizator;
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


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Main {


    public static void main(String[] args) {


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

        servM.getAllMessages().forEach(x->System.out.println(x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

//        UI ui=new UI(servU,servP, servM,serv_fr);
//        ui.start();

    }
}


