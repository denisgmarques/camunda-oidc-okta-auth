package br.com.bpms.okta.authentication;

import net.minidev.json.JSONArray;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.rest.util.EngineUtil;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthUtils {
    private static List<String> extractGroupsFromAuth(OAuth2User principal) {
        ArrayList<String> groupList = new ArrayList<>();
        JSONArray groupsJson = principal.getAttribute("groups");

        if (groupsJson != null) {
            for (int i = 0; i < groupsJson.size(); i++) {
                groupList.add(groupsJson.get(i).toString());
            }
        }
        return groupList;
    }

    private static List<String> getUserGroupsFromRepository(String userId) {
        return EngineUtil.lookupProcessEngine("default").getIdentityService()
                .createGroupQuery()
                .groupMember(userId)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

    public static List<String> getUserGroups(OAuth2User principal, String userId) {
        // Activate Groups Claim on Okta - https://developer.okta.com/blog/2019/06/20/spring-preauthorize#activate-groups-claim-on-okta
        if (principal != null && principal.getAttribute("groups") != null) {
            return AuthUtils.extractGroupsFromAuth(principal);
        } else {
            return getUserGroupsFromRepository(userId);
        }
    }
}
