package gis.drinkit.runko;

import gis.drinkit.runko.database.AinesosaDao;
import java.io.File;
import spark.Spark;
import gis.drinkit.runko.database.Database;
import gis.drinkit.runko.database.DrinkkiDao;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        File tietokantaTiedosto = new File("db", "drinkit.db");
        
        Database tietokanta = new Database("jdbc:sqlite:" + tietokantaTiedosto.getAbsolutePath());
        tietokanta.init();
        
        DrinkkiDao drinkkiDao = new DrinkkiDao(tietokanta);
        AinesosaDao ainesosaDao = new AinesosaDao(tietokanta);
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/drinkit", (req, res) -> {
            HashMap map = new HashMap();
            map.put("drinkit", drinkkiDao.findAll());
            
            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap();
            Integer id = Integer.parseInt(req.params("id"));
            
            map.put("drink", drinkkiDao.findOne(id));
            
            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/ainesosat", (req, res) -> {
            HashMap map = new HashMap();
            map.put("ainesosat", ainesosaDao.findAll());
            
            return new ModelAndView(map, "ainesosat");
        }, new ThymeleafTemplateEngine());
    }
}
