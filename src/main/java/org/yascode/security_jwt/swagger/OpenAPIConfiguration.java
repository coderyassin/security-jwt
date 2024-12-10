package org.yascode.security_jwt.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi(@Value("${server.env.request-protocol}") String requestProtocol,
                                 @Value("${server.env.address}") String address,
                                 @Value("${server.port}") String port,
                                 @Value("${server.env.description}") String serverDescription,
                                 @Value("${contact.name}") String name,
                                 @Value("${contact.email}") String email,
                                 @Value("${info.title}") String title,
                                 @Value("${info.version}") String version,
                                 @Value("${info.description}") String infoDescription) {
        Server server = new Server();
        server.setUrl(requestProtocol + "://" + address + ":" + port);
        server.setDescription(serverDescription);

        Contact myContact = new Contact();
        myContact.setName(name);
        myContact.setEmail(email);

        Info information = new Info()
                .title(title)
                .version(version)
                .description(infoDescription)
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
