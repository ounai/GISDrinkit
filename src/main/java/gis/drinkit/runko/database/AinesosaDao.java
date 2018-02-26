/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gis.drinkit.runko.database;

import gis.drinkit.runko.domain.Ainesosa;
import gis.drinkit.runko.domain.Drinkki;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AinesosaDao implements Dao<Ainesosa, Integer> {

    private Database database;

    public AinesosaDao(Database database) {
        this.database = database;
    }

    @Override
    public Ainesosa findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ainesosa WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Ainesosa Ainesosa = new Ainesosa(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return Ainesosa;
    }
    
    public Ainesosa etsiNimella(String ainesosanNimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ainesosa WHERE nimi = ?");
        stmt.setObject(1, ainesosanNimi);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Ainesosa Ainesosa = new Ainesosa(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return Ainesosa;
    }

    @Override
    public List<Ainesosa> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Ainesosa");

        ResultSet rs = stmt.executeQuery();
        List<Ainesosa> ainesosat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            ainesosat.add(new Ainesosa(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ainesosat;
    }
    
    public List<Ainesosa> etsiDrinkinAinesosat(Drinkki drinkki) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM DrinkkiAinesosa, Ainesosa WHERE drinkkiainesosa.ainesosa_id = ainesosa.id AND drinkki_id = ?");
        stmt.setInt(1, drinkki.getId());
        ResultSet rs = stmt.executeQuery();
        List<Ainesosa> ainesosat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            ainesosat.add(new Ainesosa(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return ainesosat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Ainesosa WHERE id = ?");

            stmt.setInt(1, key);
            stmt.executeUpdate();

            stmt.close();
        }

    }
    
    public Ainesosa saveOrUpdate(Ainesosa ainesosa) throws SQLException {
        if (ainesosa.getNimi().isEmpty()) {
            return null;
        }
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Ainesosa WHERE nimi = ?");
            stmt.setString(1, ainesosa.getNimi());
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                stmt = conn.prepareStatement(
                        "INSERT INTO Ainesosa (nimi) VALUES (?)");
                stmt.setString(1, ainesosa.getNimi());
                stmt.executeUpdate();
            }

            stmt.close();
            result.close();           
        }
        return null;
    }

}

