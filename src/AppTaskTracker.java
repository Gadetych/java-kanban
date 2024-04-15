import http.HttpTaskServer;

public class AppTaskTracker {
    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
