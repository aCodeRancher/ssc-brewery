package guru.sfg.brewery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.h2.server.web.WebServlet;

@Configuration
@Slf4j
public class DataSourceConfig {

   @Bean
    public ServletRegistrationBean h2servletRegistration(){
         ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
         registrationBean.addUrlMappings("/h2-console/*");
         return registrationBean;
    }
}
