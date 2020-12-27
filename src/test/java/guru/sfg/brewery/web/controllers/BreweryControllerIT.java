package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BreweryControllerIT extends BaseIT {

    @Test
    void listBreweriesCUSTOMER() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesADMIN() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesUSER() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles="CUSTOMER")
    void listBreweriesCUSTOMERRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void listBreweriesADMINRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                 .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles="USER")
    void listBreweriesUSERRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesNOAUTH() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getBreweriesJsonCUSTOMER() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonADMIN() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void getBreweriesJsonAdminRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                 .andExpect(status().is2xxSuccessful());
    }
    @Test
    @WithMockUser(roles="CUSTOMER")
    void getBreweriesJsonCustomerRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    @WithMockUser(roles="USER")
    void getBreweriesJsonUSERRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonUSER() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonNOAUTH() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }
}