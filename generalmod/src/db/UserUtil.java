package db;

import mainlib.Reader;
import mainlib.User;
import server.Server;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import java.util.Scanner;

public class UserUtil implements Util {
    private String salt;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALG = "SHA-512";
    Database database = Server.getDatabase();



    @Override
    public boolean register(User user) {
        String statement = "INSERT INTO users (username,password,salt) VALUES(?,?,?)";
        try {
            if (!checkUser(user)) {
                PreparedStatement preparedStatement = database.connection.prepareStatement(statement);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, hashPassword(user.getPassword()));
                user.setSalt(salt);
                preparedStatement.setString(3, salt);
                if (preparedStatement.executeUpdate() != 0) {
                    user.setSalt(getSalt());
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean authorize(User user) {
        if (checkUser(user)) {
            System.out.println("You are found in the system!");
             return true;
        }
        return false;
    }

    public ResultSet getUser(User user){
        String statement = "SELECT * FROM users WHERE username = ? AND PASSWORD = ? AND SALT = ?";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = database.connection.prepareStatement(statement);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getSalt());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public boolean checkUser(User user){
        String statement = "SELECT * FROM users WHERE username=?";
        ResultSet resultSet = null;
        int count = 0;
        try {
            PreparedStatement preparedStatement = database.connection.prepareStatement(statement);
            preparedStatement.setString(1,user.getUsername());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
            this.salt = DatatypeConverter.printHexBinary(salt).toLowerCase();
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
