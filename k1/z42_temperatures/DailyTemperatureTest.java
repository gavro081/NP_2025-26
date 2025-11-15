package k1.z42_temperatures;

import java.io.*;
import java.util.*;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

class Temperature{
    private final double celsius;
    private final double fahrenheit;

    Temperature(char scale, double value){
        if (scale == 'C'){
            celsius = value;
            fahrenheit = value * 9 / 5 + 32;
        } else {
            fahrenheit = value;
            celsius = (value - 32) * 5 / 9;
        }
    }

    double getValue(char scale){
        return scale == 'C' ? celsius : fahrenheit;
    }

}

class DailyTemperatures {
    Map<Integer, List<Temperature>> temperatures;

    DailyTemperatures(){
        this.temperatures = new TreeMap<>();
    }

    public void readTemperatures(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.lines().forEach(l -> {
            String []parts = l.split("\\s+");
            ArrayList<Temperature> temps = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                char scale = parts[i].charAt(parts[i].length() - 1);
                double val = Double.parseDouble(parts[i].substring(0, parts[i].length() - 1));
                temps.add(new Temperature(scale, val));
            }
            temperatures.put(Integer.parseInt(parts[0]), temps);
        });
    }
    void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw = new PrintWriter(outputStream);
        for (int day : temperatures.keySet()){
            List<Temperature> temps = temperatures.get(day);
            String out = String.format("%3d: Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c",
                    day, temps.size(),
                    temps.stream().mapToDouble(t -> t.getValue(scale)).min().getAsDouble(), scale,
                    temps.stream().mapToDouble(t -> t.getValue(scale)).max().getAsDouble(), scale,
                    temps.stream().mapToDouble(t -> t.getValue(scale)).average().getAsDouble(), scale
                    );
            pw.println(out);
        }
        pw.flush();
    }

}