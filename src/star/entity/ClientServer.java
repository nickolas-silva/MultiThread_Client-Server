package star.entity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import star.runnables.ImpClient;
import star.runnables.ImpServer;

public class ClientServer {

    public static int id;
    ServerSocket server;
    Socket clientConnected;
    Socket client;
    String ip;
    int port;
    int nextPort;

    public ClientServer(String ip, int port, int nextPort, int id) {
        this.ip = ip;
        this.port = port;
        this.nextPort = nextPort;
        ClientServer.id = id;
        onInit();
    }

    private void onInit() {
        try {
            //init server
            server = new ServerSocket(port);
            System.out.println("Server started at port " + port);

            //init client
            client = new Socket(ip,nextPort);
            ImpClient ch = new ImpClient(client);
            Thread tc = new Thread(ch);
            tc.start();

            while(true) {
                clientConnected = server.accept();
                ImpServer sh = new ImpServer(clientConnected);
                Thread ts = new Thread(sh);
                ts.start();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}