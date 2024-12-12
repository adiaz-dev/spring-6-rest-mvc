package guru.springframework.spring6restmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {

  public static final String REST_API_USERNAME = "user1";
  public static final String REST_API_PASSWORD = "password";

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeRequests()
        .anyRequest().authenticated()
        .and().httpBasic(Customizer.withDefaults());

    http.csrf(httpSecurityCsrfConfigurer -> {
      httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/**");
    });

    return http.build();
  }

}
