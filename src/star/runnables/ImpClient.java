package star.runnables;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import star.entity.ClientServer;

public class ImpClient implements Runnable {

    public static Socket client;
    private boolean connection = true;
    public static PrintStream ps;

    public ImpClient(Socket c) {
        ImpClient.client = c;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client S" + ClientServer.id + " connected to server");

            Scanner input = new Scanner(System.in);

            ps = new PrintStream(client.getOutputStream());

            String msg;
            String tp;
            String sd;
            String finalMsg;
            boolean noProblems = true;

            while (connection) {

                //input do usuário
                System.out.println("Enter message: ");
                msg = input.nextLine();

                System.out.println("Enter message type: ");
                tp = input.nextLine();

                System.out.println("Enter sender: ");
                sd = input.nextLine();

                finalMsg = msg + "-" + tp + "-" + sd + "-" + ClientServer.id;

                //sair
                if (finalMsg.equalsIgnoreCase("exit")) {
                    connection = false;
                } else {

                    while (!tp.equals("unicast") && noProblems) {

                        if (tp.equals("broadcast")) {
                            noProblems = false;
                        } else {
                            System.out.println("Incorrect input, please try again: ");

                            System.out.println("Enter message: ");
                            msg = input.nextLine();

                            System.out.println("Enter message type: ");
                            tp = input.nextLine();

                            System.out.println("Enter sender: ");
                            sd = input.nextLine();

                            finalMsg = msg + "-" + tp + "-" + sd + "-" + ClientServer.id;

                        }

                    }

                    System.out.println("Message SENT!");
                    ps.println(finalMsg);
                    noProblems = true;

                }

            }

            input.close();
            ps.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
