package br.com.bpms.okta.client;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;

public class OktaIdentityServiceClient {

    @Autowired
    public Client client;

    public UserList getUsers() {
        return client.listUsers();
    }

    public GroupList getGroups() {
        return client.listGroups();
    }

    public User getUserById(String id) {
        return client.getUser(id);
    }

    public UserList getUsersByGroupId(String groupId) {
        return client.listUsers(groupId, null, null, null, null);
    }

    public GroupList getUserGroups(String userId) {
        return client.getUser(userId).listGroups();
    }
}
