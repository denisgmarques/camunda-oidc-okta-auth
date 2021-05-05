package br.com.bpms.custom.service;

import br.com.bpms.custom.entity.CustomGroup;
import br.com.bpms.okta.client.OktaIdentityServiceClient;
import com.okta.sdk.resource.group.Group;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final OktaIdentityServiceClient oktaIdentityServiceClient;

    public GroupService(final OktaIdentityServiceClient oktaIdentityServiceClient) {
        this.oktaIdentityServiceClient = oktaIdentityServiceClient;
    }

    public CustomGroup findById(final String id) {
        return CustomGroup.builder()
                .id(id)
                .name(id)
                .type("")
            .build();
    }

    private CustomGroup fromOktaGroup(final Group oktaGroup){
        return CustomGroup.builder()
                .id(oktaGroup.getId())
                .name(oktaGroup.getProfile().getName())
                .type("")
            .build();
    }

    public List<CustomGroup> findAll() {
        return this.oktaIdentityServiceClient
                .getGroups()
                .stream()
                .map(this::fromOktaGroup)
            .collect(Collectors.toList());
    }

    public List<CustomGroup> getGroupsForUser(final String userId){
        return this.oktaIdentityServiceClient
                .getUserGroups(userId)
                .stream()
                .map(this::fromOktaGroup)
            .collect(Collectors.toList());
    }
}
