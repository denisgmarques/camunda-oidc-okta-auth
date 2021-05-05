package br.com.bpms;

import br.com.bpms.okta.client.OktaIdentityServiceClient;
import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Value("${camunda.bpm.admin.username}")
    private String administratorUsername;

    @Bean
    @Primary
    @Order(Integer.MAX_VALUE - 1)
    public AdministratorAuthorizationPlugin administratorAuthorizationPlugin() {
        final AdministratorAuthorizationPlugin plugin = new AdministratorAuthorizationPlugin();
        plugin.setAdministratorUserName(administratorUsername);
        return plugin;
    }

    @Bean
    public OktaIdentityServiceClient oktaIdentityServiceClient() {
        return new OktaIdentityServiceClient();
    }

}
