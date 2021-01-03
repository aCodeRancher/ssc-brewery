package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFail;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by jt on 7/20/20.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {


    private  final LoginFailureRepository loginFailureRepository;

    private  final UserRepository userRepository;

   @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event){
        log.debug("Login failure");

        if(event.getSource() instanceof UsernamePasswordAuthenticationToken){
            LoginFail.LoginFailBuilder builder = LoginFail.builder();
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if(token.getPrincipal() instanceof String){
                 String attemptedUsername = (String)token.getPrincipal();

                userRepository.findByUsername(attemptedUsername)
                        .ifPresentOrElse(u -> {builder.username(u.getUsername());
                                          builder.user(u);} ,
                                           new Runnable() {
                                                          public void run(){
                                                              builder.username(attemptedUsername);
                                                          }
                        } );

                log.debug("Attempted Username: " + token.getPrincipal());
            }



            if(token.getDetails() instanceof WebAuthenticationDetails){
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();

                log.debug("Source IP: " + details.getRemoteAddress());
                builder.sourceIp(details.getRemoteAddress());
            }
            LoginFail loginFail = loginFailureRepository.save(builder.build());
            log.debug("Login fail saved " + loginFail.getId());
        }


    }
}
