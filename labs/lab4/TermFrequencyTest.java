package labs.lab4;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class TermFrequency{
    HashMap<String, Integer> countMap;
    HashSet<String> stopWordsSet;
    public TermFrequency(InputStream inputStream, String[] stopWords){
        countMap = new HashMap<>();
        stopWordsSet = new HashSet<>();
        for (String s : stopWords){
            stopWordsSet.add(s.toLowerCase());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("break")) break;
                line = line.toLowerCase()
                        .replaceAll("\\.", "")
                        .replaceAll(",", "")
                        .trim();

                Arrays.stream(line.split("\\s++"))
                        .filter(w -> !stopWordsSet.contains(w) && !w.isEmpty())
                        .forEach(w -> countMap.put(w, countMap.getOrDefault(w, 0) + 1));
            }
        } catch (Exception e){}
    }

    int countTotal(){
        return (int) countMap.values()
                .stream()
                .mapToDouble(Integer::doubleValue).sum();
    }

    int countDistinct() {
        return countMap.size();
    }

    List<String> mostOften(int k){
        return countMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde

