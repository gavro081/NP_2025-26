//package k1.z26_FileSystem;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

class File implements Comparable<File>{
    private String name;
    private Integer size;
    private LocalDateTime date;

    public File(String name, Integer size, LocalDateTime date) {
        this.name = name;
        this.size = size;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(File o) {
        if (!o.date.isEqual(date)){
            return date.compareTo(o.date);
        }
        if (name.compareTo(o.name) != 0){
            return name.compareTo(o.name);
        }
        return size - o.size;
    }

    public String toString(){
        return String.format("%-10s %5sB %s", name, size, date);
    }
}

class FileSystem{
    private List<File> allFiles;
    private HashMap<Character, ArrayList<File>> folderMap;

    public FileSystem() {
        this.folderMap = new HashMap<>();
        this.allFiles = new ArrayList<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        folderMap.putIfAbsent(folder, new ArrayList<>());
        File newFile = new File(name, size, createdAt);
        folderMap.get(folder).add(newFile);
        allFiles.add(newFile);

    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return allFiles.stream()
                .filter(f -> f.getName().startsWith(".") && f.getSize() < size)
                .sorted()
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        return folderMap
                .keySet()
                .stream()
                .filter(folders::contains)
                .map(key -> folderMap.get(key))
                .mapToInt(l -> l.stream().mapToInt(File::getSize).sum())
                .sum();
    }
    public Map<Integer, Set<File>> byYear(){
        Map<Integer, Set<File>> ans = new HashMap<>();
        for (File file : allFiles){
            int y = file.getDate().getYear();
            ans.putIfAbsent(y, new HashSet<>());
            ans.get(y).add(file);
        }
        return ans;
    }
    public Map<String, Long> sizeByMonthAndDay(){
        Map<String, Long> ans = new HashMap<>();
        for (File file : allFiles){
            Month month = file.getDate().getMonth();
            int day= file.getDate().getDayOfMonth();
            String date = month.toString() + "-" + day;
            ans.put(date, ans.getOrDefault(date, 0L) + file.getSize());
        }
        return ans;
    }
}

