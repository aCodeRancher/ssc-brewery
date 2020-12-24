package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class URLParamAuthFilter extends AbstractAuthFilter {

    public URLParamAuthFilter(RequestMatcher authenticationRequestMatcher){
        super(authenticationRequestMatcher);
    }

    protected String getPassword(HttpServletRequest request) {
        return request.getParameter("Api-Secret");
    }

    protected String getUsername(HttpServletRequest request) {
        return request.getParameter("Api-Key");
    }
}
