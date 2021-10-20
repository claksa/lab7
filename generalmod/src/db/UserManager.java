package db;

import mainlib.User;


public class UserManager implements Util {
    private final UserUtil userUtil = new UserUtil();
    private static String name;
    private static UserState userState = UserState.NOT_REGISTERED;
    private static boolean isAuthorized = false;


    @Override
    public boolean register(User user) {
        isAuthorized = userUtil.register(user);
        if (isAuthorized) {
            userState = UserState.AUTHORIZED;
            name = user.getUsername();
            return true;
        }
        return false;
    }

    @Override
    public boolean authorize(User user) {
        isAuthorized = userUtil.authorize(user);
        if (isAuthorized) {
            userState = UserState.AUTHORIZED;
            name = user.getUsername();
            return true;
        }
        return false;
    }

    public static UserState getUserState() {
        return userState;
    }

    public String getName() {
        return name;
    }

    public static boolean isAuthorized() {
        return isAuthorized;
    }
}
