package ring.instances;

import ring.entity.ClientServer;

public class S3 {
    public static void main(String[] args) {
        new ClientServer("127.0.0.3",3032, 3033, "S3");
    }
}
