package ClientSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter IP address, ex: 192.168.1.10: ");
        String address;
        int port;
        try {
            address = sc.nextLine();
            System.out.println("Enter port, ex:8080: ");
            port = sc.nextInt();
            sc.nextLine();
        }
        catch (InputMismatchException im)
        {
            System.out.println("Invalid input, address and port set to defaults!");
            address = "localhost";
            port = 8080;
        }
        try
        {
            Socket client = new Socket(address,port);
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            boolean inProcess = false;

            System.out.println("Enter your username: ");
            String username = sc.nextLine();
            String mesString = "";
            readClientMess thisClient = new readClientMess(inputStream);
            Thread read = new Thread(thisClient);
            read.start();
            int step = 0;
            while(true){
//                String in = inputStream.readUTF();
//                System.out.println(in);
//                if (in.contains("%*"))
//                {
//                    System.out.print(username + " : ");
//                    outputStream.writeUTF(sc.nextLine());
//                    outputStream.flush();
//                }
//                else if (in.contains("&&"))
//                {
//                    System.out.print(username + " : ");
//                    outputStream.writeInt(sc.nextInt());
//                    outputStream.flush();
//                }
//                else{
                    if (mesString.contains("!calc") || inProcess)
                    {
                        inProcess = true;
                        mesString = sc.nextLine();
                        if (step == 0)
                        {
                            outputStream.writeUTF(mesString);
                            outputStream.flush();
                            step++;
                        }
                        else if(step == 1){
                            outputStream.writeInt(Integer.parseInt(mesString));
                            outputStream.flush();
                            step++;
                        }
                        else
                        if (step == 2)
                        {
                            outputStream.writeInt(Integer.parseInt(mesString));
                            outputStream.flush();
                            step = 0;
                            inProcess = false;
                        }

                    }else {
                        mesString = username+" : " + sc.nextLine();

                        outputStream.writeUTF(mesString);
                        outputStream.flush();
                        if (mesString.contains("!exit")) {
                            client.close();
                            outputStream.close();
                            inputStream.close();
                            sc.close();
                            read.stop();
                        }
                    }
                }
//            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
class readClientMess implements Runnable
{
    DataInputStream i;

    public readClientMess(DataInputStream i) {
        this.i = i;
    }

    @Override
    public void run() {
        int count = 0;
        while (true)
        {
            try
            {
                System.out.println(i.readUTF());
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                count++;
                if (count >10) System.exit(1);
            }
        }
    }
}