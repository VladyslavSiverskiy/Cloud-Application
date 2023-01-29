package com.vsivercloud.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTools {

    private static Connection connection;
    public static void setConnection(Connection connection) {
        DBTools.connection = connection;
    }

    public static boolean isExist(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM profiles WHERE user_email = ?");
        preparedStatement.setString(1, email);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()) return true; //if user with such email exist return true
        return false;
    }

    public static boolean registerUser(String email, String password){
        String username = email.substring(0, 5) + String.valueOf(email.hashCode()).substring(0,5);
        username = username.replaceAll("[^A-Za-z0-9]","_");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO profiles(user_name, user_email, user_password) VALUES (?,?,?);");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            preparedStatement.execute();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getUser(String email, String password) throws SQLException {
        String answer = "FALSE";
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM profiles WHERE user_email = ? AND user_password = ?");
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,password);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            answer = rs.getString("user_name");
        }
        return answer;
    }
}
