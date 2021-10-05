import db.User;
import db.UserManager;

import java.util.Scanner;

public class UserConsole {
    private static boolean isAvailableToWork = false;
    static UserManager userManager;


    public static void startWorkWithUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello user! At this point, you have two options:" + "\n" +
                "- register," + "\n" +
                "- log in" + "\n" + "Enter the selected option into the console");
        String option = scanner.nextLine();
        System.out.println("Enter a username: ");
        String username = scanner.nextLine();
        System.out.println(" Enter a password: ");
        String password = scanner.nextLine();
        User user = new User(username, password);
        if (option.equals("register")) {
            userManager = new UserManager();
            if (userManager.register(user)) {
                isAvailableToWork = true;
            }
        } else if (option.equals("log in")) {
            userManager.authorize(user);
        }
    }

    public static boolean isIsAvailableToWork() {
        return isAvailableToWork;
    }
}
