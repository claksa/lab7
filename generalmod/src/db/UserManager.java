package db;

import mainlib.User;

public class UserManager implements Util {
    private final UserUtil userUtil = new UserUtil();
    private static UserState userState = UserState.NOT_REGISTERED;


    @Override
    public boolean register(User user) {
        if (userUtil.register(user)){
            userState = UserState.REGISTERED;
            return true;
        }
        return false;
    }

    @Override
    public boolean authorize(User user) {
        if(userUtil.authorize(user)){
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

}
