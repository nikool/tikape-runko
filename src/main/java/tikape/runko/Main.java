package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.PizzaDao;
import tikape.runko.database.PizzaRaakaAineDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Pizza;
import tikape.runko.domain.PizzaRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:pizzat.db");
//        database.init();

        PizzaDao pizzaDao = new PizzaDao(database);
        PizzaRaakaAineDao praDao = new PizzaRaakaAineDao(database);
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        
        // Etusivun lataus:
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        // Reseptilistan lataus:
        
        get("/pizzat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("pizzat", pizzaDao.findAll());

            return new ModelAndView(map, "pizzat");
        }, new ThymeleafTemplateEngine());

        // Tietyn pizzan reseptin lataus:
        
        get("/pizzat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("pizza", pizzaDao.findOne(Integer.parseInt(req.params("id"))));
            
            // Luodaan lista tietokannassa olevista resepteistä ja järjestetään se uudelle listalle merkittyjen järjestyksien mukaan:

            List<PizzaRaakaAine> pra = praDao.etsiTietynPizzanAineet(Integer.parseInt(req.params("id")));
            List<PizzaRaakaAine> praJarjestetty = new ArrayList<>();
            
            for (int i = 0; i < pra.size(); i++) {
                if (praJarjestetty.size() == 0) {
                    praJarjestetty.add(pra.get(i));
                }
                if (pra.get(i).getJarjestys() < praJarjestetty.get(0).getJarjestys()) {
                    if (!praJarjestetty.contains(pra.get(i))) {
                        praJarjestetty.add(0, pra.get(i));
                    }
                } else {
                    for (int indeksi = 0; indeksi < praJarjestetty.size(); indeksi++) {
                        if (pra.get(i).getJarjestys() > praJarjestetty.get(indeksi).getJarjestys() || pra.get(i).getJarjestys() == praJarjestetty.get(indeksi).getJarjestys()) {
                            if (indeksi == praJarjestetty.size()) {
                                if (!praJarjestetty.contains(pra.get(i))) {
                                    praJarjestetty.add(pra.get(i));
                                }
                            } else {
                                if (!praJarjestetty.contains(pra.get(i))) {
                                    praJarjestetty.add(indeksi + 1, pra.get(i));
                                }
                            }
                        }
                    }
                }
            }
            
            List<String> rivit = new ArrayList<>();
            
            // Luodaan listasta StringBuilderin avulla helppolukuisempi:
            
            for (int i = 0; i < praJarjestetty.size(); i++) {
                String nimi = raakaAineDao.findOne(praJarjestetty.get(i).getRaaka_aine_id()).getNimi();
                StringBuilder sb = new StringBuilder();
                sb.append(nimi).append(", ").append(praJarjestetty.get(i).getMaara()).append(", ").append(praJarjestetty.get(i).getOhje());
                rivit.add(sb.toString());
            }
            
            map.put("rivit", rivit);
            
            return new ModelAndView(map, "pizza");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/lisaaRaakaAineita", (req, res) -> {
            HashMap map = new HashMap();
            map.put("raakaAineet", raakaAineDao.findAll());
            return new ModelAndView(map, "lisaaRaakaAineita");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/lisaaRaakaAineita", (req, res) -> {
            
            RaakaAine raakaAine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAineDao.saveOrUpdate(raakaAine);
            
            res.redirect("/lisaaRaakaAineita");
            return "";
        });
        
        Spark.get("/muokkaa", (req, res) -> {
            HashMap map = new HashMap();
            map.put("pizzat", pizzaDao.findAll());
            return new ModelAndView(map, "muokkaa");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/lisaaPizza", (req, res) -> {
            
            Pizza pizza = new Pizza(-1, req.queryParams("nimi"));
            pizzaDao.saveOrUpdate(pizza);
            
            res.redirect("/muokkaa");
            return "";
        });
        
        Spark.post("/muokkaa", (req, res) -> {
            
            if (raakaAineDao.findByName(req.queryParams("raakaAineenNimi")).getId() != null && pizzaDao.findByName("pizzannimi").getId() != null) {
                System.out.println("EKA");
                PizzaRaakaAine pra = new PizzaRaakaAine(Integer.parseInt(req.queryParams("jarjestys")), 
                        req.queryParams("maara"), 
                        req.queryParams("ohje"), 
                        raakaAineDao.findByName(req.queryParams("raakaAineenNimi")).getId(), 
                        pizzaDao.findByName("pizzannimi").getId());
                System.out.println("TOKA");
                praDao.saveOrUpdate(pra);
                System.out.println("KOLMAS");
            }        
            
            res.redirect("/muokkaa");
            return "";
        });
    }
}
