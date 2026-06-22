package org.example.sustavzaupravljajeosobnimfinancijama.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sustav za upravljanje osobnim financijama API")
                        .version("1.0")
                        .description("REST API za upravljanje osobnim financijama - pracenje prihoda, rashoda, kategorija i izvjestaja")
                        .contact(new Contact()
                                .name("Kristian Pejic")
                                .email("kristian@example.com")));
    }
}
