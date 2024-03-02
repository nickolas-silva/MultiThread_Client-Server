package ring.instances;

import ring.ClientServer;

public class S3 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.3",5003, 5004, "P3");
    }
}
