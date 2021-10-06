package db;

import mainlib.User;

public interface Util {
    boolean register(User user);
    boolean authorize(User user);
}
