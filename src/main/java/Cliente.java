import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String args[]) throws IOException {
        Socket connection = new Socket("127.0.0.1", 5555);
        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        DataInputStream input = new DataInputStream(connection.getInputStream());

        System.out.println("Digite um CPF para verificação: \n");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message = br.readLine();

        //Send the user's data to the server
        output.writeUTF(message);

        //Receive message frm the server
        System.out.println(input.readUTF());

        input.close();
        output.close();
        connection.close();
    }
}
