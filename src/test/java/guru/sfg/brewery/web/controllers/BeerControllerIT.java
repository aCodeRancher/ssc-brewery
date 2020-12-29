package guru.sfg.brewery.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Created by jt on 6/12/20.
 */
@SpringBootTest
public class BeerControllerIT extends BaseIT{

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Init New Form")
    @Nested
    class InitNewForm{

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void initCreationFormAuth(String user, String pwd) throws Exception {

            mockMvc.perform(get("/beers/new").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void initCreationFormNotAdmin(String user, String pwd) throws Exception {

            mockMvc.perform(get("/beers/new").with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());

        }
        @Test
        void initCreationFormNotAuth() throws Exception {
            mockMvc.perform(get("/beers/new"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Init Find Beer Form")
    @Nested
    class FindForm{
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersFormAUTH(String user, String pwd) throws Exception{
            mockMvc.perform(get("/beers/find")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/findBeers"))
                    .andExpect(model().attributeExists("beer"));
        }


        @Test
        void findBeersWithAnonymous() throws Exception{
            mockMvc.perform(get("/beers/find").with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Process Find Beer Form")
    @Nested
    class ProcessFindForm{
        @Test
        void findBeerForm() throws Exception {
            mockMvc.perform(get("/beers").param("beerName", ""))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers").param("beerName", "")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Beer By Id")
    @Nested
    class GetByID {
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void getBeerByIdAUTH(String user, String pwd) throws Exception{
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/beers/" + beer.getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/beerDetails"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void getBeerByIdNoAuth() throws Exception{
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/beers/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Create Beer")
    @Nested
    class CreateBeer {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void processCreationFormAuth(String user, String pwd) throws Exception{

            Beer testBeer = Beer.builder()
                    .beerName("testBeer").beerStyle(BeerStyleEnum.WHEAT)
                    .minOnHand(100).quantityToBrew(100).upc("1234567").build();
            String beerJson = objectMapper.writeValueAsString(testBeer);
            mockMvc.perform(post("/beers/new").with(httpBasic(user,pwd))
                    .content(beerJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is3xxRedirection());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void processCreationFormNonAdmin(String user, String pwd) throws Exception{

            Beer testBeer = Beer.builder()
                    .beerName("testBeer").beerStyle(BeerStyleEnum.WHEAT)
                    .minOnHand(100).quantityToBrew(100).upc("1234567").build();
            String beerJson = objectMapper.writeValueAsString(testBeer);
            mockMvc.perform(post("/beers/new").with(httpBasic(user,pwd))
                    .content(beerJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
        @Test
        void createBeerNoAuth() throws Exception{
            Beer testBeer = Beer.builder()
                    .beerName("testBeer").beerStyle(BeerStyleEnum.WHEAT)
                    .minOnHand(100).quantityToBrew(100).upc("1234567").build();
            String beerJson = objectMapper.writeValueAsString(testBeer);
            mockMvc.perform(post("/beers/new").with(anonymous())
                    .content(beerJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Read Beer")
    @Nested
    class ReadBeerForm {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void processFindFormAuth(String user, String pwd) throws Exception{

            Beer testBeer = Beer.builder()
                    .beerName("Mango Bobs").build();
            String beerJson = objectMapper.writeValueAsString(testBeer);
            mockMvc.perform(get("/beers")
                    .content(beerJson)
                    .contentType(MediaType.APPLICATION_JSON).with(httpBasic(user,pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void processFindFormNoAuth() throws Exception{
            Beer testBeer = Beer.builder()
                    .beerName("Mango Bobs").build();
            String beerJson = objectMapper.writeValueAsString(testBeer);
            mockMvc.perform(get("/beers").with(anonymous())
                    .content(beerJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }


        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void showBeerAuth(String user, String password) throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/beers/"+firstBeer.getId()).with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }

        @Test
        void showBeerNoAuth() throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/beers/"+firstBeer.getId().toString()).with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void initUpdateBeerFormAuth(String user, String password) throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/beers/"+firstBeer.getId()+"/edit").with(httpBasic(user, password)))
                    .andExpect(model().attributeExists("beer"))
                    .andExpect(view().name("beers/createOrUpdateBeer"))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void initUpdateBeerFormNotAdmin(String user, String password) throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/beers/"+firstBeer.getId()+"/edit").with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void initUpdateBeerFormNotAuth() throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/beers/"+firstBeer.getId()+"/edit").with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void initCreateBeerFormAuth(String user, String password) throws Exception{

            mockMvc.perform(get("/beers/new").with(httpBasic(user, password)))
                    .andExpect(model().attributeExists("beer"))
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void initCreateBeerFormNotAdmin(String user, String password) throws Exception{

            mockMvc.perform(get("/beers/new").with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void initCreateBeerFormNonAuth() throws Exception{
            mockMvc.perform(get("/beers/new").with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Update Beer")
    @Nested
    class UpdateBeer {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void processUpdateFormAuth(String user, String pwd) throws Exception{

            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(post("/beers/"+ firstBeer.getId()+ "/edit").with(httpBasic(user,pwd)))
                    .andExpect(status().is3xxRedirection());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void udpateBeerNotAdmin(String user, String pwd) throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(post("/beers/"+ firstBeer.getId()+ "/edit").with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void udpateBeerNoAuth() throws Exception{
            Beer firstBeer = beerRepository.findAll().get(0);
            mockMvc.perform(post("/beers/"+ firstBeer.getId()+ "/edit").with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }
}