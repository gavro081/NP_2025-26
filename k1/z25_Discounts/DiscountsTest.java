//package k1.z25_Discounts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
class Pair{
    private int orgPrice;
    private int salePrice;

    public Pair(int salePrice, int orgPrice) {
        this.orgPrice = orgPrice;
        this.salePrice = salePrice;
    }

    public int getOrgPrice() {
        return orgPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }
    public int getSalePercentage(){
        return (int) Math.floor(((float)(orgPrice - salePrice) / orgPrice) * 100);
    }
    public int absolute(){
        return orgPrice - salePrice;
    }
}

class Store{
    private String name;
    private List<Pair> products;

    public Store(String line){
        products = new ArrayList<>();
        String []parts = line.split("\\s++");
        this.name = parts[0];
        for (int i = 1; i < parts.length; i++) {
            String []prices = parts[i].split(":");
            products.add(new Pair(Integer.parseInt(prices[0]), Integer.parseInt(prices[1])));
        }
    }

    public double avg(){
        return products.stream()
                .mapToDouble(Pair::getSalePercentage)
                .average().getAsDouble();
    }

    public int total(){
        return products.stream()
                .mapToInt(Pair::absolute)
                .sum();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append('\n');
        sb.append(String.format("Average discount: %.1f%%\n", avg()));
        sb.append(String.format("Total discount: %s\n", total()));
        products.stream()
                .sorted(Comparator.comparingDouble(Pair::getSalePercentage).reversed()
                        .thenComparingInt(p -> -p.absolute()))
                .map(p -> String.format(
                        "%2d%% %s/%s\n",
                        p.getSalePercentage(),
                        p.getSalePrice(), p.getOrgPrice())
                ).forEach(sb::append);
        return sb.substring(0, sb.length() - 1); // za da se izbrise posledniot '/n'
    }

    public String getName() {
        return name;
    }
}

class Discounts {
    List<Store> stores;

    public Discounts(){
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            stores.add(new Store(sc.nextLine()));
        }
        return stores.size();
    }

    public List<Store> byAverageDiscount(){
        return stores.stream()
                .sorted(Comparator.comparingDouble(Store::avg).reversed()
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Store> byTotalDiscount(){
        return stores.stream()
                .sorted(Comparator.comparingDouble(Store::total)
                        .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toUnmodifiableList());
    }
}