package co.edu.iudigital.app.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

public class SwaggerConfig {

    //http://localhost:8081/api/v1/v2/api-docs

    //http://localhost:8081/api/v1/swagger-ui.html
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("co.edu.iudigital.helpmeiud.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "API HelpmeIUDigital",
                "API para App HelpmeIUDigital",
                "1.0",
                "https://www.iudigital.edu.co/",
                new Contact("Nombre Apellido", "https://www.iudigital.edu.co/", "email@iudigital.edu.co"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );
    }
}
