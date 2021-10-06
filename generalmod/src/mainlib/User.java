package mainlib;

import db.UserAct;

import java.io.Serializable;

public class User  implements Serializable {
    private final String username;
    private final String password;
    private String salt;
    private final UserAct userAct;

    public User(String username,String password, UserAct userAct){
        this.username = username;
        this.password = password;
        this.userAct = userAct;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public UserAct getUserAct() {
        return userAct;
    }
}
