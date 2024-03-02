package ring.runnables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import ring.ClientServer;
import ring.Message;

public class ImpClient  implements Runnable{
    private Socket client;
    private boolean connection = true; //colocar como true e tirar do construtor talvez

    //object to send
    private Message<String> msg;

    //input and output streams
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public ImpClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            
            System.out.println("Client connected to server");
            
            Scanner input = new Scanner(System.in);

            String message;

            while (connection) {
                System.out.println("Enter message: ");

                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());

                message = input.nextLine();
                message = message.concat("/" + ClientServer.id);

                if (message.equalsIgnoreCase("exit")) {
                    connection = false;
                } else{
                    msg = new Message<String>(message);
                    out.writeObject(msg);
                    out.flush();
                    System.out.println("Message SENT!");
                }
            }

            input.close();
            in.close();
            out.close();
            client.close();
            System.out.println("Client disconnected from server...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClient() {
        return this.client;
    }


    
}
