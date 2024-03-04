package ring.runnables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import ring.entity.ClientServer;
import ring.object.Message;

public class ImpServer implements Runnable{
    
    ImpClient nextClient;
    Socket client;
    boolean connection = true;

    //input and output streams
    ObjectInputStream in;
    ObjectOutputStream out;
    
    public ImpServer(Socket client, ImpClient nextClient) {
        this.client = client;
        this.nextClient = nextClient;
    }

    @Override
    public void run(){
        String receivedMessage;
        System.out.println("Connected to client: " + client.getLocalAddress());

        try{
            in = new ObjectInputStream(client.getInputStream());
            while(connection){

                Message msg = (Message) in.readObject();
                receivedMessage = msg.getMsg();
                String clientId = msg.getSender();
                String castType = msg.getType();

                // Unicast
                if(castType.equals("unicast") && clientId.equals(ClientServer.id)) {
                    System.out.println("================================================================");
                    System.out.println("Message recieved by " + ClientServer.id + ": " + receivedMessage);
                    System.out.println("================================================================");
                } else if (castType.equals("unicast") && !clientId.equals(ClientServer.id)) {
                    System.out.println("Forwarding Message for " + clientId + "...");
                    nextClient.out.writeObject(msg);
                    nextClient.out.flush();
                }
                
                // Broadcast
                if(castType.equals("broadcast") && !ClientServer.id.equals(clientId)) {
                    System.out.println("================================================================");
                    System.out.println("Message Broadcast from " + clientId + ": " + receivedMessage);
                    System.out.println("================================================================");
                    nextClient.out.writeObject(msg);
                    nextClient.out.flush();
                }
                
                // sair do loop
                if(receivedMessage.equalsIgnoreCase("exit")) {
                    connection = false;
                }

            }

            //fechando conexões
            out.close();
            in.close();
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
