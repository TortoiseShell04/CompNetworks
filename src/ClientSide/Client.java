package ClientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try
        {
            Socket client = new Socket("localhost",8697);
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

            System.out.println("Enter your username: ");
            String username = sc.nextLine();
            String mesString = "";
            while(true){
            String in = inputStream.readUTF();
            System.out.println(in);
            if (in.contains("%*"))
            {
                System.out.print(username + " : ");
                outputStream.writeUTF(sc.nextLine());
                outputStream.flush();
            }
            else if (in.contains("&&"))
            {
                System.out.print(username + " : ");
                outputStream.writeInt(sc.nextInt());
                outputStream.flush();
            }
            else{
            System.out.print(username + " : ");
            mesString = username+" : " + sc.nextLine();
            outputStream.writeUTF(mesString);
            outputStream.flush();
            if (mesString.contains("!exit"))
            {
                client.close();
                outputStream.close();
                inputStream.close();
                sc.close();
            }
            }
        }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}