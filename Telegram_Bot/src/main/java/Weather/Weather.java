package Weather;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=5632203e0ab680c83f667af056eb9269");

        Scanner in = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (in.hasNext()) {
            result.append(in.nextLine());
        }

        JSONObject object = new JSONObject(result.toString());
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONObject coord = object.getJSONObject("coord");
        model.setLon(coord.getDouble("lon"));
        model.setLat(coord.getDouble("lat"));


        return "Сейчас погода в городе: " + model.getName() + "\n" +
                "Температура воздуха: " + model.getTemp() + "C" + "\n" +
                "Влажность: " + model.getHumidity() + "%";
    }
}
