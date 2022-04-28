package socialnetwork.repository.db;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.Validator;

import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.sql.DriverManager;
import java.util.Set;

public class FriendRequestsDbRepository implements Repository<Tuple<Long, Long>, FriendRequest> {

    /**
     * folosite cand creez repo db
     */
    private String url;
    private String username;
    private String password;
    private Validator<FriendRequest> validator;
    private Repository<Long, Utilizator> repoU;

    /**
     * @param url       db postgres
     * @param username  db postgres
     * @param password  db postgres
     * @param validator validatorul entitatii validator
     */
    public FriendRequestsDbRepository(String url, String username, String password, Validator<FriendRequest> validator,Repository<Long, Utilizator> repoU ) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.repoU=repoU;
    }


    /**
     * @return all entities
     */

    @Override
    public Iterable<FriendRequest> findAll() {

        Set<FriendRequest> requests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from cereri_prietenie");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Long id1 = resultSet.getLong("user1");
                Long id2 = resultSet.getLong("user2");
                String status = resultSet.getString("status");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String date = resultSet.getString("date");
                LocalDate localDate = LocalDate.parse(date, formatter);

                Utilizator u1=repoU.findOne(id1);
                Utilizator u2=repoU.findOne(id2);


                FriendRequest request= new FriendRequest(u1, u2, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
                request.setStatus(Status.valueOf(status));

                requests.add(request);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }



    @Override
    public FriendRequest findOne(Tuple<Long, Long> id) {


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from cereri_prietenie ");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {

                Long id1 = resultSet.getLong("user1");
                Long id2 = resultSet.getLong("user2");

                if(id1==id.getLeft() && id2==id.getRight() || id1==id.getRight() && id2==id.getLeft() ) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    String date = resultSet.getString("date");
                    LocalDate localDate = LocalDate.parse(date, formatter);


                    Utilizator user1=repoU.findOne(id1);
                    Utilizator user2=repoU.findOne(id2);

                    FriendRequest friendRequest=new FriendRequest(user1, user2,  localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth() );

                    return friendRequest;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;


    }



    @Override
    public FriendRequest save(FriendRequest entity) {

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        String sql = "insert into cereri_prietenie (user1, user2, status, date) values (?, ?, ?,CAST(? as date))";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUser1().getId());
            ps.setLong(2, entity.getUser2().getId());

            ps.setString(3, entity.getStatus().toString());
            ps.setString(4,entity.getDate().toString());


            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest delete(Tuple<Long, Long> aLong) {

        if (aLong==null)
            throw new IllegalArgumentException("entity must be not null");

        FriendRequest p=findOne(aLong);
        int row_count=0;


        String sql = "delete from cereri_prietenie where (user1 = ? and user2=? or user1=? and user2=?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong.getLeft());
            ps.setLong(2, aLong.getRight());
            ps.setLong(3, aLong.getRight());
            ps.setLong(4,aLong.getLeft());

            row_count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(row_count==0)
            return null;
        return p;

    }

    @Override
    public FriendRequest update(FriendRequest entity) {

        validator.validate(entity);

        String sql = "update cereri_prietenie SET status=? where (user1=? and user2=? OR user1=? and user2=?)  ";
        int row_count = 0;


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {


            ps.setString(1, entity.getStatus().toString());
            ps.setLong(2, entity.getId().getLeft());
            ps.setLong(3, entity.getId().getRight());
            ps.setLong(4, entity.getId().getRight());
            ps.setLong(5, entity.getId().getLeft());

            row_count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if(row_count==0)
            return null;
        return entity;
    }




    @Override
    public int size()
    {
        String sql = " SELECT COUNT(*) AS total \n" +
                "FROM cereri_prietenie" +
                "  ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {


                return resultSet.getInt("total");

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;

    }


}