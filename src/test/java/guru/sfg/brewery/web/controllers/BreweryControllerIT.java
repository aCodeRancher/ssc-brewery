package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BreweryControllerIT extends BaseIT {

    @DisplayName("List breweries")
    @Nested
    class listBreweries {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BreweryControllerIT#getStreamAdminCustomer")
        void listBreweriesAuth(String user, String password) throws Exception {
            mockMvc.perform(get("/brewery/breweries")
                    .with(httpBasic(user, password)))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BreweryControllerIT#getStreamUser")
        void listBreweriesUser(String user, String password) throws Exception {
            mockMvc.perform(get("/brewery/breweries")
                    .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void listBreweriesAuth() throws Exception {
            mockMvc.perform(get("/brewery/breweries")
                    .with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Restful Breweries")
    @Nested
    class GetBreweriesJson {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BreweryControllerIT#getStreamAdminCustomer")
        void getBreweriesJsonCUSTOMER(String user, String password) throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries")
                    .with(httpBasic(user, password)))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BreweryControllerIT#getStreamUser")
        void getBreweriesJsonUSER(String user, String password) throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries")
                    .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void getBreweriesJsonNOAUTH() throws Exception {
            mockMvc.perform(get("/brewery/api/v1/breweries").with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }

    }

}