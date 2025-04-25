import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class App {
    static List<Book> books = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "books.csv";

    public static void main(String[] args) {
        if (!loginMenu()) return;

        loadBooks();

        int choice;
        do {
            System.out.println("\n=== Library System (CSV Version) ===");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> borrowBook();
                case 4 -> returnBook();
                case 5 -> {
                    saveBooks();
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 5);
    }

    public static boolean loginMenu() {
        System.out.println("=== Welcome to Library System ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose option: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (option == 1) {
            if (UserAuth.login(username, password)) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Invalid username or password.");
                return false;
            }
        } else if (option == 2) {
            if (UserAuth.register(username, password)) {
                System.out.println("Registration successful. Please login next time.");
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Invalid choice.");
            return false;
        }
    }
    public static void loadBooks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            books.clear();
            while ((line = br.readLine()) != null) {
                books.add(Book.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading books.");
            e.printStackTrace();
        }
    }

    public static void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
                bw.write(b.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books.");
            e.printStackTrace();
        }
    }

    public static void addBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        int id = books.isEmpty() ? 1 : books.get(books.size() - 1).id + 1;
        books.add(new Book(id, title, author, true));
        saveBooks();
        System.out.println("Book added.");
    }

    public static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book b : books) {
            System.out.printf("ID: %d | %s by %s | Available: %b\n", b.id, b.title, b.author, b.available);
        }
    }

    public static void borrowBook() {
        System.out.print("Enter Book ID to borrow: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        for (Book b : books) {
            if (b.id == id && b.available) {
                b.available = false;
                saveBooks();
                System.out.println("Book borrowed.");
                return;
            }
        }
        System.out.println("Book not available or ID invalid.");
    }

    public static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        for (Book b : books) {
            if (b.id == id && !b.available) {
                b.available = true;
                saveBooks();
                System.out.println("Book returned.");
                return;
            }
        }
        System.out.println("Invalid Book ID.");
    }
}

