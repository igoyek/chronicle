package dev.igoyek.logblock;

public class LogBlockProvider {

    private static LogBlockApi instance;

    public static LogBlockApi provide() {
        if (instance == null) {
            throw new IllegalStateException("LogBlockApi has not been initialized yet!");
        }

        return instance;
    }

    static void initialize(LogBlockApi logBlockApi) {
        if (instance != null) {
            throw new IllegalStateException("LogBlockApi has already been initialized!");
        }

        instance = logBlockApi;
    }

    static void deinitialize() {
        if (instance == null) {
            throw new IllegalStateException("LogBlockApi has not been initialized yet!");
        }

        instance = null;
    }
}
