package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
public class BreweryControllerIT  extends BaseIT{
    @Test
    void accessBreweryWithCustomerRole() throws Exception{
        mockMvc.perform(get("/brewery/breweries").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk())
                .andExpect(view().name("breweries/index"));
    }
    @Test
    @WithAnonymousUser
    void accessBrewery_notAuthenticated() throws Exception{
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessBreweryWithUserRole() throws Exception{
        mockMvc.perform(get("/brewery/breweries").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessBreweryWithAdminRole() throws Exception{
        mockMvc.perform(get("/brewery/breweries").with(httpBasic("spring", "guru")))
                .andExpect(status().isForbidden());
    }
    @Test
    void accessApiBreweryWithCustomerRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());

    }
    @Test
    @WithAnonymousUser
    void accessAPIBrewery_notAuthenticated() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessAPIBreweryWithUserRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAPIBreweryWithAdminRole() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("spring", "guru")))
                .andExpect(status().isForbidden());
    }
}
