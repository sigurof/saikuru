package no.sigurof.saikuru.app;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import no.sigurof.saikuru.domain.bikestation.BikeStation;
import no.sigurof.saikuru.domain.bikestation.BikeStationRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.lang.Math.PI;

@Service
public class ApplicationService {

    private final BikeStationRepository bikeStationRepository;

    public ApplicationService(BikeStationRepository bikeStationRepository) {
        this.bikeStationRepository = bikeStationRepository;
    }

    @Builder
    @Getter
    @Jacksonized
    @Schema(description = "A city bike station")
    public static class Station {
        // No reason to be brief here
        @Schema(description = "Latitude of the station", example = "59.910499")
        private final Double latitude;
        @Schema(description = "Longitude of the station", example = "10.750505")
        private final Double longitude;
        // Always include unit in variable name if type cannot convey it
        @Schema(description = "Distance in meters from the requested position to the station", example = "120.5")
        private final Double distanceMeters;

        @Builder
        @Getter
        @Jacksonized
        @Schema(description = "Availability of each different vehicle type")
        public static class Availability {
            @NonNull
            @Schema(description = "Number of available bikes", example = "10", minimum = "0")
            private final Integer bikes;
            @NonNull
            @Schema(description = "Number of available e-bikes", example = "2", minimum = "0")
            private final Integer eBikes;
        }

        private final Availability availability;
    }

    @Builder
    @Getter
    @Jacksonized
    @Schema(description = "A list of city bike stations")
    public static class StationsWrapper {
        @Schema(description = "Number of stations in `values`", example = "1", minimum = "0")
        private final int length;

        @ArraySchema(
                arraySchema = @Schema(description = "The stations, ordered by distance from the requested position"),
                schema = @Schema(implementation = Station.class)
        )
        private final List<Station> values;
    }

    public record Coordinate(Double latitude, Double longitude) {
        public static Coordinate at(Double latitude, Double longitude) {
            return new Coordinate(latitude, longitude);
        }
    }

    private static Double distanceBetween(Coordinate some, Coordinate other) {
        // TODO This is obviously super simplified, since
        //  1. it doesn't even calculate the distance, but rather the pythagorean number of "angular degrees"
        //  2. Pythagorean is obviously wrong on the surface of a sphere :)
        var earthRadiusMeters = 6378.137 * 1000;
        var earthCircumferenceMeters = 2 * PI * earthRadiusMeters;
        var metresPerDegree = earthCircumferenceMeters / 360.0;
        var dLat = some.latitude() - other.latitude();
        var dLng = some.longitude() - other.longitude();
        return metresPerDegree * Math.sqrt((dLat * dLat) + (dLng * dLng));
    }
    // TODO Create dedicated Longitude and Latitude types that enforce valid values
    //  lat in [-90, 90],
    //  lon in [0, 180]
    // TODO Create Coordinate type wrapping longitude and latitude

    public StationsWrapper getStationsWrapper(Coordinate referenceCoordinate, int maxElements) {
        // TODO Atm. the domain doesn't encode anything about sorting stations by their proximity to a referenceCoordinate
        List<Station> stations = bikeStationRepository.findAll().stream()
                .map(station -> toApiType(station, referenceCoordinate))
                .sorted(Comparator.comparing(Station::getDistanceMeters))
                .limit(maxElements)
                .toList();
        return responseWrapper(stations);
    }

    private static Station toApiType(BikeStation station, Coordinate coordinate) {
        Station.Availability availability = Station.Availability.builder()
                .bikes(station.getRentals().getNumBikes())
                .eBikes(station.getRentals().getNumEbikes())
                .build();
        var stationCoordinate = Coordinate.at(station.getLatitude(), station.getLongitude());
        return Station.builder()
                .availability(availability)
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .distanceMeters(distanceBetween(stationCoordinate, coordinate))
                .build();
    }

    private static StationsWrapper responseWrapper(List<Station> stations) {
        return StationsWrapper.builder()
                .length(stations.size())
                .values(stations)
                .build();
    }

}
