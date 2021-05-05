package br.com.bpms.custom.service;

import br.com.bpms.okta.client.OktaIdentityServiceClient;
import br.com.bpms.okta.model.OktaUser;
import br.com.bpms.custom.entity.CustomUser;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final OktaIdentityServiceClient oktaIdentityServiceClient;

    public UserService(OktaIdentityServiceClient oktaIdentityServiceClient) {
        this.oktaIdentityServiceClient = oktaIdentityServiceClient;
    }

    private CustomUser fromOktaUser(final User oktaUser){
        return CustomUser.builder()
                .id(oktaUser.getId())
                .email(oktaUser.getProfile().getEmail())
                .firstName(oktaUser.getProfile().getFirstName())
                .lastName(oktaUser.getProfile().getLastName())
            .build();
    }

    public CustomUser findById(String id) {
        return this.fromOktaUser(this.oktaIdentityServiceClient.getUserById(id));
    }

    public Collection<CustomUser> findAll() {
        final UserList oktaUsers = this.oktaIdentityServiceClient.getUsers();
        return oktaUsers.stream()
                .map(this::fromOktaUser)
            .collect(Collectors.toList());
    }

    public Collection<CustomUser> findByGroupId(String groupId){
        return this.oktaIdentityServiceClient
            .getUsersByGroupId(groupId).stream()
                .map(this::fromOktaUser)
            .collect(Collectors.toList());
    }

}
