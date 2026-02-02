package com.example.SellSyncNew.Config;



import com.example.SellSyncNew.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
//     @Autowired
//     private CustomLoginSuccessHandler successHandler;

//     @Autowired
//     private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;



//     @Bean
//     public BCryptPasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
//     @Autowired
//     private CustomUserDetailsService customUserDetailsService;




//     @Bean
//     public CustomUserDetailsService userDetailsService() {
//         return new CustomUserDetailsService(); // We'll create this next
//     }

//     @Bean
//     public DaoAuthenticationProvider authProvider() {
//         DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//         auth.setUserDetailsService(userDetailsService());
//         auth.setPasswordEncoder(passwordEncoder());
//         return auth;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
//             throws Exception {
//         return config.getAuthenticationManager();
//     }
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
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ IMPORTANT: use global CORS bean
            .cors(Customizer.withDefaults())

            // ❌ disable CSRF (API based login)
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // ✅ allow preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ public APIs
                .requestMatchers(
                    "/api/auth/**",
                    "/api/register/**",
                    "/actuator/health"
                ).permitAll()

                // 🔒 others need auth
                .anyRequest().authenticated()
            )

            // ❌ disable default login
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }
}


