// package com.example.SellSyncNew.Config;


// import org.springframework.http.HttpMethod;
// import com.example.SellSyncNew.Service.CustomUserDetailsService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
    // @Autowired
    // private CustomLoginSuccessHandler successHandler;

    // @Autowired
    // private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;



    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
    // @Autowired
    // private CustomUserDetailsService customUserDetailsService;




    // @Bean
    // public CustomUserDetailsService userDetailsService() {
    //     return new CustomUserDetailsService(); // We'll create this next
    // }

    // @Bean
    // public DaoAuthenticationProvider authProvider() {
    //     DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
    //     auth.setUserDetailsService(userDetailsService());
    //     auth.setPasswordEncoder(passwordEncoder());
    //     return auth;
    // }

    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
    //         throws Exception {
    //     return config.getAuthenticationManager();
    // }
// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//     http
//         // ⭐ VERY IMPORTANT: cors first
//         .cors(Customizer.withDefaults())

//         .csrf(csrf -> csrf.disable())

//         .authorizeHttpRequests(auth -> auth

//             // ⭐ Preflight request allow pannanum
//             .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

//             // Public APIs
//             .requestMatchers(
//                 "/api/auth/**",
//                 "/api/register/**",
//                 "/api/public/**",
//                 "/actuator/health"
//             ).permitAll()

//             // Others secured
//             .anyRequest().authenticated()
//         )

//         // API based app – no HTML login
//         .formLogin(form -> form.disable())
//         .httpBasic(Customizer.withDefaults())

//         .sessionManagement(session ->
//             session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//         );

//     return http.build();
// }


// }

package com.example.SellSyncNew.Config;

import com.example.SellSyncNew.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**", "/api/register/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
            ); // <-- Semicolon added here

        return http.build();
    } // <-- Method closing brace added here

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://sellsync-frontend.netlify.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID"); 
        serializer.setCookiePath("/"); 
        serializer.setSameSite("None"); 
        serializer.setUseSecureCookie(true); 
        return serializer;
    }
}
