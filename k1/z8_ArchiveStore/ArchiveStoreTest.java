package k1.z8_ArchiveStore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

class NonExistingItemException extends Exception{
    NonExistingItemException(int id){
        super("Item with id " + id + " doesn't exist");
    }
}

class Archive {
    private int id;
    private Date dateArchived;

    public Archive(int id) {
        this.id = id;
        dateArchived = null;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public Date getDateArchived() {
        return dateArchived;
    }

    public int getId() {
        return id;
    }
}

class LockedArchive extends Archive {
    private Date dateToOpen;
    LockedArchive(int id, Date dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }

    public void setDateToOpen(Date dateToOpen) {
        this.dateToOpen = dateToOpen;
    }
}

class SpecialArchive extends Archive {
    private int maxOpen;
    private int openedCount;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.openedCount = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public void setMaxOpen(int maxOpen) {
        this.maxOpen = maxOpen;
    }

    public int getOpenedCount() {
        return openedCount;
    }

    public void setOpenedCount(int openedCount) {
        this.openedCount = openedCount;
    }
}

class ArchiveStore {
    private List<Archive> archives;
    private List<String> logs;

    ArchiveStore(){
        archives = new ArrayList<>();
        logs = new ArrayList<>();
    }

    void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        archives.add(item);
        logs.add(String.format(
                "Item %s archived at %s",
                item.getId(), date.toString().replace("CET", "UTC")
                )
        );
    }

    void openItem(int id, Date date) throws NonExistingItemException {
        Archive archive = archives.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NonExistingItemException(id));
        if (archive instanceof LockedArchive){
            LockedArchive la = (LockedArchive) archive;
            if (date.before(la.getDateToOpen())){
                logs.add(String.format(
                        "Item %s cannot be opened before %s",
                        id, la.getDateToOpen().toString().replace("CET", "UTC")
                        )
                );
            } else {
                logs.add(String.format(
                        "Item %s opened at %s",
                        id, date.toString().replace("CET", "UTC")
                ));
            }
        } else {
            SpecialArchive sa = (SpecialArchive) archive;
            if (sa.getMaxOpen() == sa.getOpenedCount()){
                logs.add(String.format(
                        "Item %s cannot be opened more than %s times",
                        id, sa.getMaxOpen()
                        )
                );
            } else {
                sa.setOpenedCount(sa.getOpenedCount() + 1);
                logs.add(String.format(
                        "Item %s opened at %s",
                        id, date.toString().replace("CET", "UTC")
                        )
                );
            }
        }
    }

    String getLog(){
        return String.join("\n", logs);
    }


}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
//        Date date = new Date(113, 10, 7); -> ne proaga vo code runner-ot
        LocalDate localDate = LocalDate.of(2013, 11, 7);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.of("UTC")).toInstant());

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            try {
                int open = scanner.nextInt();
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
//          catch (Exception e){break;}
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде



