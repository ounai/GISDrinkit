package gis.drinkit.runko;

import gis.drinkit.runko.database.AinesosaDao;
import java.io.File;
import spark.Spark;
import gis.drinkit.runko.database.Database;
import gis.drinkit.runko.database.DrinkkiAinesosaDao;
import gis.drinkit.runko.database.DrinkkiDao;
import gis.drinkit.runko.domain.Ainesosa;
import gis.drinkit.runko.domain.Drinkki;
import gis.drinkit.runko.domain.DrinkkiAinesosa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        File tietokantaTiedosto = new File("db", "drinkit.db");

        Database tietokanta = new Database("jdbc:sqlite:" + tietokantaTiedosto.getAbsolutePath());
        tietokanta.init();

        DrinkkiDao drinkkiDao = new DrinkkiDao(tietokanta);
        AinesosaDao ainesosaDao = new AinesosaDao(tietokanta);
        DrinkkiAinesosaDao drinkkiAinesosaDao = new DrinkkiAinesosaDao(tietokanta);
        List<DrinkkiAinesosa> drinkinAinesosat = new ArrayList<>();


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
            Drinkki drinkki = drinkkiDao.findOne(id);
            
            map.put("drinkki", drinkki);
            map.put("drinkkiAinesosat", drinkkiAinesosaDao.etsiDrinkkiAinesosat(drinkki));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());

 
        Spark.get("/uusidrinkki", (req, res) -> {
            HashMap map = new HashMap();
            map.put("ainesosat", drinkinAinesosat);

            return new ModelAndView(map, "uusidrinkki");
        }, new ThymeleafTemplateEngine());
        


        Spark.post("/uusidrinkki", (req, res) -> {
            if (req.queryParams("nimi").isEmpty()) {
                return "Anna drinkille nimi";
            }
            Drinkki drinkki = new Drinkki(-1, req.queryParams("nimi"));
            drinkki = drinkkiDao.saveOrUpdate(drinkki);
            for (int x = 0; x < drinkinAinesosat.size(); x++) {
                DrinkkiAinesosa drinkkiAinesosa = drinkinAinesosat.get(x);
                drinkkiAinesosa.setDrinkki_id(drinkki.getId());
                Ainesosa ainesosa = ainesosaDao.etsiNimella(drinkkiAinesosa.getAinesosanNimi());
                drinkkiAinesosa.setAinesosa_id(ainesosa.getId());
                drinkkiAinesosaDao.saveOrUpdate(drinkkiAinesosa);
            }
            drinkinAinesosat.clear();
            

            res.redirect("/drinkit");
            return "";
        });
        
        Spark.post("/poista_drinkkiainesosa/:ainesosanNimi", (req, res) -> {
            for (int x = 0; x < drinkinAinesosat.size(); x++) {
                if (drinkinAinesosat.get(x).getAinesosanNimi().equals(req.params(":ainesosanNimi"))) {
                    drinkinAinesosat.remove(x);
                }
            }

            res.redirect("/uusidrinkki");
            return "";
        });
        
        Spark.get("/drinkkiainesosa", (req, res) -> {
            HashMap map = new HashMap();
            map.put("ainesosat", ainesosaDao.findAll());

            return new ModelAndView(map, "drinkkiainesosa");
        }, new ThymeleafTemplateEngine());


        Spark.post("/drinkkiainesosa", (req, res) -> {
            
            for(int x = 0; x < drinkinAinesosat.size(); x++) {
                if (drinkinAinesosat.get(x).getJarjestys() == (Integer.parseInt(req.queryParams("jarjestys")))) {
                    return "Ainesosan järjestysnumero drinkissä on jo käytössä, koitapa uudelleen";
                }
                if (drinkinAinesosat.get(x).getAinesosanNimi().equals(req.queryParams("ainesosanNimi"))) {
                    return "Ainesosa on jo drinkissä, valitsepa joku toinen";
                }
                
            }
            DrinkkiAinesosa drinkkiAinesosa = new DrinkkiAinesosa(-1, -1, -1, Integer.parseInt(req.queryParams("jarjestys")), Double.parseDouble(req.queryParams("maara")), req.queryParams("ohje"));
            drinkkiAinesosa.setAinesosanNimi(req.queryParams("ainesosanNimi"));
            drinkinAinesosat.add(drinkkiAinesosa);
            Collections.sort(drinkinAinesosat);

            res.redirect("/uusidrinkki");
            return "";
        });

        Spark.post("/poista_drinkki/:id", (req, res) -> {
            drinkkiDao.delete(Integer.parseInt(req.params(":id")));
            drinkkiAinesosaDao.poistaKokoDrinkki(Integer.parseInt(req.params(":id")));

            res.redirect("/drinkit");
            return "";
        });

        Spark.get("/ainesosat", (req, res) -> {
            HashMap map = new HashMap();
            map.put("ainesosat", ainesosaDao.findAll());

            return new ModelAndView(map, "ainesosat");
        }, new ThymeleafTemplateEngine());

        Spark.post("/ainesosat", (req, res) -> {
            if (req.queryParams("nimi").isEmpty()) {
        
                return "Anna oikea ainesosa";
            }
            Ainesosa ainesosa = new Ainesosa(-1, req.queryParams("nimi"));
            ainesosaDao.saveOrUpdate(ainesosa);

            res.redirect("/ainesosat");
            return "";
        });

        Spark.post("/poista_ainesosa/:id", (req, res) -> {
            ainesosaDao.delete(Integer.parseInt(req.params(":id")));

            res.redirect("/ainesosat");
            return "";
        });
        
    }
}
