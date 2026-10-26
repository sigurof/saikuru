package no.sigurof.saikuru.api.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import no.sigurof.saikuru.app.ApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("citybike")
public class SaikuruController {

    private final ApplicationService applicationService;

    public SaikuruController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/stations")
    public ApplicationService.StationsWrapper getStations(
            // brief names for shorter URLs, but can be discussed
            @Parameter(description = "Longitude of the position to search from", example = "10.750505")
            @NotNull @RequestParam("lon") Double longitude,
            @Parameter(description = "Latitude of the position to search from", example = "59.910499")
            @NotNull @RequestParam("lat") Double latitude,
            @Parameter(description = "Maximum number of stations to return", example = "10", schema = @Schema(minimum = "1"))
            // TODO clamp to reasonable range, but throw client error on blatantly illegal input such as anything below 1
            @RequestParam("max") int maxElements
    ) {
         var coordinate = ApplicationService.Coordinate.at(latitude, longitude);
        return applicationService.getStationsWrapper(coordinate, maxElements);
    }



}
