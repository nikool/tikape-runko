
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.PizzaRaakaAine;
import tikape.runko.domain.RaakaAine;

public class PizzaRaakaAineDao implements Dao<PizzaRaakaAine, Integer> {
    
    private Database database;
    
    public PizzaRaakaAineDao(Database database) {
        this.database = database;
    }
    
    @Override
    public PizzaRaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PizzaRaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer jarjestys = rs.getInt("jarjestys");
        String maara = rs.getString("maara");
        String ohje = rs.getString("ohje");
        Integer raaka_aine_id = rs.getInt("raaka_aine_id");
        Integer pizza_id = rs.getInt("pizza_id");

        PizzaRaakaAine ra = new PizzaRaakaAine(jarjestys, maara, ohje, raaka_aine_id, pizza_id);

        rs.close();
        stmt.close();
        connection.close();

        return ra;
    }
    
    @Override
    public List<PizzaRaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PizzaRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<PizzaRaakaAine> pizzaRaakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer pizza_id = rs.getInt("pizza_id");

            pizzaRaakaAineet.add(new PizzaRaakaAine(jarjestys, maara, ohje, raaka_aine_id, pizza_id));
        }

        rs.close();
        stmt.close();
        connection.close();

        return pizzaRaakaAineet;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    public List<PizzaRaakaAine> etsiTietynPizzanAineet(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM PizzaRaakaAine WHERE pizza_id = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        
        List<PizzaRaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");
            Integer pizza_id = rs.getInt("pizza_id");
            raakaAineet.add(new PizzaRaakaAine(jarjestys, maara, ohje, raaka_aine_id, pizza_id));
        }
        rs.close();
        stmt.close();
        connection.close();
        
        return raakaAineet;
    }
    
    public PizzaRaakaAine saveOrUpdate(PizzaRaakaAine object) throws SQLException {
        PizzaRaakaAine byId = findById(object.getPizza_id());
        
        if (byId != null) {
            return byId;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO PizzaRaakaAine (jarjestys, maara, ohje, raaka_aine_id, pizza_id) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getJarjestys());
            stmt.setString(2, object.getMaara());
            stmt.setString(3, object.getOhje());
            stmt.setInt(4, object.getRaaka_aine_id());
            stmt.setInt(5, object.getPizza_id());
            stmt.executeUpdate();
        }

        return findById(object.getPizza_id());
    }
    
    private PizzaRaakaAine findById(Integer id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT jarjestys, maara, ohje, raaka_aine_id, pizza_id FROM PizzaRaakaAine WHERE pizza_id = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new PizzaRaakaAine(result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje"), result.getInt("raaka_aine_id"), result.getInt("pizza_id"));
        }
    }
}
