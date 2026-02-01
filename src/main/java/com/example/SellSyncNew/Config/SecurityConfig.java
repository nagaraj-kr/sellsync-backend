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
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomLoginSuccessHandler successHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;



    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private CustomUserDetailsService customUserDetailsService;




    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService(); // We'll create this next
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http.csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(auth -> auth
    //                     .requestMatchers(
    //                             "/api/register/**", "/api/auth/login",
    //                             "/register.html", "/Login.html", "/index.html", "/pricing.html",
    //                             "/About.html", "/forgot-password.html", "/contactus.html", "/features.html","/profile-verification.html",
    //                             "/css/**", "/js/**", "/images/**"
    //                     ).permitAll()
    //                     .requestMatchers("/api/wholesaler").authenticated()
    //                     .requestMatchers("/admindashboard.html").hasRole("ADMIN")
    //                     .requestMatchers("/manufacturerdashboard.html").hasRole("MANUFACTURER")
    //                     .requestMatchers("/wholesalerdashboard.html").hasRole("WHOLESALER")
    //                     .anyRequest().authenticated()
    //             )
    //             .sessionManagement(session -> session
    //                     .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    //             )
    //             .formLogin(form -> form
    //                     .loginPage("/Login.html")
    //                     .loginProcessingUrl("/login")
    //                     .usernameParameter("email")
    //                     .passwordParameter("password")
    //                     .successHandler(successHandler)
    //                     .failureHandler(customAuthenticationFailureHandler)
    //                     .permitAll()
    //             )
    //             .logout(logout -> logout
    //                     .logoutUrl("/logout")
    //                     .logoutSuccessUrl("/Login.html?logout=true")
    //                     .permitAll()
    //             );

    //     return http.build();
    // }
    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/error",
                        "/api/auth/**",
                        "/api/register/**",
                        "/api/public/**"
                ).permitAll()
                .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults());

    return http.build();
}

}

