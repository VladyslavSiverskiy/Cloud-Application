package com.vsivercloud;

import com.vsivercloud.utils.DBTools;
import com.vsivercloud.utils.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args){
        run();
    }
    public static void run(){
        try (Connection connection = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD)){
            DBTools.setConnection(connection);
            Server.start();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problem with db connection...");
            System.exit(0);
        }
    }
}
