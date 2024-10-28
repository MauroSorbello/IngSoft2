package com.is.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//indica que esta clase define beans que deben registrarse en el contexto de Spring.
@Configuration
//habilita la configuración de seguridad web personalizada en la aplicación.
@EnableWebSecurity
public class SecurityConfig {
    //bean de Spring Security que contiene la configuración de clientes OAuth2 (como Auth0 en este caso). Al inyectar
    //este repositorio en la clase, puedes utilizar su configuración en el manejo de autenticación y cierre de sesión.
    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }
    //SecurityFilterChain define la cadena de filtros de seguridad aplicada a las solicitudes HTTP. En Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //permite el acceso sin autenticación a la ruta de inicio (/), la página de login (/login) y cualquier
                // recurso bajo /public/**. anyRequest().authenticated() exige que cualquier otra ruta esté protegida y
                // requiera que el usuario esté autenticado.
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/usuario/login", "/usuario/logout","/img/**", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .defaultSuccessUrl("/usuario/inicio", true)
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                        .invalidateHttpSession(true) // Invalida la sesión local
                        .clearAuthentication(true) // Limpia la autenticación
                        .deleteCookies("JSESSIONID") // Elimina cookies de sesión
                );
        return http.build();
    }
//Configura la funcionalidad de cierre de sesión para usuarios autenticados mediante OpenID Connect. Utiliza un
// LogoutSuccessHandler personalizado que inicia el proceso de logout en el proveedor de OIDC (Auth0 en este caso) y
// redirige a la URL deseada tras el logout.
    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/");
        return successHandler;
    }
}
