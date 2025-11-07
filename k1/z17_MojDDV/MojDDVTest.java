package k1.z17_MojDDV;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;

class AmountNotAllowedException extends Exception{
    AmountNotAllowedException(int sum){
        super("Receipt with amount " + sum + " is not allowed to be scanned");
    }
}

class ScannedReceipt implements Comparable<ScannedReceipt>{
    private final String id;
    private final int sum;
    private final double taxReturn;

    public ScannedReceipt(String id, int sum, double taxReturn) throws AmountNotAllowedException {
        if (sum > 30000) throw new AmountNotAllowedException(sum);
        this.id = id;
        this.sum = sum;
        this.taxReturn = taxReturn;
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, sum, taxReturn);
    }

    @Override
    public int compareTo(ScannedReceipt o) {
        return Double.compare(taxReturn, o.taxReturn);
    }

    public double getTaxReturn() {
        return taxReturn;
    }
}

class MojDDV{
    private ArrayList<ScannedReceipt> receipts;
    static final Map<String, Double> TAXES = Map.of(
            "A", 0.18d,
            "B", 0.05d,
            "V", 0d);

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public void readRecords (InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            String []parts = sc.nextLine().split("\\s++");
            String id = parts[0];
            int totalSum = 0;
            double totalTax = 0;
            for (int i = 1; i < parts.length; i+=2) {
                int price = Integer.parseInt(parts[i]);
                String type = parts[i + 1];
                totalSum += price;
                totalTax += TAXES.get(type) * price * 0.15d;
            }
            try {
                receipts.add(new ScannedReceipt(id, totalSum, totalTax));
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

    }
    public void printTaxReturns(OutputStream outputStream){
        receipts.forEach(r -> {
            try {
                String outputString = r.toString() + "\n";
                outputStream.write(outputString.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    double min(){
        return receipts.stream()
                .sorted()
                .findFirst()
                .get()
                .getTaxReturn();
    }
    double max(){
        return receipts.stream()
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get()
                .getTaxReturn();
    }

    double sum(){
        return receipts.stream()
                .mapToDouble(ScannedReceipt::getTaxReturn)
                .sum();
    }

    long count(){
        return receipts.size();
    }

    double average(){
        return receipts.stream()
                .mapToDouble(ScannedReceipt::getTaxReturn)
                .average().getAsDouble();
    }

    public void printStatistics(PrintStream out) {
        out.printf("min:\t%5.3f%n", min());
        out.printf("max:\t%5.3f%n", max());
        out.printf("sum:\t%5.3f%n", sum());
        out.printf("count:\t%-5d%n", count());
        out.printf("avg:\t%5.3f%n", average());
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}
