package antifraud.Security;

import antifraud.Region.Region;
import antifraud.Region.RegionRepository;
import antifraud.Security.RestAuthenticationEntryPoint;
import antifraud.User.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class Configure extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/api/auth/user")
                .permitAll()
                .mvcMatchers("/actuator/shutdown")
                .permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/api/auth/user/**")
                .hasAuthority("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/api/auth/list")
                .hasAnyAuthority("ADMINISTRATOR", "SUPPORT")
                .mvcMatchers(HttpMethod.POST, "/api/antifraud/transaction")
                .hasAuthority("MERCHANT")
                .mvcMatchers(HttpMethod.PUT,"/api/antifraud/transaction")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.PUT, "/api/auth/role")
                .hasAuthority("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.PUT, "/api/auth/access")
                .hasAuthority("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.DELETE, "api/antifraud/suspicious-ip/**")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.GET,"api/antifraud/suspicious-ip")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.POST, "api/antifraud/suspicious-ip")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.DELETE, "api/antifraud/stolencard/**")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.GET, "api/antifraud/stolencard")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.POST, "api/antifraud/stolencard")
                .hasAuthority("SUPPORT")
                .mvcMatchers(HttpMethod.GET, "/api/antifraud/history/**")
                .hasAuthority("SUPPORT")
                .mvcMatchers("/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner loadData(RegionRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new Region("EAP"));
            repository.save(new Region("ECA"));
            repository.save(new Region("HIC"));
            repository.save(new Region("LAC"));
            repository.save(new Region("MENA"));
            repository.save(new Region("SA"));
            repository.save(new Region("SSA"));
        };
    }
}