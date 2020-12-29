package guru.sfg.brewery.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.Customer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jt on 6/27/20.
 */
@SpringBootTest
public class CustomerControllerIT extends BaseIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("List Customers")
    @Nested
    class ListCustomers{
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdminCustomer")
        void testListCustomersAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(get("/customers")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());

        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamUser")
        void testListCustomersNOTAUTH(String user, String password) throws Exception {
            mockMvc.perform(get("/customers")
                    .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testListCustomersNOTLOGGEDIN() throws Exception {
            mockMvc.perform(get("/customers"))
                    .andExpect(status().isUnauthorized());

        }

        Customer customer = customerRepository.findAll().get(0);
        UUID customerId = customer.getId();
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdminCustomer")
        void showCustomerAUTH(String user, String pwd) throws Exception {

            mockMvc.perform(get("/customers/"+ customerId)
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());

        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamUser")
        void showNOTAUTH(String user, String password) throws Exception {

            mockMvc.perform(get("/customers/"+ customerId)
                    .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void showCustomerNOTLOGGEDIN() throws Exception {

            mockMvc.perform(get("/customers/"+ customerId))
                    .andExpect(status().isUnauthorized());

        }
    }

    @DisplayName("Add Customers")
    @Nested
    class AddCustomers {

        @Rollback
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void processCreationForm(String user, String password) throws Exception{
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Foo Customer")
                    .with(httpBasic(user, password)))
                    .andExpect(status().is3xxRedirection());
        }

        @Rollback
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void processCreationFormNOTAUTH(String user, String pwd) throws Exception{
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Foo Customer2")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void processCreationFormNOAUTH() throws Exception{
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Foo Customer"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Edit Customer")
    @Nested
    class EditCustomer {

        Customer customer = customerRepository.findAll().get(0);
        UUID customerId = customer.getId();

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void initUpdateCustomerForm(String user, String pwd) throws Exception{

            mockMvc.perform(get("/customers/"+ customerId+ "/edit")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void initUpdateCustomerFormNotAuthUser(String user, String pwd) throws Exception{

            mockMvc.perform(get("/customers/"+ customerId+ "/edit")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void initUpdateCustomerFormNotAuth( ) throws Exception{

            mockMvc.perform(get("/customers/"+ customerId+ "/edit")
                    .with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Edit Beer")
    @Nested
    class EditBeer{

        Beer beer = beerRepository.findAll().get(0);
        UUID beerId = beer.getId();
        Customer testCustomer = Customer.builder().build();

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdmin")
        void processUpdateForm(String user, String pwd) throws Exception{
            String testCustomerJson = objectMapper.writeValueAsString(testCustomer);

            mockMvc.perform(post("/customers/"+ beerId+ "/edit")
                    .content(testCustomerJson).contentType(MediaType.APPLICATION_JSON)
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().is3xxRedirection());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void initUpdateCustomerFormNotAuthUser(String user, String pwd) throws Exception{

            mockMvc.perform(post("/customers/"+ beerId+ "/edit")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void initUpdateCustomerFormNotAuth( ) throws Exception{

            mockMvc.perform(post("/customers/"+ beerId+ "/edit")
                    .with(anonymous()))
                    .andExpect(status().isUnauthorized());
        }
    }

}