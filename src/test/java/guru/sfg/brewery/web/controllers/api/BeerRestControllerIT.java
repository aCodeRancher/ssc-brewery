package guru.sfg.brewery.web.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.bootstrap.DefaultBreweryLoader;
import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerDto;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jt on 6/13/20.
 */
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void deleteBeerHttpBasicNotAuth(String user, String pwd) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("List Beers")
    @Nested
    class ListBeers {
        @Test
        void findBeers() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(get("/api/v1/beer/").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Beer By ID")
    @Nested
    class GetBeerById {
        @Test
        void findBeerById() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerByIdAUTH(String user, String pwd) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Find By UPC")
    class FindByUPC {
        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerByUpcAUTH(String user, String pwd) throws Exception {
            String upc = DefaultBreweryLoader.BEER_1_UPC;
            mockMvc.perform(get("/api/v1/beerUpc/"+ upc)
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Save Beer")
    @Nested
    class SaveBeer{

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void saveBeerAUTH(String user, String pwd) throws Exception {
            BeerDto beerDto = BeerDto.builder().build();
            String beerDtoJson = objectMapper.writeValueAsString(beerDto);
            mockMvc.perform(post("/api/v1/beer/")
                    .with(httpBasic(user, pwd))
                    .content(beerDtoJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void saveBeerNotAdmin(String user, String pwd) throws Exception {
            BeerDto beerDto = BeerDto.builder().build();
            String beerDtoJson = objectMapper.writeValueAsString(beerDto);
            mockMvc.perform(post("/api/v1/beer/")
                    .with(httpBasic(user, pwd))
                    .content(beerDtoJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

         @Test
        void saveBeerNotAuth( ) throws Exception {
            BeerDto beerDto = BeerDto.builder().build();
            String beerDtoJson = objectMapper.writeValueAsString(beerDto);
            mockMvc.perform(post("/api/v1/beer/")
                    .content(beerDtoJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Edit Beer")
    @Nested
    class EditBeer{

        public Beer beerToEdit() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Edit Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void editBeerAUTH(String user, String pwd) throws Exception {
            BeerDto beerDto = BeerDto.builder().build();
            String beerDtoJson = objectMapper.writeValueAsString(beerDto);

            mockMvc.perform(put("/api/v1/beer/"+ beerToEdit().getId())
                    .with(httpBasic(user, pwd))
                    .content(beerDtoJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void editBeerNotAdmin(String user, String pwd) throws Exception {
            BeerDto beerDto = BeerDto.builder().build();
            String beerDtoJson = objectMapper.writeValueAsString(beerDto);

            mockMvc.perform(put("/api/v1/beer/"+beerToEdit().getId())
                    .with(httpBasic(user, pwd))
                    .content(beerDtoJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        void editBeerNotAuth( ) throws Exception {
            BeerDto beerDto = BeerDto.builder().build();
            String beerDtoJson = objectMapper.writeValueAsString(beerDto);

            mockMvc.perform(post("/api/v1/beer/"+beerToEdit().getId())
                    .content(beerDtoJson)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

}
