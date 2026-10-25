package no.sigurof.saikuru.infra.oslocitybikes;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@EnableConfigurationProperties(OsloCityBikesClientProperties.class)
@Configuration
public class OsloCityBikesConfiguration {

    @Bean("osloCityBikesRestTemplate")
    RestTemplate restTemplate(@Value("${no.sigurof.saikuru.integration.oslocitybikes.client.id}") String clientId) {
        JsonMapper mapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().removeIf(c -> c instanceof JacksonJsonHttpMessageConverter);
        restTemplate.getMessageConverters().add(new JacksonJsonHttpMessageConverter(mapper));
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Client-Identifier", clientId);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
