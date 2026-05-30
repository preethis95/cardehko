package com.carfind.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carFindOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CarFind Decision Engine API")
                        .description(
                                "Full-stack Indian car recommendation engine. " +
                                "Submit a quiz payload and receive a ranked list of 100+ real Indian " +
                                "car models scored by budget fit, safety rating, mileage efficiency, " +
                                "fuel preference, and requested features. " +
                                "Includes non-trivial scoring logic: Safety priority doubles the safety weight; " +
                                "City use-case penalises low-mileage ICE cars."
                        )
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("CarFind Team")
                                .email("support@carfind.in"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ));
    }
}
