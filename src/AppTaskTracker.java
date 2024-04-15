import http.HttpTaskServer;
import service.Managers;

public class AppTaskTracker {
    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
    }
}
