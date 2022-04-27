package socialnetwork.repository.db;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;

import socialnetwork.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.sql.DriverManager;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {

    /**
     * folosite cand creez repo db
     */
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    /**
     * @param url       db postgres
     * @param username  db postgres
     * @param password  db postgres
     * @param validator validatorul entitatii validator
     */
    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
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
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from utilizatori");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String userName =resultSet.getString("user_name");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Utilizator utilizator = new Utilizator(userName, firstName, lastName);
                utilizator.setId(id);
                users.add(utilizator);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public Utilizator findOne(Long aLong) {


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from utilizatori ");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {

                Long id = resultSet.getLong("id");

                if (id == aLong) {
                    String userName = resultSet.getString("user_name");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    Utilizator utilizator = new Utilizator(userName, firstName, lastName);
                    utilizator.setId(id);

                    return utilizator;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Utilizator save(Utilizator entity) {

        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        String sql = "insert into utilizatori (user_name, first_name, last_name ) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getUserName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator delete(Long aLong) {

        if (aLong==null)
            throw new IllegalArgumentException("entity must be not null");

        Utilizator entity=findOne(aLong);
        int row_count=0;
        String sql = "delete from utilizatori where id = ? ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            row_count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(row_count==0)

         return null;
        return entity;

    }

    @Override
    public Utilizator update(Utilizator entity) {

        validator.validate(entity);
        //doar daca e valid pot verif prin main
        int row_count=0;
        String sql = "update utilizatori SET user_name=?, first_name=?, last_name=? where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getUserName());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setLong(4, entity.getId());

            row_count=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

           if(row_count==0)
            return entity;
           return   null;
   }


    @Override
    public int size()
    {
        String sql = "select  DISTINCT COUNT(u.id) as total\n" +
                "FROM utilizatori u";

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