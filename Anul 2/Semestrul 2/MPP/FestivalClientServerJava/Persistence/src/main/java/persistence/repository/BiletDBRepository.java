package persistence.repository;

import model.Bilet;
import model.Spectacol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BiletDBRepository implements BiletRepo{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private SpectacolDBRepository spectacolRepository;

    public BiletDBRepository(Properties props, SpectacolDBRepository repoS) {
        logger.info("Initializing BiletDbRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.spectacolRepository = repoS;
    }

    @Override
    public void add(Bilet bilet) {
        logger.traceEntry("saving task {}", bilet);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement ps =con.prepareStatement("insert into bilete (id_bilet, show, no_tickets, buyer_name) values (?,?,?,?)")){
            ps.setInt(1, bilet.getIDBilet());
            ps.setInt(2,bilet.getShow().getID());
            ps.setInt(3,bilet.getNoTickets());
            ps.setString(4,bilet.getBuyerName());
            int result=ps.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Bilet findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("SELECT * from bilete")){
             try(ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    int idBilet = rs.getInt("id_bilet");

                    if (idBilet == id) {
                        int ID = rs.getInt("id_bilet");
                        Integer showId = rs.getInt("show");
                        Spectacol show = spectacolRepository.findById(showId);
                        int noTickets = rs.getInt("no_tickets");
                        String buyerName = rs.getString("buyer_name");
                        Bilet bilet = new Bilet(ID, show, noTickets, buyerName);
                        bilet.setIDBilet(idBilet);

                        return bilet;
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
    public Iterable<Bilet> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Bilet> bilete=new ArrayList<>();
        //b inner join spectacole as s on s.id_show = b.show
        try(PreparedStatement preStmt=con.prepareStatement("select * from bilete ")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id_bilet");
                    int showId = result.getInt("show");
                    Spectacol show = spectacolRepository.findById(showId);
                    int noTickets = result.getInt("no_tickets");
                    String buyerName = result.getString("buyer_name");
                    Bilet bilet = new Bilet(id, show, noTickets, buyerName);
                    bilet.setIDBilet(id);
                    bilete.add(bilet);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit(bilete);
        return bilete;
    }

    @Override
    public void delete(Bilet bilet) {
        logger.traceEntry("deleting bilet {}", bilet);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement ps =con.prepareStatement("delete from bilete where id_bilet = ? and show = ? and no_tickets = ? and buyer_name = ?")){
            ps.setInt(1, bilet.getIDBilet());
            ps.setInt(2,bilet.getShow().getID());
            ps.setInt(3,bilet.getNoTickets());
            ps.setString(4,bilet.getBuyerName());
            int result=ps.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }


    /////////////////////////////////////////////////////////////////////////////////
    @Override
    public void update(Integer integer, Bilet elem) {}

}
