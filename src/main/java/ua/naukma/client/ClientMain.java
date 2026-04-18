package ua.naukma.client;

import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.naukma.client.ui.MenuContext;
import ua.naukma.client.ui.MenuLevel;
import ua.naukma.client.ui.NewMenu;
import ua.naukma.client.utils.SystemUserVerificator;
import ua.naukma.domain.*;
import ua.naukma.network.Request;
import ua.naukma.network.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientMain {
    private static final Logger log = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 8080), 5000);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (!socket.isClosed() && socket.isConnected()) {
                SystemUser loggedUser = authenticateUser(oos, ois);
                NewMenu.current_level = MenuLevel.MON;
                NewMenu menu = new NewMenu(oos, ois, loggedUser);
                menu.main_menu(socket);
            }
        } catch (IOException e) {
            log.error("Network connection error: {}", e.getMessage(), e);
        }
    }

    private static SystemUser authenticateUser(ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        while (true) {
            String login = SystemUserVerificator.askLogin();
            String password = SystemUserVerificator.askPassword();

            SystemUser credentials = new SystemUser(-1, login, password, null);
            Request loginRequest = new Request(Request.RequestType.LOGIN, credentials, MenuLevel.ADMIN_PANEL);

            try {
                oos.writeObject(loginRequest);
                oos.flush();

                Response response = (Response) ois.readObject();

                if (response.getResponseStatus() == Response.ResponseStatus.SUCCESS) {
                    SystemUser user = (SystemUser) response.getPayload();
                    log.info("Logged in as {}", user.getLogin());
                    return user;
                } else {
                    System.out.println("Invalid login or password. Please try again.");
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("Authentication process interrupted: {}", e.getMessage(), e);
            }
        }
    }
}