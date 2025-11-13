package k1.z3_filesystem;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class FileNameExistsException extends Exception{
    FileNameExistsException(String message){
        super(message);
    }
}

interface IFile{
    String getFileName();
    long getFileSize();
    String getFileInfo(int i);
    void sortBySize();
    long findLargestFile();
}

class File implements IFile{
    String name;
    long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            // \t doesn't work :(
            sb.append("    ");
        }
        sb.append(String.format(
                "File name: %10s File size: %10s\n",
                name, size
        ));
        return sb.toString();
    }

    @Override
    public void sortBySize() {
    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class Folder implements IFile{
    String name;
    List<IFile> files;

    public Folder(String name) {
        this.name = name;
        this.files = new ArrayList<>();
    }

    void addFile(IFile file) throws FileNameExistsException {
        boolean check = files.stream()
                .anyMatch(f -> f.getFileName().equals(file.getFileName()));
        if (check) throw new FileNameExistsException(
                String.format("There is already a file named %s in the folder %s",
                        file.getFileName(), name));
        files.add(file);
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("    ");
        }
        sb.append(String.format(
                "Folder name: %10s Folder size: %10s\n",
                name, this.getFileSize()
        ));
        for (IFile file: files){
            sb.append(file.getFileInfo(n + 1));
        }
        return sb.toString();
    }


    @Override
    public void sortBySize() {
        this.files = files.stream()
                .sorted(Comparator.comparingInt(x -> (int) x.getFileSize()))
                .collect(Collectors.toList());
        files.stream()
                .filter(f -> f instanceof Folder)
                .forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {
        return files.stream()
                .mapToLong(IFile::findLargestFile)
                .max().orElse(0);
    }
}

class FileSystem {
    Folder rootDirectory;
    FileSystem(){
        rootDirectory = new Folder("root");
    }

    void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }

    long findLargestFile(){
        return rootDirectory.files.stream()
                .mapToLong(IFile::findLargestFile)
                .max().orElse(0);
    }

    void sortBySize(){
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}
