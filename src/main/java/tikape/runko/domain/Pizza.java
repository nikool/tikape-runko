
package tikape.runko.domain;

public class Pizza {
    
    private Integer id;
    private String nimi;
    
    public Pizza(Integer id, String nimi) {
        this.nimi = nimi;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    
}
