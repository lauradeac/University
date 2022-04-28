package persistence.repository;

import model.Angajat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AngajatDBRepository implements AngajatRepo{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public AngajatDBRepository(Properties props) {
        logger.info("Initializing AngajatDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public void add(Angajat angajat) {
        logger.traceEntry("saving task {}", angajat);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement ps =con.prepareStatement("insert into angajati (user_name, password) values (?,?)")){
            ps.setString(1, angajat.getUserName());
            ps.setString(2,angajat.getPassword());
            int result=ps.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Angajat findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * from angajati ")){
            try(ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    int idAngajat = rs.getInt("id_angajat");

                    if (idAngajat == id) {
                        String userName = rs.getString("user_name");
                        String password = rs.getString("password");
                        Angajat angajat = new Angajat(userName, password);
                        angajat.setID(idAngajat);

                        return angajat;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Angajat> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Angajat> angajati = new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from angajati")){
            try(ResultSet result = ps.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id_angajat");
                    String userName = result.getString("user_name");
                    String password = result.getString("password");
                    Angajat angajat = new Angajat(userName, password);
                    angajat.setID(id);
                    angajati.add(angajat);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit(angajati);
        return angajati;
    }


    @Override
    public void delete(Angajat angajat) {
        logger.traceEntry("deleting task {}", angajat);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement ps =con.prepareStatement("delete from angajati where user_name = ? and password = ?")){
            ps.setString(1, angajat.getUserName());
            ps.setString(2,angajat.getPassword());
            int result=ps.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Angajat getAngajatByUsername(String username) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * from angajati")){
            try(ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    String user = rs.getString("user_name");

                    if (user.equals(username)) {
                        int idAngajat = rs.getInt("id_angajat");
                        String userName = rs.getString("user_name");
                        String password = rs.getString("password");
                        Angajat angajat = new Angajat(userName, password);
                        angajat.setID(idAngajat);

                        return angajat;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit();
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////



    @Override
    public void update(Integer integer, Angajat elem) {}
}
