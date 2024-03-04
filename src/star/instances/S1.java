package star.instances;

import star.entity.ClientServer;

public class S1 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.1", 3031, 3031, 1);
    }
}
