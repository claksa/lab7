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

public class UserUtil implements Util {
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
                preparedStatement.setString(2, hashPassword(user));
                preparedStatement.setString(3, user.getSalt());
                if (preparedStatement.executeUpdate() != 0) {
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
        String salt = getUserSalt(user);
        if (salt!= null){
            user.setSalt(salt);
            if (checkHashed(user.getSalt())) {
                System.out.println("user with such salt found in the system");
                System.out.println("salt: " + user.getSalt());
            }
            return true;
        }
        return false;
    }

    public ResultSet getUser(User user) {
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

    private String getUserSalt(User user){
        String sql = "SELECT * FROM users";
        String salt = null;
        if (checkUser(user)){
            Statement st;
            ResultSet resultSet;
            try{
                st = database.connection.createStatement();
                resultSet = st.executeQuery(sql);
                while (resultSet.next()){
                    salt = resultSet.getString("salt");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return salt;
    }

    public boolean checkUser(User user) {
        String statement = "SELECT * FROM users WHERE username=?";
        ResultSet resultSet = null;
        int count = 0;
        try {
            PreparedStatement preparedStatement = database.connection.prepareStatement(statement);
            preparedStatement.setString(1, user.getUsername());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count > 0;
    }

    public boolean checkHashed(String salt) {
        String sql = "SELECT * FROM users WHERE salt=?";
        ResultSet resultSet = null;
        int count = 0;
        try {
            PreparedStatement preparedStatement = database.connection.prepareStatement(sql);
            preparedStatement.setString(1, salt);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count > 0;
    }


    private String hashPassword(User user) {
        MessageDigest digest;
        String hashPassword = null;
        try {
            digest = MessageDigest.getInstance(ALG);
            byte[] generatedSalt = generateSalt(4);
            user.setSalt(getStringSalt(generatedSalt));
//            usersSalt = getStringSalt(generatedSalt);
            digest.update(generatedSalt);
            byte[] hash = digest.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
            hashPassword = DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            Reader.PrintErr("no such encryption algorithm: " + ALG);
        }
        return hashPassword;
    }

    private byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        RANDOM.nextBytes(salt);
        return salt;
    }


    public String getStringSalt(byte[] salt) {
        String strSalt = DatatypeConverter.printHexBinary(salt).toLowerCase();
        return strSalt;
    }

}
