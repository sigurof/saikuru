package no.sigurof.saikuru.api.rest;

import no.sigurof.saikuru.SaikuruApplication;
import no.sigurof.saikuru.app.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@EnableWireMock(@ConfigureWireMock(
        // Overrides the baseUrl's domain with Wiremock's
        baseUrlProperties = "no.sigurof.saikuru.integration.oslocitybikes.client.baseurl",
        filesUnderDirectory = "src/test/resources"
))
@SpringBootTest(
        classes = SaikuruApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "client.name=ControllerTest"
)
class SaikuruControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @BeforeEach
    void beforeEach() {
        stubFor(get("/station_information.json").willReturn(ok()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("fetchStationInformation200Ok.json")));
        stubFor(get("/station_status.json").willReturn(ok()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("fetchStationStatus200Ok.json")));
    }

    @Test
    void getStationsReturnsStationsAroundTheRequestedPosition() {
        var response = restTestClient.get()
                .uri("/citybike/stations?lon={lon}&lat={lat}&max={max}", 10.750505, 59.910499, 5)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApplicationService.StationsWrapper.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getLength()).isEqualTo(response.getValues().size());
        assertThat(response.getValues()).singleElement().satisfies(station -> {
            assertThat(station.getLatitude()).isEqualTo(59.910499184043516);
            assertThat(station.getLongitude()).isEqualTo(10.750505532434318);
        });
    }
}
