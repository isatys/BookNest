package com.BookNest.BookNestCore.config;

import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder; // Injecter le PasswordEncoder


    @Bean
    @Lazy
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/webjars/**", "/swagger-resources/**", "/v3/api-docs/**","/resources/**").permitAll() // Autoriser l'accès aux ressources statiques
                        .requestMatchers("/pages/**").authenticated()
                        .anyRequest().authenticated()

                )
                .formLogin(login -> {
                    login
                            .loginPage("/login") // Page de connexion personnalisée
                            .permitAll()
                            .defaultSuccessUrl("/pages/accueil", true); // Redirection après une connexion réussie
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(AbstractHttpConfigurer::disable // Désactiver CSRF pour permettre l'accès à Swagger
                );


        return http.build();
    }

    @Autowired
    @Lazy
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, true from users where username=?")
                .authoritiesByUsernameQuery("select u.username, r.name as authority " +
                        "from users u " +
                        "inner join user_roles ur on u.id = ur.user_id " +
                        "inner join roles r on ur.role_id = r.id " +
                        "where u.username=?")
                .passwordEncoder(passwordEncoder);
    }

}
