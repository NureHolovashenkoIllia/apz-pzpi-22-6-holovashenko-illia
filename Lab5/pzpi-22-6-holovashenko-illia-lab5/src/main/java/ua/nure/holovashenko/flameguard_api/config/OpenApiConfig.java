package ua.nure.holovashenko.flameguard_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        //Server server = new Server();
        //server.setUrl("https://flameguard-api-ejfwdhe0dne6b2gw.centralus-01.azurewebsites.net");
        return new OpenAPI()
          //      .servers(List.of(server))
                .info(new Info()
                        .title("FlameGuard API")
                        .version("1.0")
                        .description("FlameGuard API documentation on how to use the system."));
    }
}
