package cl.randstad.user.infraestructura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {
	@Bean
    public OpenAPI customOpenAPI() {
		Contact contact = new Contact();
		contact.setName("Alvaro Vergara C.");
		contact.setEmail("alvaro.vergara.cl@gmail.com");
		
        return new OpenAPI()
            .info(new Info()
                .title("Randstad API")                
                .version("1.0")
                .description("Ejercicio tecnico para Randstad")
                .contact(contact));
    }

}
