import mainlib.User;
import db.UserManager;

import java.util.Scanner;

public class UserConsole  {
    static UserManager userManager;
    Scanner scanner = new Scanner(System.in);

    public boolean startWorkWithUser(String line) {
        boolean isAvailableToWork = false;
        System.out.println("Enter a username: ");
        String username = scanner.nextLine();
        System.out.println(" Enter a password: ");
        String password = scanner.nextLine();
//        User user = new User(username, password);
        userManager = new UserManager();
        if (line.equals("register")) {
//            isAvailableToWork = userManager.register(user);
        } else if (line.equals("log in")) {
//            isAvailableToWork = userManager.authorize(user);
        }
        return isAvailableToWork;
    }

}
