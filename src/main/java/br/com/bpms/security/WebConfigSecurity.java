package br.com.bpms.security;

import br.com.bpms.okta.authentication.OktaAuthenticationProvider;
import org.camunda.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.Filter;
import java.util.Collections;

@Configuration
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    public WebConfigSecurity(final CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().ignoringAntMatchers("/camunda/api/**")
            .and()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/camunda/app/**")
                .authenticated()
                .antMatchers("/camunda/api/**")
                .permitAll()
                .antMatchers("/camunda/app/*/styles/*.css", "/camunda/app/*/styles/*.js")
                .permitAll()
            .and()
                .oauth2Login()
                .defaultSuccessUrl("/camunda/app/welcome/default/#!/welcome")
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/**/logout"))
                .logoutSuccessHandler(customLogoutSuccessHandler);
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FilterRegistrationBean containerBasedAuthenticationFilter(){
        final FilterRegistrationBean<ContainerBasedAuthenticationFilter> registry = new FilterRegistrationBean<>();
        registry.setName("camunda-container-auth");
        registry.setFilter(new ContainerBasedAuthenticationFilter());
        registry.setInitParameters(Collections.singletonMap("authentication-provider",
                OktaAuthenticationProvider.class.getName()));
        registry.setOrder(101);
        registry.addUrlPatterns("/*");
        return registry;
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FilterRegistrationBean processEngineAuthenticationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("camunda-engine-auth");
        registration.setFilter(this.getProcessEngineAuthenticationFilter());
        registration.addInitParameter("authentication-provider",
                ProcessEngineAuthenticationFilter.class.getName());
        registration.setOrder(102);
        registration.addUrlPatterns("/engine-rest/*");
        return registration;
    }

    @Bean
    public Filter getProcessEngineAuthenticationFilter() {
        return new ProcessEngineAuthenticationFilter();
    }

}
