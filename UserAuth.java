import java.io.*;
public class UserAuth {
    private static final String FILE_NAME = "users.csv";

    public static boolean login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Login file error: " + e.getMessage());
        }
        return false;
    }

    public static boolean register(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    System.out.println("Username already exists.");
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println("Error checking users: " + e.getMessage());
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(username + "," + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
        return false;
    }
}

