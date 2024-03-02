package ring.instances;

import ring.ClientServer;

public class S2 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.2",5002, 5003,"P2");
    }
}
