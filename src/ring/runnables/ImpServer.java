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
                
                @SuppressWarnings({ "unchecked", "rawtypes" })
                Message<String> msg = (Message) in.readObject();
                receivedMessage = msg.getMsg();
                System.out.println("Mensagem recebida: " + receivedMessage);

                //logica para descompactar a mensagem
                // Separa o id do cliente da mensagem
                String[] splitMsg = receivedMessage.split("/");
                String clientId = splitMsg[2];
                String castType = splitMsg[1];
                String finalMsg = splitMsg[0];

                // Caso a mensagem deva ser recebida por este id
                if(clientId.equals(ClientServer.id) && castType.equals("unicast")) {
                    System.out.println("Mensagem recebida de " + splitMsg[3] + ": " + finalMsg);
                }
                // Caso a mensagem deva ser encaminhada para outro id
                else if(!clientId.equals("anything") && castType.equals("unicast")){
                    System.out.println("Encaminhando Mensagem...");
                    nextClient.out.writeObject(msg);
                    nextClient.out.flush();
                }

                // Caso a mensagem seja broadcast
                if(castType.equals("broadcast") && !ClientServer.id.equals(clientId)) {
                    System.out.println("Mensagem Broadcast de " + splitMsg[splitMsg.length - 1] + ": " + finalMsg);
                    nextClient.out.writeObject(msg);
                    nextClient.out.flush();
                }
                
                // Caso queira finalizar a conexão
                if(receivedMessage.equalsIgnoreCase("fim")) {
                    connection = false;
                }


            }

            out.close();
            in.close();
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
