package com.vsivercloud.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    //set number of port
    public static final int PORT_NUMBER = 23333;

    //run server
    public static void start(){
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server is working on port " + serverSocket.getLocalPort());

            ExecutorService executorService = Executors.newFixedThreadPool(8);
            while (true){
                //У циклі створюємо нову сесію для кожного нового клієнта
                executorService.execute(new Session(serverSocket.accept()));
                System.out.println("Connection...");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with connection...");
            e.printStackTrace();
        }
    }
}
