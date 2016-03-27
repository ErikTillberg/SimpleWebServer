package servertest;
import java.io.*;
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
            String fileName = "temp.txt";
            String line = null;
            String body = "";
            
           try {
           
            FileReader fileReader = 
                new FileReader(fileName);

           
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                body += line;
            }   

          
        bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "File cannont be opened '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
            
        }
           
           
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