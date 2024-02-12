import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port){
        return new EchoClient("127.0.0.1", port); // localhost
    }

    public void run(){
        System.out.println("Type 'bye' to leave. \n\n\n");

        try(var socket = new Socket(host, port)){
            var scanner = new Scanner(System.in, "UTF-8");
            var writer = new PrintWriter(socket.getOutputStream(), true);

            var responseScanner = new Scanner(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            while(true){
                if(scanner.hasNextLine()){
                    var message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();

                    if("bye".equalsIgnoreCase(message)){
                        return;
                    }

                    if(responseScanner.hasNextLine()){
                        var response = responseScanner.nextLine();
                        System.out.println("Server responded with: " + response);
                    }
                }
            }
        } catch (NoSuchElementException e){
            System.out.println("Connection dropped!");
        } catch (IOException e){
            System.out.printf("Can't connect to %s:%s%n", host, port);
            e.printStackTrace();
        }
    }
}
