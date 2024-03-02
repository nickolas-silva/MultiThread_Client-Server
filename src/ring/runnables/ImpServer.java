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
            while(connection){

                in = new ObjectInputStream(client.getInputStream());
                out = new ObjectOutputStream(client.getOutputStream());

                Message msg = (Message) in.readObject();
                receivedMessage = msg.getMsg();
                String clientId = msg.getSender();
                String castType = msg.getType();
                // System.out.println("Mensagem recebida: " + receivedMessage);
                // System.out.println("Tipo de mensagem: " + castType);
                // System.out.println("Enviada por: " + clientId);


                // Caso a mensagem deva ser recebida por este id
                if(clientId.equals(ClientServer.id) && castType.equals("unicast")) {
                    System.out.println("Message recieved by " + clientId + ": " + receivedMessage);
                }
                // Caso a mensagem deva ser encaminhada para outro id
                else if(!clientId.equals("anything") && castType.equals("unicast")){
                    System.out.println("Forwarding Message...");
                    nextClient.out.writeObject(msg);
                    nextClient.out.flush();
                }

                // Caso a mensagem seja broadcast
                if(castType.equals("broadcast") && !ClientServer.id.equals(clientId)) {
                    System.out.println("Mensagem Broadcast de " + clientId + ": " + receivedMessage);
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
