package no.sigurof.saikuru.api.rest;

import no.sigurof.saikuru.SaikuruApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@SpringBootTest(
        classes = SaikuruApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "client.name=ControllerTest"
)
class SaikuruControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void getStationsReturnsStationsAroundTheRequestedPosition() {
        var response = restTestClient.get()
                .uri("/citybike/stations?lon={lon}&lat={lat}&max={max}", 10.750505, 59.910499, 5)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SaikuruController.StationsWrapper.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getLength()).isEqualTo(response.getValues().size());
        assertThat(response.getValues()).singleElement().satisfies(station -> {
            assertThat(station.getLatitude()).isEqualTo(59.910499);
            assertThat(station.getLongitude()).isEqualTo(10.750505);
        });
    }
}
