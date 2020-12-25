package guru.sfg.brewery.userdetails;

import guru.sfg.brewery.domain.security.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import guru.sfg.brewery.domain.security.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user){
          this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = user.getAuthorities();
        ArrayList<SimpleGrantedAuthority> authorityArrayList = new ArrayList<SimpleGrantedAuthority>();
         for (Authority auth : authorities) {
             SimpleGrantedAuthority authority = new SimpleGrantedAuthority(auth.getRole());
             authorityArrayList.add(authority);
         }
        return authorityArrayList;
    }

    @Override
    public String getPassword(){
         return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
