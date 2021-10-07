package db;

import mainlib.User;

import java.util.Scanner;

public class UserManager implements Util {
    private final UserUtil userUtil = new UserUtil();
    private static UserState userState = UserState.NOT_REGISTERED;
    private static boolean isAuthorized = false;


    @Override
    public boolean register(User user) {
        isAuthorized = userUtil.register(user);
        if (isAuthorized) {
            userState = UserState.REGISTERED;
            return true;
        }
        return false;
    }

    @Override
    public boolean authorize(User user) {
        isAuthorized = userUtil.authorize(user);
        if (isAuthorized) {
            userState = UserState.AUTHORIZED;
            return true;
        }
        return false;
    }

    public UserUtil getUserUtil() {
        return userUtil;
    }

    public static UserState getUserState() {
        return userState;
    }

    public static boolean isAuthorized() {
        return isAuthorized;
    }
}
