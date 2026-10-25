package no.sigurof.saikuru.api.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("citybike")
public class SaikuruController {

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

    @GetMapping("/stations")
    public StationsWrapper getStations(
            // brief names for shorter URLs, but can be discussed
            @Parameter(description = "Longitude of the position to search from", example = "10.750505")
            @NotNull @RequestParam("lon") Double longitude,
            @Parameter(description = "Latitude of the position to search from", example = "59.910499")
            @NotNull @RequestParam("lat") Double latitude,
            @Parameter(description = "Maximum number of stations to return", example = "10", schema = @Schema(minimum = "1"))
            // TODO clamp to reasonable range, but throw client error on blatantly illegal input such as anything below 1
            @RequestParam("max") int maxElements
    ) {
        Station.Availability availability = Station.Availability.builder().bikes(maxElements).eBikes(maxElements).build();
        Station station = Station.builder()
                .latitude(latitude)
                .longitude(longitude)
                .availability(availability)
                .distanceMeters(100.0)
                .build();
        var stations = List.of(station);
        return StationsWrapper.builder()
                .length(stations.size())
                .values(stations)
                .build();

    }


}
