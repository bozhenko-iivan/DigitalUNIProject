package ua.naukma.client;

import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.ui.NewMenu;
import ua.naukma.client.utils.SystemUserVerificator;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientMain {
    public static void main(String[] args) {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("255.243.123.134", 8080), 5000);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


            while (true) {
                SystemUser loggedUser = null;

                while (true) {
                    System.out.println("Login");
                    String login = SystemUserVerificator.askLogin();
                    String password = SystemUserVerificator.askPassword();

                    SystemUser credentials = new SystemUser(-1, login, password, null);

                    Request loginRequest = new Request(Request.RequestType.LOGIN, credentials);
                    oos.writeObject(loginRequest);
                    oos.flush();

                    Response response = (Response) ois.readObject();

                    if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                        loggedUser = (SystemUser) response.getPayload();
                        System.out.println("Login successful! Welcome, " + loggedUser.getLogin());
                        break;
                    } else {
                        System.out.println("Error: " + response.getMsg());
                        System.out.println("Please try again.");
                    }
                }

            NewMenu.current_level = MenuLevel.MON;

            NewMenu menu = new NewMenu(oos, ois, loggedUser);
            menu.main_menu();
        }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}