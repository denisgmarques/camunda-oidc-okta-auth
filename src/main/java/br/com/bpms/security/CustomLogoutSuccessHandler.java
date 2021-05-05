package br.com.bpms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${okta.oauth2.issuer}")
    private String issuer;

    private static final String SIGNOUT_URL = "/v1/logout";

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Authentication authentication) throws IOException {
        final HttpSession session;

        if ((session = request.getSession(false)) != null) {
            session.invalidate();
        }

        // https://dev-XPTO1231.okta.com/oauth2/default/v1/logout?id_token_hint=${id_token}&post_logout_redirect_uri=${post_logout_redirect_uri}&state=${state}
        redirectStrategy.sendRedirect(request, response, issuer + SIGNOUT_URL + "?id_token_hint=" + getToken(authentication));
    }

    private String getToken(Authentication authentication) {
        return ((OidcUser) authentication.getPrincipal()).getIdToken().getTokenValue();
    }
}
