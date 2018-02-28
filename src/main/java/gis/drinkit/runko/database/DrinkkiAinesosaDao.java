package gis.drinkit.runko.database;

import gis.drinkit.runko.domain.Drinkki;
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
        Double maara =  rs.getDouble("maara");
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
            Double maara =  rs.getDouble("maara");
            String ohje = rs.getString("ohje");
            
            drinkkiAinesosat.add(new DrinkkiAinesosa(id, drinkki_id, ainesosa_id, jarjestys, maara, ohje));
            
        }
        rs.close();
        stmt.close();
        connection.close();
        
        return drinkkiAinesosat;

    }
    
        public DrinkkiAinesosa saveOrUpdate(DrinkkiAinesosa drinkkiAinesosa) throws SQLException {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DrinkkiAinesosa WHERE drinkki_id = ? AND ainesosa_id = ?");
            stmt.setInt(1, drinkkiAinesosa.getDrinkki_id());
            stmt.setInt(2, drinkkiAinesosa.getAinesosa_id());
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                stmt = conn.prepareStatement(
                        "INSERT INTO DrinkkiAinesosa (drinkki_id, ainesosa_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
                stmt.setInt(1, drinkkiAinesosa.getDrinkki_id());
                stmt.setInt(2, drinkkiAinesosa.getAinesosa_id());
                stmt.setInt(3, drinkkiAinesosa.getJarjestys());
                stmt.setDouble(4, drinkkiAinesosa.getMaara());
                stmt.setString(5, drinkkiAinesosa.getOhje());
                stmt.executeUpdate();
            }

            stmt.close();
            result.close();           
        }
        return null;
    }
    
    public List<DrinkkiAinesosa> etsiDrinkkiAinesosat(Drinkki drinkki) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiAinesosa, Ainesosa WHERE drinkkiainesosa.ainesosa_id = ainesosa.id AND drinkki_id = ? ORDER BY jarjestys");
        stmt.setInt(1, drinkki.getId());
        ResultSet rs = stmt.executeQuery();
        List<DrinkkiAinesosa> drinkkiAinesosat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer drinkki_id = rs.getInt("drinkki_id");
            Integer ainesosa_id = rs.getInt("ainesosa_id");
            Integer jarjestys = rs.getInt("jarjestys");
            Double maara = rs.getDouble("maara");
            String ohje = rs.getString("ohje");
            String ainesosanNimi = rs.getString("nimi");
            
            DrinkkiAinesosa drinkkiAinesosa = new DrinkkiAinesosa(id, drinkki_id, ainesosa_id, jarjestys, maara, ohje);
            drinkkiAinesosa.setAinesosanNimi(ainesosanNimi);
            drinkkiAinesosat.add(drinkkiAinesosa);

        }
        rs.close();
        stmt.close();
        connection.close();

        return drinkkiAinesosat;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM DrinkkiAinesosa WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();
        }
    }
    
    public void poistaKokoDrinkki(Integer key) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM DrinkkiAinesosa WHERE drinkki_id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            stmt.close();
        }
    }

}
