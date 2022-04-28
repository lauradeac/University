package socialnetwork.repository.db;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import socialnetwork.repository.Repository;

import java.util.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.DriverManager;

public class PrietenieDbRepository implements Repository<Tuple<Long, Long>, Prietenie> {

    /**
     * folosite cand creez repo db
     */
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    /**
     * @param url       db postgres
     * @param username  db postgres
     * @param password  db postgres
     * @param validator validatorul entitatii validator
     */
    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    /**
     *
     * @return all entities
     */

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "\n" +
                             "select f.id1 as id1, f.id2 as id2, u1.first_name as fN1, u2.first_name as fN2, f.date as date \n" +
                             "from prietenii f \n" +
                             "inner join utilizatori u1 on u1.id = f.id1 \n" +
                             "inner join utilizatori u2 on u2.id = f.id2"
                     );
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String date = resultSet.getString("date");

                LocalDate localDate = LocalDate.parse(date, formatter);

                Prietenie prietenie = new Prietenie(id1, id2, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
                prietenii.add(prietenie);
            }

            return prietenii;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    @Override

    public Prietenie findOne(Tuple<Long, Long> id) {


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from prietenii ");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {

                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                if(id1==id.getLeft() && id2==id.getRight() || id1==id.getRight() && id2==id.getLeft() ) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    String date = resultSet.getString("date");
                    LocalDate localDate = LocalDate.parse(date, formatter);

                    Prietenie prietenie = new Prietenie(id1, id2, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
                    return prietenie;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Prietenie save(Prietenie entity) {

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        int row_count=0;

        String sql2=
                "insert into prietenii ( id1, id2, date) values (?, ?, CAST(? as date))  ";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql2)) {

            ps.setInt(1, entity.getId().getLeft().intValue());
            ps.setInt(2, entity.getId().getRight().intValue());
            ps.setString(3,entity.getDate().toString());

            row_count=ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(row_count==0)
        return entity;
        return null;
    }

    @Override
    public Prietenie delete(Tuple<Long, Long> aLong) {

        if (aLong==null)
            throw new IllegalArgumentException("entity must be not null");

        Prietenie p=findOne(aLong);
        int row_count=0;

        String sql = "delete from prietenii where (id1 = ? and id2=? or id1=? and id2=?)";

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
    public Prietenie update(Prietenie entity) {

        validator.validate(entity);

        String sql = "update prietenii SET date=CAST(? as date) where (id1=? and id2=? OR id1=? and id2=?)  ";
        int row_count = 0;


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getDate().toString());
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
                "FROM prietenii" +
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