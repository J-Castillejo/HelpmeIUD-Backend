package co.edu.iudigital.app.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // URLS abiertas sin autenticación ni autorización
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/delitos").permitAll()
                .antMatchers(HttpMethod.POST, "/usuarios/signup**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/usuarios/signup**").permitAll()
                .antMatchers(HttpMethod.GET, "/casos", "/casos/caso/**").permitAll()
                .antMatchers(HttpMethod.GET, "/usuarios/uploads/img/**").permitAll()
                .anyRequest().authenticated()   // Rutas no especificadas, serán para usuarios autenticados
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable();
    }

    // Configuración de los CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);// Rutas del Back
        return source;
    }
    /** Configuración en la prioridad más alta de los filtros de Spring
        se aplica al servidor de auth que genera el token y lo valida para acceder a los servicios
    **/
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
