package br.com.maxsueleinstein.stratega.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName, new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name(securitySchemeName)
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .tags(List.of(
                        new Tag().name("Users").description("Account registration and login endpoints."),
                        new Tag().name("Wallets").description("Wallet management endpoints."),
                        new Tag().name("Categories").description("Transaction category management endpoints."),
                        new Tag().name("Transactions").description("Income, expense, transfer, import, and export endpoints."),
                        new Tag().name("Dashboard").description("Financial summaries and reporting endpoints."),
                        new Tag().name("Budgets").description("Monthly spending budget endpoints."),
                        new Tag().name("Savings Goals").description("Savings goal management endpoints."),
                        new Tag().name("Groups").description("Shared expense group endpoints."),
                        new Tag().name("Notifications").description("User notification endpoints."),
                        new Tag().name("System").description("Service status and health endpoints.")
                ))
                .info(new Info()
                        .title("Stratega API")
                        .version("1.0")
                        .description("Financial planning API for the Stratega portfolio demo. In the published demo, protected endpoints can be tested without a JWT when demo access is enabled."));
    }
}
