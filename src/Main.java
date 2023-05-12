import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args)
    {
        try{
            ServerSocket server = new ServerSocket(8697);
            while (true)
            {
                Socket newClient = server.accept();
                System.out.println("New client entered!!");

                ClientSocket newC = new ClientSocket(newClient);

                new Thread(newC).start();
            }

        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }


}

class ClientSocket implements Runnable
{
    private final Socket thisCleint;

    public ClientSocket(Socket thisCleint) {
        this.thisCleint = thisCleint;
    }


    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(thisCleint.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(thisCleint.getOutputStream());
            outputStream.writeUTF("Server booted at port 8697");
            while (true) {
                String message = inputStream.readUTF();
                if (message.contains("!exit")) {
                    System.exit(0);
                    thisCleint.close();
                    outputStream.close();
                    inputStream.close();
                }
                if (message.contains("!calc")) {
                    calc(inputStream, outputStream);
                } else {
                    System.out.println(message);
                    outputStream.writeUTF(message);
                    outputStream.flush();
                }
            }

        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public void calc(DataInputStream i, DataOutputStream o)
    {
        try{
            o.writeUTF("%*Enter an opertaion: ");
            o.flush();
            String opp = i.readUTF();
            o.writeUTF("&&Enter first number: ");
            o.flush();
            int num1 = i.readInt();
            o.writeUTF("&&Enter second number: ");
            o.flush();
            int num2 = i.readInt();


            if (opp.contains("+")) {
                o.writeUTF(String.valueOf(num1 + num2));
                System.out.println(num1 + num2);
                o.flush();
            }
            else
            if (opp.contains("-")){
                o.writeUTF(String.valueOf(num1 - num2));
                System.out.println(num1-num2);
                o.flush();
            }
            else if (opp.contains("/")) {
                o.writeUTF(String.valueOf((double) num1 / (double) num2));
                System.out.println((double) num1 / (double) num2);
                o.flush();
            }
            else if (opp.contains("*")) {
                o.writeUTF(String.valueOf(num1 * num2));
                System.out.println(num1*num2);
                o.flush();
            }
            else if (opp.contains("^")) {
                o.writeUTF(String.valueOf(Math.pow(num1, num2)));
                System.out.println(Math.pow(num1, num2));
                o.flush();
            }
            else {
                o.writeUTF("Invalid");
                o.flush();
            }
        }
        catch(IOException io){
            return;
        }
    }
}