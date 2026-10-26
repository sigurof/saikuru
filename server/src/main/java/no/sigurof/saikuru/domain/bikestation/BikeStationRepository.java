package no.sigurof.saikuru.domain.bikestation;

import no.sigurof.collections.Sets;
import no.sigurof.saikuru.infra.oslocitybikes.OsloCityBikesClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class BikeStationRepository {

    private final CachedOsloCityBikes osloCityBikes;

    BikeStationRepository(CachedOsloCityBikes osloCityBikes) {
        this.osloCityBikes = osloCityBikes;
    }

    private record InfoVsStatus(OsloCityBikesClient.StationInformation info, OsloCityBikesClient.StationStatus status) {

        static InfoVsStatus of(OsloCityBikesClient.StationInformation info, OsloCityBikesClient.StationStatus status) {
            return new InfoVsStatus(info, status);
        }

        boolean hasBoth() {
            return info != null && status != null;
        }
    }

    public List<BikeStation> findAll() {
        var stationInformationResponse = osloCityBikes.fetchStationInformation();
        var stationStatusResponse = osloCityBikes.fetchStationStatus();
        // TODO Deal with potential mismatch of elements between status response and information response properly
        Map<String, OsloCityBikesClient.StationInformation> infoByStationId = stationInformationResponse.getData().getStations().stream()
                // TODO Risk failing due to corrupted data from Oslo Kommune
                .collect(Collectors.toMap(OsloCityBikesClient.StationInformation::getStationId, stationInformation -> stationInformation));
        Map<String, OsloCityBikesClient.StationStatus> statusByStationId = stationStatusResponse.getData().getStations().stream()
                // TODO Risk failing due to corrupted data from Oslo Kommune
                .collect(Collectors.toMap(OsloCityBikesClient.StationStatus::getStationId, station -> station));
        Set<String> allStationIds = Sets.unionOf(infoByStationId.keySet(), statusByStationId.keySet());
        return allStationIds.stream()
                .map(id -> InfoVsStatus.of(infoByStationId.get(id), statusByStationId.get(id)))
                .filter(InfoVsStatus::hasBoth)
                .map(BikeStationRepository::buildBikeStation)
                .toList();
    }

    private static BikeStation buildBikeStation(InfoVsStatus value) {
        OsloCityBikesClient.StationInformation info = value.info();
        OsloCityBikesClient.StationStatus status = value.status();
        return BikeStation.builder()
                .longitude(info.getLon())
                .latitude(info.getLat())
                .address(info.getAddress())
                .name(info.getName())
                .rentals(buildRentals(status))
                .returns(buildReturns(status))
                .build();
    }

    private static BikeStation.Rentals buildRentals(OsloCityBikesClient.StationStatus status) {
        int ebikeCount = findEbikeVehicleType(status)
                .map(OsloCityBikesClient.VehicleType::getCount)
                .orElse(0);
        return BikeStation.Rentals.builder()
                .totalAvailable(status.getNumVehiclesAvailable())
                .allowed(status.isRenting())
                .numEbikes(ebikeCount)
                .numBikes(status.getNumBikesAvailable())
                .build();
    }

    private static Optional<OsloCityBikesClient.VehicleType> findEbikeVehicleType(OsloCityBikesClient.StationStatus status) {
        return status.getVehicleTypesAvailable().stream().filter(
                it -> it.getVehicleTypeId().equals("ebike")
        ).findFirst();
    }

    private static BikeStation.Returns buildReturns(OsloCityBikesClient.StationStatus status) {
        return BikeStation.Returns.builder()
                .accepted(status.isReturning())
                .numAvailable(status.getNumDocksAvailable())
                .build();
    }


}
