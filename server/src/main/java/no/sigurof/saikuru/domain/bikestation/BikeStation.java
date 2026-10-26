package no.sigurof.saikuru.domain.bikestation;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BikeStation {

    @Builder
    @Getter
    public static class Returns {
        // How many docks available for return
        private final int numAvailable;
        // Whether return is allowed
        private final boolean accepted;
    }

    @Builder
    @Getter
    public static class Rentals {
        // How many available for rental
        private final int totalAvailable;
        // Whether rental is allowed
        private final boolean allowed;
        // How many ebikes available
        private final int numEbikes;
        private final int numBikes;
    }

    private final Double latitude;
    private final Double longitude;
    private final String name;
    private final String address;
    private final Returns returns;
    private final Rentals rentals;
}
