package br.com.bpms.custom;

import br.com.bpms.custom.plugin.CustomIdentityProviderFactory;
import br.com.bpms.custom.service.GroupService;
import br.com.bpms.custom.service.UserService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.stereotype.Component;

@Component
public class CustomIdentityProviderPlugin implements ProcessEnginePlugin {

    private final UserService userService;
    private final GroupService groupService;

    public CustomIdentityProviderPlugin(final UserService userService, final GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    public void preInit(final ProcessEngineConfigurationImpl configuration) {
        final CustomIdentityProviderFactory identityProviderFactory =
                new CustomIdentityProviderFactory(userService, groupService);
        configuration.setIdentityProviderSessionFactory(identityProviderFactory);
    }

    @Override
    public void postInit(final ProcessEngineConfigurationImpl configuration) { }

    @Override
    public void postProcessEngineBuild(final ProcessEngine engine) { }
}
