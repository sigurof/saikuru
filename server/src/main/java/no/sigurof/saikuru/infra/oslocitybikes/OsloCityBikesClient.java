package no.sigurof.saikuru.infra.oslocitybikes;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class OsloCityBikesClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public OsloCityBikesClient(
            @Qualifier("osloCityBikesRestTemplate") RestTemplate restTemplate,
            OsloCityBikesProperties properties) {
        this.restTemplate = restTemplate;
        this.baseUrl = properties.baseUrl();
    }

    @Getter
    @Builder
    @Jacksonized
    public static class VehicleType {
        // TODO Consider making enum?
        public final String vehicleTypeId;
        public final int count;
    }

    public static class LonLatPair extends ArrayList<Double> {
    }

    public static class CoordinateShape extends ArrayList<LonLatPair> {
    }

    @Getter
    @Builder
    @Jacksonized
    public static class StationArea {
        private final String type;
        private final List<List<CoordinateShape>> coordinates;
    }

    @Getter
    @Builder
    @Jacksonized
    public static class RentalUris {
        private final String android;
        private final String ios;
    }

    @Getter
    @Builder
    @Jacksonized
    public static class StationInformation {
        private final String stationId;
        private final String name;
        private final String address;
        private final String crossStreet;
        private final Double lat;
        private final Double lon;
        private final boolean isVirtualStation;
        private final int capacity;
        private final StationArea stationArea;
        private final RentalUris rentalUris;

    }

    @Getter
    @Builder
    @Jacksonized
    public static class Data<T> {
        private final List<T> stations;
    }

    @Getter
    @Builder
    @Jacksonized
    public static class StationInformationResponse {
        private final int lastUpdated;
        private final int ttl;
        @NonNull
        private final String version;
        @NonNull
        private final Data<StationInformation> data;
    }

    public StationInformationResponse fetchStationInformation() {
        // TODO use URI type for increased reliability
        return restTemplate.getForObject(baseUrl + "/station_information.json", StationInformationResponse.class);
    }

    @Getter
    @Builder
    @Jacksonized
    public static class StationStatus {
        String stationId;
        boolean isInstalled;
        boolean isRenting;
        boolean isReturning;
        long lastReported;
        int numbVehiclesAvailable;
        int numBikesAvailable;
        int numDocksAvailable;
        List<VehicleType> vehicleTypesAvailable;
    }

    @Getter
    @Builder
    @Jacksonized
    public static class StationStatusResponse {
        private final String lastUpdated;
        private final int ttl;
        private final String version;
        private final Data<StationStatus> data;
    }

    public StationStatusResponse fetchStationStatus() {
        // TODO use URI type for increased reliability
        return restTemplate.getForObject(baseUrl + "/station_status.json", StationStatusResponse.class);
    }


}
