
package servertest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.Executor;

public class ServerTest {

    public static void main(String[] args) throws Exception {
        InetSocketAddress addr = new InetSocketAddress(8000);
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/servertest", new RequestHandler());
        server.setExecutor(new MultiThreadingExecutor()); // creates a default executor
        server.start();
    }

    static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println(t);
            
            String body = "<HTML><TITLE>Java Server</TITLE>This web page was sent by our simple <B>Java Server</B></HTML>";
            body += "<script>alert('hello');</script>";
            String response =  body;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}

class MultiThreadingExecutor implements Executor {
     public void execute(Runnable r) {
         new Thread(r).start();
     }
 }