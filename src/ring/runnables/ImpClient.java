package ring.runnables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

//import ring.entity.ClientServer;
import ring.object.Message;

public class ImpClient  implements Runnable{
    private Socket client;
    private boolean connection = true; 

    //object to send
    private Message msg;

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
            out = new ObjectOutputStream(client.getOutputStream());

            String message;
            String tp;
            String sd;

            while (connection) {

                System.out.println("Enter message: ");


                message = input.nextLine();

                System.out.println("Enter message type: ");
                tp = input.nextLine();

                System.out.println("Enter sender: ");
                sd = input.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    connection = false;
                } else{
                    msg = new Message(message, tp, sd);
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
