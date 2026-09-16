package dev.igoyek.chronicle;

public class ChronicleProvider {

    private static ChronicleApi instance;

    public static ChronicleApi provide() {
        if (instance == null) {
            throw new IllegalStateException("ChronicleApi has not been initialized yet!");
        }

        return instance;
    }

    static void initialize(ChronicleApi chronicleApi) {
        if (instance != null) {
            throw new IllegalStateException("ChronicleApi has already been initialized!");
        }

        instance = chronicleApi;
    }

    static void deinitialize() {
        if (instance == null) {
            throw new IllegalStateException("ChronicleApi has not been initialized yet!");
        }

        instance = null;
    }
}
