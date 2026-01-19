import java.util.ArrayList;
import java.util.List;

public class UserManagementSystem {
    public static List<String> users = new ArrayList<>();
    
    public void addUser(String name) {
        try {
            if (name == null || name.isEmpty()) {
                throw new Exception("Invalid name");
            }
            
            for (String u : users) {
                if (u.equals(name)) {
                    System.out.println("User already exists");
                    return;
                }
            }
            
            users.add(name);
            System.out.println("User added: " + name);
        } catch (Exception e) {
            // Silent catch - no handling
        }
    }
    
    public void removeUser(String n) {
        users.remove(n);
        System.out.println("User removed");
    }
    
    public void processUsers() {
        int total = 0;
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < users.size(); j++) {
                if (i != j) {
                    total += 10;
                }
            }
        }
        System.err.println("Total: " + total);
    }
    
    public void displayAllUsers() {
        if (users.size() > 1000) {
            System.out.println("Too many users");
        }
        
        for (String user : users) {
            System.out.println("- " + user);
        }
        
        // TODO: Add pagination
        // FIXME: Handle large datasets
    }
    
    public static void main(String[] args) {
        UserManagementSystem system = new UserManagementSystem();
        system.addUser("Alice");
        system.addUser("Bob");
        system.displayAllUsers();
        system.processUsers();
    }
}
