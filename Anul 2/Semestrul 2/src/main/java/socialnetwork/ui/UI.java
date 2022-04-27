//package socialnetwork.ui;
//
//import socialnetwork.domain.*;
//import socialnetwork.domain.exceptions.PrietenieValidationException;
//import socialnetwork.domain.exceptions.ServiceException;
//import socialnetwork.domain.exceptions.UserValidationException;
//import socialnetwork.repository.Repository;
//import socialnetwork.service.MessageService;
//import socialnetwork.service.PrietenieService;
//import socialnetwork.service.UtilizatorService;
//import socialnetwork.service.FriendRequestService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class UI {
//    UtilizatorService serv;
//    PrietenieService serv_pr;
//    MessageService servM;
//    FriendRequestService serv_fr;
//
//    private static Long idU;
//    private static Long idM;
//
//    public UI(UtilizatorService serv,PrietenieService serv_pr, MessageService servM, FriendRequestService serv_fr) {
//        this.serv=serv;
//        this.serv_pr=serv_pr;
//        this.servM=  servM;
//        this.serv_fr=serv_fr;
//        idU=serv.getLastID();
//    }
//    static void afisareUtilizatori(UtilizatorService serv){
//        for(Utilizator u:serv.getAll()){
//            System.out.println(u.getId()+" "+ u.getUserName() + " " + u.getFirstName()+" "+u.getLastName());
//        }
//        System.out.println();
//    }
//
//    static void afisarePrieteniiAll(PrietenieService servP, UtilizatorService servU){
//        for(Prietenie p:servP.getAll()){
//            System.out.println("(User1:"+servU.getUtilizator(p.getId().getLeft()).getUserName()+" & "+
//                    servU.getUtilizator(p.getId().getRight()).getUserName()+ ") "+
//                    servU.getUtilizator(p.getId().getLeft()).getFirstName()+" "+
//                    servU.getUtilizator(p.getId().getLeft()).getLastName()+" si "+
//                    servU.getUtilizator(p.getId().getRight()).getFirstName()+" "+
//                    servU.getUtilizator(p.getId().getRight()).getLastName()+" sunt prieteni"
//                    +" din "+p.getDate()) ;
//        }
//        System.out.println();
//    }
//
//    static void afisarePrieteni(Scanner myObj, PrietenieService servP, UtilizatorService servU) {
//        System.out.print("Username utilizator: ");
//        String userName = myObj.next();
//        Long id=servU.getUtilizatorByUserName(userName).getId();
//        if(servU.getUser(id)==null){
//            System.out.println("Acest utilizator nu exista!");
//        }
//        else{
//            for (UserFriendshipDTO p : servP.userFriendships(userName)) {
//
//                System.out.println(p);
//            }
//            System.out.println();}
//    }
//
//    static void afiseazaMesajePentruUtilizator(Scanner myObj, MessageService servM, UtilizatorService servU){
//        System.out.print("Username utilizator: ");
//        String userName = myObj.next();
//        if(servU.getUtilizatorByUserName(userName)==null){
//            System.out.println("Acest utilizator nu exista!");
//        }
//        else{
//            Iterable<Message> messages = servM.getAllMessagesForAUser(userName);
//            if (messages.equals(0)){
//                System.out.println("Acest utilizator nu are mesaje!");
//            }
//            for (Message m : messages){
//                System.out.println(m);
//            }
//        }
//    }
//
//    static void afiseazaToateMesajele(Scanner myObj, MessageService servM){
//        Iterable<Message> messages = servM.getAllMessages();
//        if (messages.equals(0)) {
//            System.out.println("Nu exista niciun mesaj!");
//        }
//        else {
//            for (Message m : messages){
//                System.out.println(m);
//            }
//        }
//    }
//
//    static void addMessageUI(Scanner myObj, MessageService servM, UtilizatorService servU) {
//
//        System.out.print("FROM utilizator : ");
//        String userName = myObj.next();
//        Long fromId = servU.getUtilizatorByUserName(userName).getId();
//        if (servU.getUtilizator(fromId) == null){
//            System.out.println("Nu exista acest utilizator!");
//        }
//
//        System.out.print("TO: ");
//        List<Utilizator> to = new ArrayList<>();
//        String toUserName = myObj.next();
//        Utilizator toUser;
//        while (!toUserName.equals("end")) {
//            if(servU.existUtilizator(toUserName)) {
//                toUser = servU.getUtilizatorByUserName(toUserName);
//                to.add(toUser);
//            }
//            toUserName = myObj.nextLine();
//        }
//
//        LocalDateTime date = LocalDateTime.now();
//        System.out.println("Textul mesajului de trimis: ");
//        String text = myObj.nextLine();
//
//        System.out.println("Raspunde la mesajul (0 daca mesajul nu e reply): ");
//        Long originalMessage = Long.parseLong(myObj.next());
//        Message message1 = new Message(servU.getUtilizator(fromId), date, to, text);
//        message1.setOriginalMessage(originalMessage);
//        servM.addMessage(message1);
//        System.out.println("Mesajul a fost adaugat cu succes!");
//
//    }
//
//    static void replyMessageUI(Scanner myObj, MessageService servM, UtilizatorService serv){
//
//        System.out.println("Log-in cu username-ul dumneavoastra: ");
//        String userName = myObj.nextLine();
//
//        if(serv.existUtilizator(userName)) {
//            System.out.println("ID MESAJ INITIAL la care faceti reply: ");
//            Long idMesaj = Long.parseLong(myObj.nextLine());
//            if (servM.getMessage(idMesaj) != null) {
//                System.out.println("Introduceti reply-ul: ");
//                String text = myObj.nextLine();
//                servM.replyToMessage(idMesaj, userName, text);
//            } else System.out.println("Nu exista mesajul la care incercati sa faceti reply!!");
//        }
//        else System.out.println("Utilizator inexistent!");
//    }
////
////    static void replyToAllUI(Scanner myObj, MessageService servM, UtilizatorService serv){
////
////        System.out.println("Log-in cu username-ul dumneavoastra: ");
////        String userName = myObj.nextLine();
////
////        if(serv.existUtilizator(userName)) {
////            System.out.println("ID MESAJ INITIAL la care faceti reply: ");
////            Long idMesaj = Long.parseLong(myObj.nextLine());
////            if (servM.getMessage(idMesaj) != null) {
////                System.out.println("Introduceti reply-ul: ");
////                String text = myObj.nextLine();
////                servM.replyToAll(idMesaj, userName, text);
////            } else System.out.println("Nu exista mesajul la care incercati sa faceti reply!!");
////        }
////        else System.out.println("Utilizator inexistent!");
////
////
////    }
//
//    static void getConversationsUI(Scanner myObj, MessageService servM, UtilizatorService serv) {
//        System.out.println("UserName1: ");
//        String user1 = myObj.next();
//        System.out.println("UserName2: ");
//        String user2 = myObj.next();
//        Iterable<Message> list = servM.getConversations(user1, user2);
//        for (Message m :list){
//            System.out.print(m.getFrom().getFirstName() + " " + m.getFrom().getLastName() + ": ");
//            System.out.println(m.getMessage());
//        }
//    }
//
//    static void addConversationUI(Scanner myObj, MessageService servM, UtilizatorService serv){
//        System.out.println("Adaugati o conversatie: ");
//        System.out.println("User 1: ");
//        String user1 = myObj.next();
//        Long id1 = serv.getUtilizatorByUserName(user1).getId();
//        System.out.println("User2: ");
//        String user2 = myObj.next();
//        Long id2 = serv.getUtilizatorByUserName(user2).getId();
//        Utilizator us1 = serv.getUtilizator(id1);
//        Utilizator us2 = serv.getUtilizator(id2);
//        System.out.println(us1.getFirstName() + " " + us1.getLastName() + ": ");
//        String text1 = myObj.nextLine();
//        List<Utilizator> toUser1 = new ArrayList<>();
//        toUser1.add(us1);
//        List<Utilizator> toUser2 = new ArrayList<>();
//        toUser2.add(us2);
//        Message messageFromUser1 = new Message(us1, LocalDateTime.now(), toUser2, text1);
//        messageFromUser1.setOriginalMessage(0L);
//        servM.addMessage(messageFromUser1);
//        Message messageFromUser2 = null;
//        String text2;
//        do {
//            System.out.println(us2.getFirstName() + " " + us2.getLastName() + ": ");
//            text2 = myObj.nextLine();
//            messageFromUser2 = new Message(us2, LocalDateTime.now(), toUser1, text2);
//            //messageFromUser2.setOriginalMessage(messageFromUser1.getId());
//            messageFromUser2.setOriginalMessage(id1);
//            servM.addMessage(messageFromUser2);
//            System.out.println(us1.getFirstName() + " " + us1.getLastName() + ": ");
//            text1 = myObj.nextLine();
//            messageFromUser1 = new Message(us1, LocalDateTime.now(), toUser2, text1);
//            //messageFromUser1.setOriginalMessage(messageFromUser2.getId());
//            messageFromUser1.setOriginalMessage(id2);
//            servM.addMessage(messageFromUser1);
//        }
//        while (!text1.equals("bye"));
//
//        System.out.println("END OF CONVERSATION");
//
//    }
//
//    static void replyToAllUI(Scanner myObj, MessageService servM, UtilizatorService serv){
//
//        System.out.println("Log-in cu username-ul dumneavoastra: ");
//        String userName = myObj.nextLine();
//
//        if(serv.existUtilizator(userName)) {
//            System.out.println("ID MESAJ INITIAL la care faceti reply: ");
//            Long idMesaj = Long.parseLong(myObj.nextLine());
//            if (servM.getMessage(idMesaj) != null) {
//                System.out.println("Introduceti reply-ul: ");
//                String text = myObj.nextLine();
//                servM.replyToAll(idMesaj, userName, text);
//            } else System.out.println("Nu exista mesajul la care incercati sa faceti reply!!");
//        }
//        else System.out.println("Utilizator inexistent!");
//
//
//    }
//
//    static void optiuni(){
//        System.out.println("1-------->ADD utilizator");
//        System.out.println("2-------->DELETE utilizator");
//        System.out.println("3-------->PRINT toti utilizatorii");
//
//        System.out.println("4-------->ADD prietenie");
//        System.out.println("5-------->REMOVE prietenie");
//        System.out.println("6-------->PRINT toate prieteniile");
//        System.out.println("7-------->PRINT prietenii unui utilizator (LAMBDA)");
//
//
//        System.out.println("8-------->Numar de comunitati");
//        System.out.println("9-------->Cea mai sociabila comunitate");
//
//        System.out.println("10------->UPDATE utilizator");
//        System.out.println("11------->UPDATE o prietenie");
//        System.out.println("12------->CAUTA un utilizator");
//        System.out.println("13------->CAUTA o prietenie");
//        System.out.println("14------->PRINT prieteniile unui utilizator dintr-o luna data (LAMBDA)");
//
//        System.out.println("15------->PRINT toate mesajele pentru Utilizator");
//        System.out.println("16------->PRINT toate mesajele");
//        System.out.println("17------->SEND mesaj");
//        System.out.println("18------->REPLY mesaj");
//        System.out.println("19------->PRINT conversatie a doi Useri");
//        System.out.println("20------->ADD conversatie");
//
//        System.out.println("21------->Trimite o cerere de prietenie");
//        System.out.println("22------->Raspunde la o cerere de prietenie");
//
//        System.out.println("23------->REPLY TO ALL");
//        System.out.println("24------->PRINT all requests for a user");
//        System.out.println("25------->PRINT all requests");
//
//        System.out.println("0.Exit");
//    }
//    static void addUtilizatorUI(Scanner myObj,UtilizatorService serv){
//
//        try {
//            System.out.println("Username: ");
//            String username = myObj.nextLine();
//            System.out.print("Nume: ");
//            String nume = myObj.nextLine();
//            System.out.print("Prenume: ");
//            String prenume = myObj.nextLine();
//            Utilizator u1 = new Utilizator(username, nume, prenume);
//            idU++;
//            u1.setId(idU);
//            //serv.addUtilizator(u1);
//            if (serv.existUtilizator(username)){
//                System.out.println("Acest utilizator exista deja!\n");
//            }
//            else {
//                serv.addUtilizator(u1);
//                System.out.println("Utilizator adaugat! \n");
//            }
//            //System.out.println("Utilizator adaugat! \n");
//        }catch(ServiceException e) {System.out.println(e.getMessage()+"\n");}
//    }
//
//
//    static void addPrietenieUI(Scanner myObj,PrietenieService serv_pr){
//        try {
//            System.out.print("Username1: ");String user1=myObj.next();
//            System.out.print("Username2: ");String user2=myObj.next();
//            System.out.print("Anul: ");int an=myObj.nextInt();
//            System.out.print("Luna: ");int luna=myObj.nextInt();
//            System.out.print("Ziua: ");int zi=myObj.nextInt();
//            Prietenie prietenieNoua = serv_pr.createPrietenie(user1, user2, an, luna, zi);
//            serv_pr.addPrieten(prietenieNoua.getId().getLeft(), prietenieNoua.getId().getRight(), an, luna, zi);
//            System.out.println("Prietenie adaugata! \n");
//        }catch(ServiceException e) {System.out.println(e.getMessage() +"\n");}
//
//
//    }
//    static void removeUserUI(Scanner myObj,UtilizatorService serv,Repository<Tuple<Long, Long>, Prietenie> repoP){
//        System.out.print("UserName: ");
//        String userName = myObj.nextLine();
//        try{
//            serv.removeUtilizator(userName,repoP);
//            System.out.println("Utilizator sters! \n");}
//        catch(ServiceException e){
//            System.out.println( e.getMessage());
//        }
//
//    }
//
//    static void updateUserUI(Scanner myObj,UtilizatorService serv){
//        System.out.print("UserName: ");
//        String userName = myObj.nextLine();
//        myObj.nextLine();
//
//
//        Utilizator user = serv.getUtilizatorByUserName(userName);
//        if(user!=null) {
//            System.out.println("Se poate face modificarea...");
//
//            System.out.print("UserName (nou sau vechi) : ");
//            String user_name = myObj.nextLine();
//            System.out.print("Nume (nou sau vechi) : ");
//            String nume = myObj.nextLine();
//            System.out.print("Prenume (nou sau vechi) : ");
//            String prenume = myObj.nextLine();
//            user.setUserName(user_name);
//            user.setFirstName(nume);
//            user.setLastName(prenume);
//
//            try {
//                serv.getRepo().update(user);
//                System.out.println("Utilizator modificat \n");
//            } catch (UserValidationException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        else System.out.println("Nu se poate face modifc pt ca utilizatorul nu exista!");
//
//
//    }
//
//    static void updatePrietenieUI(Scanner myObj, PrietenieService servP){
//        System.out.print("ID1: ");
//        Long id1 = myObj.nextLong();
//        myObj.nextLine();
//
//        System.out.print("ID2: ");
//        Long id2 = myObj.nextLong();
//        myObj.nextLine();
//
//        Prietenie p= servP.getPrietenie(id1, id2);
//        if(p!=null)
//        {
//            System.out.println("Se poate face modificarea...");
//
//            System.out.print("Noua data: ");
//            String date=myObj.nextLine();
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//            LocalDate localDate = LocalDate.parse(date, formatter);
//
//            Prietenie pr=servP.getPrietenie(id1, id2);
//            pr.setDate(localDate);
//
//            try {
//                servP.getRepo().update(pr);
//                System.out.println("Prietenie modificata \n");
//            } catch (PrietenieValidationException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        else System.out.println("Nu se poate face modifc pt ca utilizatorul nu exista!");
//
//
//    }
//    static void removePrietenieUI(Scanner myObj,PrietenieService serv_pr) {
//
//        System.out.print("Username1: ");
//        String user1 = myObj.next();
//        System.out.print("Username2: ");
//        String user2 = myObj.next();
//        try {
//            Prietenie prSters = serv_pr.createPrietenie(user1, user2);
//            serv_pr.removePrieten(prSters.getId().getLeft(), prSters.getId().getRight());
//            System.out.println("Relatie de prietenie stearsa \n");
//        }
//        catch(ServiceException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    static void afisarePrieteniLambdaLunaData(Scanner myObj, PrietenieService servP, UtilizatorService servU){
//        System.out.print("Username Utilizator: ");
//        String userName = myObj.nextLine();
//        Long id= servU.getUtilizatorByUserName(userName).getId();
//        System.out.println("Luna in care s-a legat prietenia: ");
//        try {
//            if(servU.getUser(id)==null){
//                System.out.println("Acest utilizator nu exista!");
//            }
//            else{
//                Month month = Month.of(Integer.parseInt(myObj.nextLine()));
//                List<UserFriendshipDTO> list = servP.userFriendshipsMonth(id, month);
//                for (UserFriendshipDTO userFriendshipDTO : list) {
//                    System.out.println(userFriendshipDTO);
//                }
//                System.out.println();
//            }
//        }
//        catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//
//    }
//
//    public void sendFriendRequest(Scanner myObj, FriendRequestService serv, UtilizatorService servU, PrietenieService serv_pr){
//        System.out.print("Log-in cu username-ul dumneavoastra: ");
//        String user1 =  myObj.nextLine();
//        System.out.print("Username-ul utilizatorului cu care vreti sa va imprieteniti: ");
//        String user2 = myObj.nextLine();
//        System.out.print("Anul: ");int an=myObj.nextInt();
//        System.out.print("Luna: ");int luna=myObj.nextInt();
//        System.out.print("Ziua: ");int zi=myObj.nextInt();
//        if(!(servU.existUtilizator(user1) || servU.existUtilizator(user2))){
//            System.out.println("Acesti utilizatori nu exista!");
//        }
//        else {
//            int ok = 1;
//            Long id1 = servU.getUtilizatorByUserName(user1).getId();
//            Long id2 = servU.getUtilizatorByUserName(user2).getId();
//            if (serv_pr.suntPrieteni(id1, id2))
//                ok = 0;
//            if (ok == 1) {
//                FriendRequest fr = new FriendRequest(servU.getUser(id1), servU.getUser(id2),an,luna,zi);
//                if(serv.getFriendRequest(fr.getId())!=null&&serv.getFriendRequest(fr.getId()).getStatus().equals(Status.PENDING)){
//                    System.out.println("Cererea de prietenie e deja in asteptare!\n");
//                }
//                else {
//                    serv.addFriendRequest(fr);
//                    System.out.println("Cerere de prietenie trimisa!");
//                }
//            } else
//                System.out.println("Sunteti deja prieten cu acest utilizator");
//        }
//
//    }
//
//    public void respondToFriendRequest(Scanner myObj, FriendRequestService serv,UtilizatorService servU){
//        System.out.print("Log-in cu username-ul dumneavoastra: ");
//        String user1 = myObj.nextLine();
//        if(!servU.existUtilizator(user1))
//            System.out.println("Acest utilizator nu exista!");
//        else {
//            int nr = 0;
//            Long id1 = servU.getUtilizatorByUserName(user1).getId();
//            List<FriendRequest> cererile_acestui_utilizator = new ArrayList<>();
//            for (FriendRequest f : serv.getAll()) {
//                if (f.getUser2().getId() == id1 && f.getStatus().equals(Status.PENDING))
//                    cererile_acestui_utilizator.add(f);
//
//            }
//            System.out.println("");
//            if (cererile_acestui_utilizator.size() != 0) {
//                System.out.println("Cererile dumneavoastra care sunt PENDING: ");
//                System.out.println();
//                for (FriendRequest f : cererile_acestui_utilizator) {
//                    System.out.println(f.getUser1().getId() + " | " + f.getUser1().getFirstName() + " | " +
//                            f.getUser1().getUserName() + " | " + f.getDate() + " | " + f.getStatus().toString());
//                }
//
//
//                System.out.print("Username-ul utilizatorului care v-a trimis cererea de prietenie: ");
//                String user2 = myObj.nextLine();
//                if (!servU.existUtilizator(user2)) {
//                    System.out.println("Acest utilizator nu exista!");
//                } else {
//                    Long id2 = servU.getUtilizatorByUserName(user2).getId();
//                    System.out.println("Pentru a accepta cererea, tastati 1");
//                    System.out.println("Pentru a refuza cererea, tastati 0");
//                    int ok = myObj.nextInt();
//                    FriendRequest fr = new FriendRequest(servU.getUser(id1), servU.getUser(id2), LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
//                    if (ok == 0) {
//                        try {
//                            serv.respond(fr, Status.REJECTED.name());
//                            System.out.println("Am respins prietenia!");
//                        } catch (ServiceException e) {
//                            System.out.println(e.getMessage());
//                        }
//                    }
//                    if (ok == 1) {
//                        try {
//                            serv.respond(fr, Status.APPROVED.name());
//                            System.out.println("Am acceptat prietenia!");
//                        } catch (ServiceException e) {
//                            System.out.println(e.getMessage());
//                        }
//                    }
//                }
//            } else System.out.println("Nu aveti cereri de prietenie PENDING!"); //
//        }
//    }
//
//    static void printAllRequestsUI(Scanner myObj, UtilizatorService serv, FriendRequestService serv_fr) {
//        for(FriendRequest fr:serv_fr.getAll()){
//            System.out.println(fr.getUser1().getUserName()+" "+ fr.getUser2().getUserName() + " " + fr.getStatus()+" "+fr.getDate());
//        }
//        System.out.println();
//    }
//
////    static void printAllRequestsForAUserUI(Scanner myObj, UtilizatorService serv, FriendRequestService serv_fr) {
////        System.out.print("Log-in cu username-ul dumneavoastra: ");
////        String user1 = myObj.nextLine();
////        if(!serv.existUtilizator(user1))
////            System.out.println("Acest utilizator nu exista!");
////        else {
////            Long id = serv.getUtilizatorByUserName(user1).getId();
////            List<FriendRequestDTO> list = serv_fr.getAllRequestsForUser(id);
////            list.forEach(System.out::println);
////        }
////    }
//
//    public void start() {
//
//        while(true){
//            optiuni();
//            Scanner myObj = new Scanner(System.in);
//            System.out.print("Introduceti optiunea: ");//spatiu doar intre nr complex si operator
//            int opt = Integer.parseInt(myObj.nextLine());
//            switch(opt){
//                case 1:
//                    addUtilizatorUI(myObj,serv);
//                    break;
//                case 2:
//                    removeUserUI(myObj,serv,serv_pr.getRepo());
//                    break;
//                case 3:
//                    afisareUtilizatori(serv);
//                    break;
//                case 4:
//                    addPrietenieUI(myObj,serv_pr);
//                    break;
//                case 5:
//                    removePrietenieUI(myObj,serv_pr);
//                    break;
//                case 6:
//                    afisarePrieteniiAll(serv_pr,serv);
//                    break;
//                case 7:
//                    afisarePrieteni(myObj,serv_pr,serv);
//                    break;
//                case 8:
//                    System.out.println("Numar comunitati: "+serv.NumarComunitati()+"\n");
//                    break;
//                case 9:
//                    System.out.println("Cea mai sociabila comunitate: ");
//                    int maxim=serv.ComunitateSociabila()-1;
//
//                    System.out.println("de lungime:"+maxim);
//                    if(maxim==0)
//                        System.out.println("Nu exista inca prietenii!! \n");
//
//                    break;
//                case 10:
//                    updateUserUI(myObj,serv);
//                    break;
//
//                case 11:
//                    updatePrietenieUI(myObj, serv_pr);
//                    break;
//
//                case 12:
//                    System.out.print("Introduceti username-ul de cautare:");
//                    String userName = myObj.next();
//                    //Long id = myObj.nextLong();
//                    //Utilizator u=serv.getRepo().findOne(id);
//                    Utilizator u=serv.getUtilizatorByUserName(userName);
//                    if(u!=null)
//                        System.out.println(u.getId()+"  "+ u.getUserName()+ " "+ u.getFirstName()+"  "+u.getLastName());
//                    else
//                        System.out.println("Nu s-a gasit acest utilizator!");
//                    break;
//
//                case 13:
//                    System.out.println("Username1: ");
//                    String user1 = myObj.next();
//                    System.out.println("Username2: ");
//                    String user2 = myObj.next();
//                    Long id1 = serv.getUtilizatorByUserName(user1).getId();
//                    Long id2 = serv.getUtilizatorByUserName(user2).getId();
//                    Tuple pp = new Tuple((long)id1, (long)id2);
//                    Prietenie p=serv_pr.getRepo().findOne(pp);
//                    if(p!=null)
//                        System.out.println("Utilizatorii cu userName1= "+ user1+" si userName2="+user2+"  sunt prieteni de la data de "+p.getDate().getYear()+"-"+p.getDate().getMonthValue()+"-"+p.getDate().getDayOfMonth());
//                    else
//                        System.out.println("Nu exista aceasta prietenie!");
//                    break;
//
//                case 14:
//                    afisarePrieteniLambdaLunaData(myObj, serv_pr, serv);
//                    break;
//                case 15:
//                    afiseazaMesajePentruUtilizator(myObj, servM, serv);
//                    break;
//                case 16:
//                    afiseazaToateMesajele(myObj, servM);
//                    break;
//                case 17:
//                    addMessageUI(myObj, servM, serv);
//                    break;
//                case 18:
//                    replyMessageUI(myObj, servM, serv);
//                    break;
//                case 19:
//                    getConversationsUI(myObj, servM, serv);
//                    break;
//                case 20:
//                    addConversationUI(myObj, servM, serv);
//                    break;
//                case 21:
//                    sendFriendRequest(myObj,serv_fr,serv,serv_pr);
//                    break;
//                case 22:
//                    respondToFriendRequest(myObj, serv_fr, serv);
//                    break;
//
//                case 23:
//                    replyToAllUI(myObj, servM, serv);
//                    break;
////                case 24:
////                    printAllRequestsForAUserUI(myObj, serv, serv_fr);
////                    break;
//                case 25:
//                    printAllRequestsUI(myObj, serv, serv_fr);
//                case 0:
//                    System.out.println("Aplicatie inchisa...");
//                    return;
//            }
//        }
//    }
//}
//
