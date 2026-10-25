package no.sigurof.saikuru.infra.oslocitybikes;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class OsloCityBikesClientTest {

    private static final String CLIENT_ID = "Privat/FirstLast-Company";

    private OsloCityBikesClient osloCityBikesClient;

    @BeforeEach
    void beforeEach(WireMockRuntimeInfo wireMockRuntimeInfo) {
        var configuration = new OsloCityBikesConfiguration();
        OsloCityBikesClientProperties properties = new OsloCityBikesClientProperties(wireMockRuntimeInfo.getHttpBaseUrl());
        osloCityBikesClient = new OsloCityBikesClient(
                configuration.restTemplate(CLIENT_ID), properties
        );
    }

    @Nested
    class FetchStationInformation {

        @Test
        void setsTheClientId() {
            stubFor(get("/station_information.json").willReturn(ok()
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("fetchStationInformation200Ok.json")));
            osloCityBikesClient.fetchStationInformation();
            verify(getRequestedFor(urlEqualTo("/station_information.json"))
                    .withHeader("Client-Identifier", equalTo(CLIENT_ID)));
        }

        @Test
        void snapshotTest() {
            stubFor(get("/station_information.json").willReturn(okJson("""
                    {
                      "last_updated": 1792949655,
                      "ttl": 15,
                      "version": "2.3",
                      "data": {
                        "stations": [
                          {
                            "station_id": "6026",
                            "name": "Østbanehallen",
                            "address": "Jernbanetorget 1",
                            "cross_street": "utenfor Comfort Hotel",
                            "lat": 59.910499184043516,
                            "lon": 10.750505532434318,
                            "is_virtual_station": false,
                            "capacity": 35,
                            "station_area": {
                              "type": "MultiPolygon",
                              "coordinates": [
                                [
                                  [
                                    [
                                      10.750263304184585,
                                      59.91056029923746
                                    ],
                                    [
                                      10.750313815408745,
                                      59.910546303772364
                                    ],
                                    [
                                      10.750280725223575,
                                      59.91049700902121
                                    ],
                                    [
                                      10.750320254027173,
                                      59.91047498586818
                                    ],
                                    [
                                      10.75081536775275,
                                      59.910369606512546
                                    ],
                                    [
                                      10.7509019175846,
                                      59.910491837900025
                                    ],
                                    [
                                      10.750846790135625,
                                      59.91050226010438
                                    ],
                                    [
                                      10.750871153827916,
                                      59.91054106418767
                                    ],
                                    [
                                      10.750319392144633,
                                      59.91063727081874
                                    ],
                                    [
                                      10.750263304184585,
                                      59.91056029923746
                                    ]
                                  ]
                                ]
                              ]
                            },
                            "rental_uris": {
                              "android": "oslobysykkel://stations/6026",
                              "ios": "oslobysykkel://stations/6026"
                            }
                          }
                        ]
                      }
                    }
                    """)));
            var response = osloCityBikesClient.fetchStationInformation();
            assertThat(response).isNotNull();
            assertThat(response.getLastUpdated()).isEqualTo(1792949655);
            assertThat(response.getVersion()).isEqualTo("2.3");
            assertThat(response.getData()).satisfies(data -> {
                assertThat(data).isNotNull();
                assertThat(data.getStations()).satisfies(stations -> {
                    assertThat(stations).isNotEmpty().hasSize(1);
                    assertThat(stations.getFirst().getStationId()).isEqualTo("6026");
                });
            });
        }


    }

    @Nested
    class FetchStationStatus {

        @Test
        void setsTheClientId() {
            stubFor(get("/station_status.json").willReturn(ok()
                    .withHeader("Content-Type", "application/json")
                    .withBodyFile("fetchStationStatus200Ok.json")));
            osloCityBikesClient.fetchStationStatus();
            verify(getRequestedFor(urlEqualTo("/station_status.json"))
                    .withHeader("Client-Identifier", equalTo(CLIENT_ID)));
        }

        @Test
        void snapshotTest() {
            stubFor(get("/station_status.json").willReturn(okJson("""
                    {
                      "last_updated": 1792950165,
                      "ttl": 15,
                      "version": "2.3",
                      "data": {
                        "stations": [
                          {
                            "station_id": "6026",
                            "is_installed": true,
                            "is_renting": true,
                            "is_returning": true,
                            "last_reported": 1792950176,
                            "num_vehicles_available": 18,
                            "num_bikes_available": 18,
                            "num_docks_available": 15,
                            "vehicle_types_available": [
                              {
                                "vehicle_type_id": "bike",
                                "count": 2
                              },
                              {
                                "vehicle_type_id": "ebike",
                                "count": 16
                              }
                            ]
                          }
                        ]
                      }
                    }
                    """)));
            var response = osloCityBikesClient.fetchStationStatus();
            assertThat(response).isNotNull()
                    .extracting(OsloCityBikesClient.StationStatusResponse::getData)
                    .satisfies(it -> {
                        assertThat(it).isNotNull()
                                .extracting(OsloCityBikesClient.Data::getStations)
                                .satisfies(stations -> {
                                    assertThat(stations).isNotEmpty().
                                            singleElement().satisfies(station -> {
                                                assertThat(station).isNotNull();
                                                assertThat(station.getStationId()).isEqualTo("6026");
                                            });
                                });
                    });
        }
    }

}
