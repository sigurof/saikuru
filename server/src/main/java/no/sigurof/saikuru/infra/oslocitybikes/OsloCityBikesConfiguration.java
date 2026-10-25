package no.sigurof.saikuru.infra.oslocitybikes;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@EnableConfigurationProperties(OsloCityBikesProperties.class)
@Configuration
public class OsloCityBikesConfiguration {

    private final OsloCityBikesProperties clientProperties;

    public OsloCityBikesConfiguration(OsloCityBikesProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Bean("osloCityBikesRestTemplate")
    RestTemplate restTemplate() {
        JsonMapper mapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().removeIf(c -> c instanceof JacksonJsonHttpMessageConverter);
        restTemplate.getMessageConverters().add(new JacksonJsonHttpMessageConverter(mapper));
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Client-Identifier", clientProperties.id());
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
