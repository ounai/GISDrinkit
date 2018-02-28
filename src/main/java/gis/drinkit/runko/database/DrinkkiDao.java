package gis.drinkit.runko.database;

import gis.drinkit.runko.domain.Drinkki;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DrinkkiDao implements Dao<Drinkki, Integer> {

    private Database database;

    public DrinkkiDao(Database database) {
        this.database = database;
    }

    @Override
    public Drinkki findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Drinkki drinkki = new Drinkki(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return drinkki;
    }

    @Override
    public List<Drinkki> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Drinkki");

        ResultSet rs = stmt.executeQuery();
        List<Drinkki> drinkit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            drinkit.add(new Drinkki(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return drinkit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Drinkki WHERE id = ?");

            stmt.setInt(1, key);
            stmt.executeUpdate();

            stmt.close();
            connection.close();
        }
    }

    public Drinkki saveOrUpdate(Drinkki drinkki) throws SQLException {
        if (drinkki.getNimi().isEmpty()) {
            return null;
        }
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Drinkki WHERE nimi = ?");
            stmt.setString(1, drinkki.getNimi());
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                stmt = conn.prepareStatement(
                        "INSERT INTO Drinkki (nimi) VALUES (?)");
                stmt.setString(1, drinkki.getNimi());
                stmt.executeUpdate();
            }

            stmt = conn.prepareStatement("SELECT * FROM Drinkki WHERE nimi = ?");
            stmt.setString(1, drinkki.getNimi());
            result = stmt.executeQuery();
            result.next();
            drinkki.setId(result.getInt("id"));
                    
            stmt.close();
            result.close();
        }

        return drinkki;
    }

}
