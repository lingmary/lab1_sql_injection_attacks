package com.ias101.lab1.security;

import com.ias101.lab1.database.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

/**
 * Authentication class for user validation
 */
public class Authenticator {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


    private static  String sanitize(String input) {
        if (input == null) {
            return "";
        }

        return input.replace("'", "''");
    }

    public static boolean authenticateUser(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        if (!isValidPassword(password)) {
            System.out.println("Invalid password format.");
            return false;
        }

        username = sanitize(username);
        password = sanitize(password);

        try (var conn = DBUtil.connect("jdbc:sqlite:src/main/resources/database/sample.db",
                "root", "root")) {
            try (var statement = conn.createStatement()) {
                var query = "SELECT * FROM user_data WHERE username = '" + username + "'AND password = '" + password + "'";
                System.out.println(query);
                ResultSet rs = statement.executeQuery(query);

                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    // Validate password with regex
    private static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();



    }
}