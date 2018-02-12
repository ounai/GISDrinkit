package gis.drinkit.runko;

import java.io.File;
import spark.Spark;
import gis.drinkit.runko.database.Database;

public class Main {

    public static void main(String[] args) throws Exception {
        File tietokantaTiedosto = new File("db", "drinkit.db");
        
        Database tietokanta = new Database("jdbc:sqlite:" + tietokantaTiedosto.getAbsolutePath());
        tietokanta.init();
        
        Spark.get("/", (req, res) -> {
            return "testi";
        });
        
        /*Database database = new Database("jdbc:sqlite:opiskelijat.db");
        database.init();

        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());*/
    }
}
