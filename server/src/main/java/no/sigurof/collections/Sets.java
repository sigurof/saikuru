package no.sigurof.collections;

import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public class Sets {

    // Calculates the union of two sets.
    public static <T> Set<T> unionOf(@NonNull Set<T> a, @NonNull Set<T> b){
        var copy = new HashSet<>(a);
        copy.addAll(b);
        return copy;
    }
}
