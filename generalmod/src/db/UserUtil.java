package db;

import mainlib.Reader;
import server.Server;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Scanner;

public class UserUtil implements Util {
    private String salt;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALG = "SHA-256";


    @Override
    public boolean register(User user) {
        String statement = "INSERT INTO users (username,password,salt) VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = Server.getDatabase().getConnection().prepareStatement(statement);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,hashPassword(user.getPassword()));
            user.setSalt(salt);
            preparedStatement.setString(3,salt);
            if (preparedStatement.executeUpdate()!= 0 ){
                user.setSalt(getSalt());
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean authorize(User user) {
        boolean isAddedToTheDB = false;
        if (checkUser(user)){
            System.out.println("You are found in the system!");
            isAddedToTheDB = true;
        } else {
            System.out.println("If you want to register with the entered data, enter yes. If you want to log out, enter no");
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine().equals("yes")){
                isAddedToTheDB = register(user); //типа сразу регистрирует и работает как с авториз-м пользователем
            } else if (scanner.nextLine().equals("no")){
                System.exit(0); //на случай если пользователя всё достало, и он решил пойти пить чаёк
            }
        }
        return isAddedToTheDB;
    }

    public ResultSet getUser(User user){
        String statement = "SELECT * FROM users WHERE username = ? AND PASSWORD = ? AND SALT = ?";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = Server.getDatabase().getConnection().prepareStatement(statement);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, hashPassword(user.getPassword()));
            preparedStatement.setString(3, user.getSalt());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public boolean checkUser(User user){
        ResultSet resultSet = getUser(user);
        int count = 0;
        while (true){
            try {
                if (!resultSet.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            count++;
        }
        return count > 0;
    }

    private String hashPassword(String password) {
        MessageDigest digest;
        String hashPassword =  null;
        try {
            digest = MessageDigest.getInstance(ALG);
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            this.salt = Base64.getEncoder().encodeToString(salt);
            this.salt = DatatypeConverter.printHexBinary(salt).toLowerCase();
//            hashPassword = Base64.getEncoder().encodeToString(hash);
            hashPassword = DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            Reader.PrintErr("no such encryption algorithm: " + ALG);
        }
        return hashPassword;
    }


    public String getSalt() {
        return salt;
    }

}
