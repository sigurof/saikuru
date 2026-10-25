package no.sigurof.saikuru.infra.oslocitybikes;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("no.sigurof.saikuru.integration.oslocitybikes.client")
public record OsloCityBikesClientProperties(
        @NotBlank String baseUrl
) { }