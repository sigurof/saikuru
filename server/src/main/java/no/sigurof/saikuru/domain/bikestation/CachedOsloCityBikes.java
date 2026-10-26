package no.sigurof.saikuru.domain.bikestation;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import no.sigurof.saikuru.infra.oslocitybikes.OsloCityBikesClient;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
class CachedOsloCityBikes {

    private static final String KEY = "all";

    private final LoadingCache<String, OsloCityBikesClient.StationInformationResponse> stationInformation;
    private final LoadingCache<String, OsloCityBikesClient.StationStatusResponse> stationStatus;

    CachedOsloCityBikes(OsloCityBikesClient osloCityBikesClient) {
        this.stationInformation = Caffeine.newBuilder()
                .expireAfter(Expiry.creating((String key, OsloCityBikesClient.StationInformationResponse response) -> Duration.ofSeconds(response.getTtl())))
                .build(key1 -> osloCityBikesClient.fetchStationInformation());
        this.stationStatus = Caffeine.newBuilder()
                .expireAfter(Expiry.creating((String key, OsloCityBikesClient.StationStatusResponse response) -> Duration.ofSeconds(response.getTtl())))
                .build(key -> osloCityBikesClient.fetchStationStatus());
    }

    OsloCityBikesClient.StationInformationResponse fetchStationInformation() {
        return stationInformation.get(KEY);
    }

    OsloCityBikesClient.StationStatusResponse fetchStationStatus() {
        return stationStatus.get(KEY);
    }

}
