package k1.z18_FINKIOnion;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception{
    CategoryNotFoundException(String name){
        super("Category " + name + " was not found");
    }
}

class Category implements Comparable<Category>{
    private String categoryType;

    public Category(String category) {
        this.categoryType = category;
    }

    @Override
    public int compareTo(Category o) {
        return categoryType.compareTo(o.categoryType);
    }

    public String getCategoryType() {
        return categoryType;
    }
}

abstract class NewsItem{
    protected String title;
    protected Date date;
    protected Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    abstract String getTeaser();


}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    String getTeaser() {
        Instant pastDateInstant = date.toInstant();
        Instant now = Instant.now();
        long minutes = Duration.between(pastDateInstant, now).toMinutes();
        return this.title + "\n"
                + minutes + "\n"
                + text.substring(0, Math.min(text.length(), 80))
                + "\n";
    }
}

class MediaNewsItem extends NewsItem{
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    String getTeaser() {
        Instant pastDateInstant = date.toInstant();
        Instant now = Instant.now();
        long minutes = Duration.between(pastDateInstant, now).toMinutes();
        return this.title + "\n"
                + minutes + "\n"
                + url + "\n"
                + views + "\n";
    }
}

class FrontPage{
    List<NewsItem> newsItems;
    Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        newsItems = new ArrayList<>();
    }

    void addNewsItem(NewsItem newsItem){
        newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category){
        return newsItems.stream()
                .filter(i -> i.category.equals(category))
                .collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = Arrays.stream(categories)
                .filter(c -> c.getCategoryType().equals(category))
                .findAny();

        return listByCategory(categoryOptional.orElseThrow(() -> new CategoryNotFoundException(category)));
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        newsItems.forEach(n -> sb.append(n.getTeaser()));
        return sb.toString();
    }


}



public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde