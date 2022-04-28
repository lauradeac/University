package persistence.repository;

import model.Spectacol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SpectacolDBRepository implements SpectacolRepo {

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm");

    public SpectacolDBRepository(Properties props) {
        logger.info("Initializing SpectacolDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public void add(Spectacol spectacol) {
        logger.traceEntry("saving task {}", spectacol);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("insert into spectacole (id_show, artist_name, dateOfShow, timeOfShow, location, remaining_tickets, total_tickets ) values (?,?,?,?,?,?,?)")){
            ps.setInt(1, spectacol.getID());
            ps.setString(2,spectacol.getArtistName());
            ps.setString(3, spectacol.getDateOfShow().format(dtf));
            ps.setString(4, spectacol.getTime().format(dtf1));
            ps.setString(5,spectacol.getLocation());
            ps.setInt(6, spectacol.getRemainingTickets());
            ps.setInt(7, spectacol.getTotalTickets());
            int result=ps.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer id, Spectacol spectacol) {
        logger.traceEntry("saving task {}", spectacol);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update spectacole set remaining_tickets=(?) where id_show=(?)")){
            preStmt.setInt(1, spectacol.getRemainingTickets());
            preStmt.setInt(2,id);

            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();


    }

    @Override
    public void delete(Spectacol spectacol) {
        logger.traceEntry("deleting spectacol {}", spectacol);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement ps = con.prepareStatement("delete from spectacole where id_show = ? and artist_name= ? and  dateOfShow =? and  timeOfShow=? and  location= ? and  remaining_tickets= ? and total_tickets =?")){
            ps.setInt(1, spectacol.getID());
            ps.setString(2,spectacol.getArtistName());
            ps.setString(3, spectacol.getDateOfShow().format(dtf));
            ps.setString(4, spectacol.getTime().format(dtf1));
            ps.setString(5,spectacol.getLocation());
            ps.setInt(6, spectacol.getRemainingTickets());
            ps.setInt(7, spectacol.getTotalTickets());
            int result=ps.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();



    }

    @Override
    public Spectacol findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * from spectacole")){
            try(ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    int idShow = rs.getInt("id_show");

                    if (idShow == id) {
                        int ID = rs.getInt("id_show");
                        String name = rs.getString("artist_name");
                        LocalDate date = LocalDate.parse(String.format(rs.getString("dateOfShow"), dtf));
                        LocalTime time = LocalTime.parse(String.format(rs.getString("timeOfShow"), dtf1));
                        String location =  rs.getString("location");
                        int remaining = rs.getInt("remaining_tickets");
                        int total = rs.getInt( "total_tickets");
                        Spectacol spectacol = new Spectacol(ID, name, date, time, location, remaining, total);
                        spectacol.setID(ID);

                        return spectacol;
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
    public Iterable<Spectacol> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Spectacol> shows = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from spectacole")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id_show");
                    String name = result.getString("artist_name");
                    LocalDate date = LocalDate.parse(String.format(result.getString("dateOfShow"), dtf));
                    LocalTime time = LocalTime.parse(String.format(result.getString("timeOfShow"), dtf1));
                    String location =  result.getString("location");
                    int remaining = result.getInt("remaining_tickets");
                    int total = result.getInt( "total_tickets");
                    Spectacol spectacol = new Spectacol(id, name, date, time, location, remaining, total);
                    spectacol.setID(id);
                    shows.add(spectacol);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit(shows);
        return shows;

    }

    @Override
    public List<Spectacol> findByDate(LocalDateTime date) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Spectacol> shows = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from spectacole")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id_show");
                    String name = result.getString("artist_name");
                    LocalDate dateShow = LocalDate.parse(String.format(result.getString("dateOfShow"), dtf));
                    LocalTime time = LocalTime.parse(String.format(result.getString("timeOfShow"), dtf1));
                    String location =  result.getString("location");
                    int remaining = result.getInt("remaining_tickets");
                    int total = result.getInt( "total_tickets");

                    if(dateShow.equals(date)){
                        Spectacol spectacol = new Spectacol(id, name, dateShow, time, location, remaining, total);
                        spectacol.setID(id);
                        shows.add(spectacol);
                    }
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit(shows);
        return shows;
    }
}
