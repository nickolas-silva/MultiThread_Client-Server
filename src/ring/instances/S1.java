package ring.instances;

import ring.ClientServer;

public class S1 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.1", 5001, 5002, "P1");
    }
}
