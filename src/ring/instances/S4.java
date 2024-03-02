package ring.instances;

import ring.entity.ClientServer;

public class S4 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.4",3033, 3030, "S4");
    }
}
