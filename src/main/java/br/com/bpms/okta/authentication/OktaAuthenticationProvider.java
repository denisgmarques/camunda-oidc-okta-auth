package br.com.bpms.okta.authentication;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OktaAuthenticationProvider extends ContainerBasedAuthenticationProvider {

    @Override
    public AuthenticationResult extractAuthenticatedUser(final HttpServletRequest request, final ProcessEngine engine) {
        Authentication auth = null;

        final SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            auth = context.getAuthentication();
        }

        if (auth instanceof OAuth2AuthenticationToken) {
            final OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return contextAuthentication(authentication);
        } else {
            return AuthenticationResult.unsuccessful();
        }

    }

    private AuthenticationResult contextAuthentication(OAuth2AuthenticationToken authentication) {
        final String userId = authentication.getName();

        if (StringUtils.isEmpty(userId)) {
            return AuthenticationResult.unsuccessful();
        }

        final AuthenticationResult result = new AuthenticationResult(userId, true);
        result.setGroups(AuthUtils.getUserGroups(authentication.getPrincipal(), userId));

        return result;
    }

    private List<String> extractGroupsFromToken(JSONArray groupsJson) {
        ArrayList<String> groupList = new ArrayList<>();
        if (groupsJson != null) {
            for (int i=0;i<groupsJson.size();i++){
                groupList.add(groupsJson.get(i).toString());
            }
        }
        return groupList;
    }
}
