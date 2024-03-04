package star.runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import star.entity.ClientServer;

public class ImpServer implements Runnable {

    public static List<Socket> clients = new ArrayList<>();
    private Socket client;
    private boolean connection = true;
    private Scanner input = null;

    public ImpServer(Socket c) {
        this.client = c;
        clients.add(c);
    }

    @Override
    public void run() {

        String receivedMessage;

        try {

            input = new Scanner(client.getInputStream());

            //Message
            String separetedMsg[];
            String castType;
            String msg;
            int clientId;
            String sender;

            Socket destiny = null;
            PrintStream printer = null;

            while (connection) {

                //descompressing message
                receivedMessage = input.nextLine();
                separetedMsg = receivedMessage.split("-");
                msg = separetedMsg[0];
                castType = separetedMsg[1];
                clientId = Integer.parseInt(separetedMsg[2]);
                sender = separetedMsg[3];

                //unicast
                if (clientId == ClientServer.id && castType.equals("unicast")) {
                    System.out.println("================================================================");
                    System.out.println("Message unicast recived from S" + sender + ": " + msg);
                    System.out.println("================================================================");
                } else if (clientId != 999 && castType.equals("unicast")) {
                    System.out.println("---------------------->");
                    System.out.println("Forwarding Message from S" + sender + " to S" + clientId + "...");

                    destiny = new Socket("localhost", 3030 + clientId);
                    printer = new PrintStream(destiny.getOutputStream());
                    printer.println(receivedMessage);

                }

                //broadcast
                if (castType.equals("broadcast")) {

                    if (ClientServer.id != 1) {
                        System.out.println("================================================================");
                        System.out.println("Message Broadcast from S" + separetedMsg[separetedMsg.length - 1] + ": " + msg);
                        System.out.println("================================================================");
                    } else {

                        for (int i = 1; i <= clients.size(); i++) {

                            if (ClientServer.id == 1 && i == 1 && clientId != 1) {
                                System.out.println("================================================================");
                                System.out.println(
                                        "Message Broadcast from S" + separetedMsg[separetedMsg.length - 1] + ": " + msg);
                                System.out.println("================================================================");
                            } else if (i != clientId) {
                                destiny = new Socket("localhost", 3030 + i);
                                printer = new PrintStream(destiny.getOutputStream());
                                printer.println(receivedMessage);
                            }
                        }
                    }
                }
                //sair
                if (receivedMessage.equalsIgnoreCase("exit"))  connection = false;
            }
            input.close();
            destiny.close();
            printer.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
