
package tikape.runko.domain;

public class PizzaRaakaAine {
    
    private Integer jarjestys;
    private String maara;
    private String ohje;
    private Integer raaka_aine_id;
    private Integer pizza_id;

    public PizzaRaakaAine(Integer jarjestys, String maara, String ohje, Integer raaka_aine_id, Integer pizza_id) {
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
        this.raaka_aine_id = raaka_aine_id;
        this.pizza_id = pizza_id;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public void setMaara(String maara) {
        this.maara = maara;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }

    public Integer getRaaka_aine_id() {
        return raaka_aine_id;
    }

    public void setRaaka_aine_id(Integer raaka_aine_id) {
        this.raaka_aine_id = raaka_aine_id;
    }

    public Integer getPizza_id() {
        return pizza_id;
    }

    public void setPizza_id(Integer pizza_id) {
        this.pizza_id = pizza_id;
    }
    
    
}
