package gis.drinkit.runko.database;

import gis.drinkit.runko.domain.Ainesosa;
import gis.drinkit.runko.domain.DrinkkiAinesosa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrinkkiAinesosaDao implements Dao<DrinkkiAinesosa, Integer> {
    
    private Database database;

    public DrinkkiAinesosaDao(Database database) {
        this.database = database;
    }

    @Override
    public DrinkkiAinesosa findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiAinesosa WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer drinkki_id = rs.getInt("drinkki_id");
        Integer ainesosa_id = rs.getInt("ainesosa_id");
        Integer jarjestys = rs.getInt("jarjestys");
        float maara =  rs.getFloat("maara");
        String ohje = rs.getString("ohje");
        

        DrinkkiAinesosa DrinkkiAinesosa = new DrinkkiAinesosa(id, drinkki_id, ainesosa_id, jarjestys, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return DrinkkiAinesosa;
    }

    @Override
    public List<DrinkkiAinesosa> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiAinesosa");
        ResultSet rs = stmt.executeQuery();
        List<DrinkkiAinesosa> drinkkiAinesosat = new ArrayList<>();
        while(rs.next()){
            Integer id = rs.getInt("id");
            Integer drinkki_id = rs.getInt("drinkki_id");
            Integer ainesosa_id = rs.getInt("ainesosa_id");
            Integer jarjestys = rs.getInt("jarjestys");
            float maara =  rs.getFloat("maara");
            String ohje = rs.getString("ohje");
            
            drinkkiAinesosat.add(new DrinkkiAinesosa(id, drinkki_id, ainesosa_id, jarjestys, maara, ohje));
            
        }
        rs.close();
        stmt.close();
        connection.close();
        
        return drinkkiAinesosat;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM DrinkkiAinesosa WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

}
