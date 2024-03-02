package ring.instances;

import ring.entity.ClientServer;

public class S1 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.1", 3030, 3031, "S1");
    }
}
