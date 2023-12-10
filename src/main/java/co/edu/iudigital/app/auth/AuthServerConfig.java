package co.edu.iudigital.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.jwt.client-service}")
    private String client;

    @Value("${security.jwt.password-service}")
    private String secret;

    @Value("${security.jwt.scope-read}")
    private String read;

    @Value("${security.jwt.scope-write}")
    private String write;

    @Value("${security.jwt.grant-password}")
    private String grantPassword;

    @Value("${security.jwt.grant-refresh}")
    private String grantRefresh;

    @Value("${security.jwt.token-validity-seconds}")
    private Integer accessTime;

    @Value("${security.jwt.refresh-validity-seconds}")
    private Integer refreshTime;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /** Lo que se configuro en SpringSecurityConfig se inyectara aquí,
        para que el servidor lo use para el proceso del login
     **/
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenMoreInfo tokenMoreInfo;

    // Se implementan los 3 métodos de configuración
    // Configuración del endpoint de autorización y validación del token y firma.
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();   // Unimos la info del token por default con la nueva
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenMoreInfo, accessTokenConverter()));
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain);//
    }

    // Ruta del login debe ser pública [ servicio de autenticación ]
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(passwordEncoder)//---- 2
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(client)
                .secret(passwordEncoder.encode(secret))
                .scopes(read, write)    // Scope: permisos que va tener la App
                .authorizedGrantTypes(grantPassword, grantRefresh)
                .accessTokenValiditySeconds(accessTime)
                .refreshTokenValiditySeconds(refreshTime);
    }


    //  Se encarga de crear el token y almacenarlo
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    // Retorna un bean y traduce la información del token
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();// Por defacto tiene un token storage
        jwtAccessTokenConverter.setSigningKey(JWTConfig.RSA_PRIVATE);// clave secreta
        jwtAccessTokenConverter.setVerifierKey(JWTConfig.RSA_PUBLIC);
        return new JwtAccessTokenConverter();
    }
}
