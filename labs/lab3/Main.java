package labs.lab3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes
class Ad implements Comparable<Ad>{
    private String id;
    private String category;
    private double bidValue;
    private double ctr;
    private String content;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getBidValue() {
        return bidValue;
    }

    public double getCtr() {
        return ctr;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString(){
        return String.format(
                "%s %s (bid=%.2f, ctr=%.2f%%) %s",
                id, category, bidValue, ctr * 100, content
        );
    }

    @Override
    public int compareTo(Ad o) {
        int diff = Double.compare(o.bidValue, this.bidValue);
        return diff != 0 ? diff : id.compareTo(o.id);
    }
}

class AdRequest{
    private String id;
    private String category;
    private double floorBid;
    private String keywords; // split by " "

    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getFloorBid() {
        return floorBid;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public String toString(){
        return String.format(
                "%s %s (floor=%.2f): %s",
                id, category, floorBid, keywords
        );
    }
}

class AdNetwork {
    ArrayList<Ad> ads;

    public AdNetwork() {
        this.ads = new ArrayList<>();
    }

    void readAds(BufferedReader br) throws IOException{
        while (true){
            String line = br.readLine();
            if (line.trim().isBlank()) break;
            String []parts = line.split("\\s++",5);

            ads.add(new Ad(parts[0], parts[1], Double.parseDouble(parts[2]),
                    Double.parseDouble(parts[3]), parts[4]));
        }
    }

    public List<Ad> placeAds(BufferedReader br, int k, PrintWriter pw)
    throws IOException{
        String line = br.readLine();
        String []parts = line.split("\\s++", 4);
        AdRequest adRequest = new AdRequest(parts[0], parts[1],
                Double.parseDouble(parts[2]), parts[3]);
        List<Ad> list = ads.stream()
                .filter(s -> s.getBidValue() >= adRequest.getFloorBid())
                .sorted(Comparator.comparing(ad ->
                        -(relevanceScore(ad, adRequest) + 5.0 * ad.getBidValue() + 100.0 * ad.getCtr())))
                .limit(k)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        pw.println("Top ads for request " + adRequest.getId() + ":");
        list.forEach(pw::println);
        return list;

    }


    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}
