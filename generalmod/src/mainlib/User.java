package mainlib;

import db.UserAct;

import java.io.Serializable;

public class User  implements Serializable {
    private final String username;
    private String password;
    private final UserAct userAct;

    public User(String username,String password, UserAct userAct){
        this.username = username;
        this.password = password;
        this.userAct = userAct;
    }

    public synchronized String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public UserAct getUserAct() {
        return userAct;
    }
}
