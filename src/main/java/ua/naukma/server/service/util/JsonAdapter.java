package ua.naukma.server.service.util;

import com.google.gson.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonAdapter {
    public static Gson getCustomGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        JsonSerializer<LocalDate> writeData = (date, type, context) -> new JsonPrimitive(date.format(formatter).toString());
        JsonDeserializer<LocalDate> readData = (date, type, context) -> LocalDate.parse(date.getAsString(), formatter);
        gsonBuilder.registerTypeAdapter(LocalDate.class, writeData);
        gsonBuilder.registerTypeAdapter(LocalDate.class, readData);
        return gsonBuilder.create();
    }
}
