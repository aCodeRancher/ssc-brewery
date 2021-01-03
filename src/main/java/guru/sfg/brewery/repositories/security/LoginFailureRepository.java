package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.LoginFail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginFailureRepository  extends JpaRepository<LoginFail, Integer> {
}
