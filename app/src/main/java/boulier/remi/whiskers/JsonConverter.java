package boulier.remi.whiskers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by Remi BOULIER on 13/10/2015.
 * email: boulier.r@gmail.com
 * project: Whiskers
 */
public class JsonConverter {
    private static final Gson gson = new GsonBuilder().create();

    public static <T> T getFromJSON(String json, Type typeOf) {
        return gson.fromJson(json, typeOf);
    }

    public static <T> T getFromJSON(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> String toJSON(T clazz) {
        return gson.toJson(clazz);
    }
}
