package socialnetwork.repository.db;


import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;
    private Repository<Long, Utilizator> repoU;


    /**
     * Constructorul clasei
     * @param url - db postgres
     * @param username - db postgres
     * @param password - db postgres
     * @param validator - validatorul entitatii
     * @param repoU - Repo-ul de utilizatori
     */
    public MessageDbRepository(String url, String username, String password, Validator<Message> validator, Repository<Long, Utilizator> repoU) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.repoU = repoU;
    }


    /**
     * @param aLong - id-ul dupa care se cauta entitatea
     * @return - entitatea
     */
    @Override
    public Message findOne(Long aLong) {
        Message message = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String sql = "select * from message where id_m = ?";
        try (Connection connection = DriverManager.getConnection(url, username,password);
             PreparedStatement ps =connection.prepareStatement(sql)){
            ps.setLong(1, aLong);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Long idM = resultSet.getLong("id_m");
                Long idFrom = resultSet.getLong("from");
                Date date = resultSet.getDate("date");
                String text = resultSet.getString("text");
                Long originalM = resultSet.getLong("original_message");
                Utilizator from = repoU.findOne(idFrom);
                List<Utilizator> to = new ArrayList<>();

                String sql2 = "select * from message\n" +
                        "    left join receivers as r on message.id_m = r.id_message\n" +
                        "    left join utilizatori as u on u.id = r.id_receiver\n" +
                        " where  message.id_m=?";
                PreparedStatement statement1 = connection.prepareStatement(sql2);

                    statement1.setLong(1, idM);
                    ResultSet resultSet1 = statement1.executeQuery();
                    String userNameReceiver;
                    String firstNameReceiver;
                    String lastNameReceiver;
                    while(resultSet1.next()) {
                        Long idTo = resultSet1.getLong("id_receiver");
                        userNameReceiver = resultSet1.getString("user_name");
                        firstNameReceiver = resultSet1.getString("first_name");
                        lastNameReceiver = resultSet1.getString("last_name");
                        Utilizator receiver = new Utilizator(userNameReceiver, firstNameReceiver, lastNameReceiver);
                        receiver.setId(idTo);
                        to.add(receiver);
                    }

                message = new Message(from, convertToLocalDateTime(date), to, text);
                message.setOriginalMessage(originalM);
                message.setId(idM);
                ps.execute();

            }

            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }


    /**
     * @return toate entitatile de tip Message
     */
    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * from message m inner join utilizatori as u on u.id = m.from");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_m");
                Long idFrom = resultSet.getLong("from");
                Timestamp date = resultSet.getTimestamp("date");

                String text = resultSet.getString("text");
                String userName = resultSet.getString("user_name");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Long originalMessage =resultSet.getLong("original_message");

                List<Utilizator> to = new ArrayList<>();
                try (Connection connection1 = DriverManager.getConnection(url, username, password);
                     PreparedStatement statement1 = connection1.prepareStatement(
                             "select * from message\n" +
                                     "    left join receivers as r on message.id_m = r.id_message\n" +
                                     "    left join utilizatori as u on u.id = r.id_receiver\n" +
                                     " where  message.id_m=?"))
                {
                    statement1.setLong(1, id);
                    ResultSet resultSet1 = statement1.executeQuery();
                    String userNameReceiver;
                    String firstNameReceiver;
                    String lastNameReceiver;
                    while(resultSet1.next()) {
                        Long idTo = resultSet1.getLong("id_receiver");
                        userNameReceiver =resultSet1.getString("user_name");
                        firstNameReceiver = resultSet1.getString("first_name");
                        lastNameReceiver = resultSet1.getString("last_name");
                        Utilizator receiver = new Utilizator(userNameReceiver, firstNameReceiver, lastNameReceiver);
                        receiver.setId(idTo);
                        to.add(receiver);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Utilizator from = new Utilizator(userName, firstName, lastName);
                from.setId(idFrom);
                Message message = new Message(from, date.toLocalDateTime(), to, text);
                //System.out.println(convertToLocalDateTime(date));
                message.setOriginalMessage(originalMessage);
                message.setId(id);
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }


    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }


    /**
     * metoda ce salveaza entitatile in repo
     * @param entity entity must be not null
     * @return entitatea daca se salveaza, altfel null
     */
    @Override
    public Message save(Message entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        Long id_message = null;
        int row_count=0;
        String sql = "insert into message (\"from\", date, text, original_message) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            //ps.setLong(1, entity.getId());
            ps.setLong(1, entity.getFrom().getId());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            //ps.setDate(2, Date.valueOf(entity.getDate()));
            ps.setString(3, entity.getMessage());

            if(entity.getOriginalMessage()!=null)
            ps.setLong(4, entity.getOriginalMessage());
            else ps.setLong(4,0);
            row_count=ps.executeUpdate();



            String sql1 = "select max(id_m) from message";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            ResultSet resultSet1 = ps1.executeQuery();
            while(resultSet1.next()) {
                id_message = resultSet1.getLong("max");
            }

            ps1.execute();

            String sql2 = "insert into receivers (id_message, id_receiver) values (?, ?)";
            //Connection connection1 = DriverManager.getConnection(url, username, password);
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            for (Utilizator user : entity.getTo() ){
                ps2.setLong(1, id_message);
                ps2.setLong(2, user.getId());
               ps2.execute();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(row_count!=0)
            return entity;
        return null;

    }

    @Override
    public Message delete(Long aLong) {
        return null;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}