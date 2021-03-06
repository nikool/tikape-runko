
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Pizza;
import tikape.runko.domain.RaakaAine;

public class PizzaDao implements Dao<Pizza, Integer> {
    
    private Database database;
    
    public PizzaDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Pizza findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Pizza WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Pizza p = new Pizza(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return p;
    }
    
    @Override
    public List<Pizza> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Pizza");

        ResultSet rs = stmt.executeQuery();
        List<Pizza> pizzat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            pizzat.add(new Pizza(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return pizzat;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    public Pizza saveOrUpdate(Pizza object) throws SQLException {
        Pizza byName = findByName(object.getNimi());
        
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Pizza (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());
    }
    
    public Pizza findByName(String nimi) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Pizza WHERE nimi = ?");
            stmt.setString(1, nimi);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Pizza(result.getInt("id"), result.getString("nimi"));
        }
    }
}
