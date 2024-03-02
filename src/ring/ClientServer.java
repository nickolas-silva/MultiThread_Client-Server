package ring;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import ring.runnables.ImpClient;
import ring.runnables.ImpServer;

public class ClientServer {
    ServerSocket server;
    Socket clientConnected;
    Socket client;
    String ip;
    int port;
    int nextPort;
    public static String id;

    public ClientServer(String ip, int port, int nextPort, String id) {
        this.ip = ip;
        this.port = port;
        this.nextPort = nextPort;
        ClientServer.id = id;
        onInit();
    }

    private void onInit(){
        try{
            //init server
            server = new ServerSocket(port);
            System.out.println("Server started at port " + port);
            System.out.println("Waiting for a client...");

            @SuppressWarnings("resource")
            Scanner input = new Scanner(System.in);
            input.nextLine();

            //init client
            client = new Socket(ip, nextPort);
            System.out.println("Connected to server at port " + nextPort);
            ImpClient impClient = new ImpClient(client);
            Thread ct = new Thread(impClient);
            ct.start();

            clientConnected = server.accept();

            ImpServer impServer = new ImpServer(clientConnected, impClient);
            Thread st = new Thread(impServer);
            st.start();


        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
