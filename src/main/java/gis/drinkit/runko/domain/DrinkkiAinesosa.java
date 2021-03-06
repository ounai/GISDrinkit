package gis.drinkit.runko.domain;

public class DrinkkiAinesosa implements Comparable {
    
    private Integer id;
    private Integer drinkki_id;
    private Integer ainesosa_id;
    private Integer jarjestys;
    private Double maara;
    private String ohje;
    private String ainesosanNimi;
    private String drinkinNimi;

    public DrinkkiAinesosa(Integer id, Integer drinkki_id, Integer ainesosa_id, Integer jarjestys, Double maara, String ohje) {
        this.id = id;
        this.drinkki_id = drinkki_id;
        this.ainesosa_id = ainesosa_id;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAinesosa_id() {
        return ainesosa_id;
    }

    public Integer getDrinkki_id() {
        return drinkki_id;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public Double getMaara() {
        return maara;
    }

    public String getOhje() {
        return ohje;
    }
    
    public void setAinesosanNimi(String ainesosanNimi) {
        this.ainesosanNimi = ainesosanNimi;
    }
    
    public String getAinesosanNimi() {
        return ainesosanNimi;
    }
    
    public void setDrinkki_id(Integer id) {
        this.drinkki_id = id;
    }
    
    public void setAinesosa_id(Integer id) {
        this.ainesosa_id = id;
    }
    
    public void setDrinkinNimi(String drinkinNimi) {
        this.drinkinNimi = drinkinNimi;
    }

    public String getDrinkinNimi() {
        return drinkinNimi;
    }

    @Override
    public int compareTo(Object drinkkiAinesosa) {
        int compareJarjestys=((DrinkkiAinesosa)drinkkiAinesosa).getJarjestys();
        /* For Ascending order*/
        return this.jarjestys-compareJarjestys;
    }

}
