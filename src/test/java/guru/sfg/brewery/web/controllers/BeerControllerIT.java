package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/12/20.
 */
@SpringBootTest
public class BeerControllerIT extends BaseIT {


    @Autowired
    private BeerRepository beerRepository;

    @Test
    void initCreationFormWithSpring() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("spring", "guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormWithScott() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void editFormWithScott() throws Exception {
        Beer beer =beerRepository.findAll().get(0);
        String beerId = beer.getId().toString();
        mockMvc.perform(get("/beers/"+ beerId + "/edit").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createOrUpdateBeer"))
                .andExpect(model().attributeExists("beer"));
    }
    @Test
    void findBeersWithAdmin() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("spring","guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }
    @Test
    void findBeersWithCustomer() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("scott","tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithUser() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("user","password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }
    @Test
    void findBeersWithAnonymous() throws Exception{
        mockMvc.perform(get("/beers/find").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeersByIdWithAnonymous() throws Exception{
        Beer beer =beerRepository.findAll().get(0);
        String beerId = beer.getId().toString();
        mockMvc.perform(get("/beers/"+ beerId).with(anonymous()))
                .andExpect(status().isUnauthorized());
     }

    @Test
    void findBeersByIdWithAdmin() throws Exception{
        Beer beer =beerRepository.findAll().get(0);
        String beerId = beer.getId().toString();
        mockMvc.perform(get("/beers/"+ beerId).with(httpBasic("spring","guru")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/beerDetails"));
    }

    @Test
    void findBeersByIdWithCustomer() throws Exception{
        Beer beer =beerRepository.findAll().get(0);
        String beerId = beer.getId().toString();
        mockMvc.perform(get("/beers/"+ beerId).with(httpBasic("scott","tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/beerDetails"));
    }
    @Test
    void findBeersByIdWithUser() throws Exception{
        Beer beer =beerRepository.findAll().get(0);
        String beerId = beer.getId().toString();
        mockMvc.perform(get("/beers/"+ beerId).with(httpBasic("user","password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/beerDetails"));
    }

}
