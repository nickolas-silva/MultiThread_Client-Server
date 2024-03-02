package ring.instances;

import ring.ClientServer;

public class S4 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.4",5004, 5001, "P4");
    }
}
