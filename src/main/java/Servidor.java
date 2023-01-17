import javax.imageio.IIOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5555);
        System.out.println("A porta 5555 foi aberta.");
        while(true) {
            Socket connection = server.accept();

            DataInputStream input = new DataInputStream(connection.getInputStream());
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());

            String rawCPF = input.readUTF();
            System.out.println(validateCPF(rawCPF));
            if (validateCPF(rawCPF)) {
                output.writeUTF("Este CPF é válido");
            } else {
                output.writeUTF("Este CPF é inválido");
            }

            input.close();
            output.close();
            connection.close();
        }
    }

    //Filter CPF special characters
    private static List<Integer> parseCPF(String inputCPf) {
        inputCPf = inputCPf.replaceAll("[^0-9]", "");
        System.out.println(inputCPf);
        ArrayList<Integer> parsedCPF = new ArrayList<Integer>();

        for (char digit: inputCPf.toCharArray()) {
            parsedCPF.add(Integer.valueOf(Character.toString(digit)));
        }
        return parsedCPF;
    }

    private static boolean validateCPF(String inputCPf) {
        List<Integer> parsedCPF = parseCPF(inputCPf);
        //Validate if CPF is bigger os lower than 11
        if (parsedCPF.size() != 11) {
            return false;
        }

        //Validate if CPF contains zeros

       //FIRST DIGIT OPERATIONS
        //Obtaining the module of the calculation
        int digitsCalculation = 0;
        for(int i = 10; i >= 2; i--) {
            digitsCalculation += parsedCPF.get(10-i)*i;
        }
        double moduleCalculation = digitsCalculation % 11;
        System.out.printf("The module calculated is: %f\n",moduleCalculation);
        //Obtaining the calculated first verification digit
        int calculatedVerifDigit;
        if(moduleCalculation == 0 || moduleCalculation == 1) {
            calculatedVerifDigit = (int) moduleCalculation;
        } else {
            calculatedVerifDigit = (int) (11 - moduleCalculation);
        }
        System.out.printf("The first verification calculated digit is: %d\n\n", calculatedVerifDigit);
        //Validate first digit
        if(parsedCPF.get(9) != calculatedVerifDigit) {
            System.out.println("ENTERING FALSE LOOP" + parsedCPF.get(9) + calculatedVerifDigit);
            return false;
        }

        //SECOND DIGIT OPERATIONS
        //Obtaining new module of the calculation
        digitsCalculation = 0;
        for(int i = 11; i >= 2; i--) {
            digitsCalculation += parsedCPF.get(11-i)*i;
            System.out.printf("Iteration number %d equals to %d\n", 11-i, digitsCalculation);
        }
        moduleCalculation = digitsCalculation % 11;
        //Obtaining the calculated second verification digit
        calculatedVerifDigit = 0;
        if(moduleCalculation == 0 || moduleCalculation == 1) {
            calculatedVerifDigit = (int) moduleCalculation;
        } else {
            calculatedVerifDigit = (int) (11 - moduleCalculation);
        }
        System.out.printf("The second verification calculated digit is: %d\n\n", calculatedVerifDigit);
        //Validate second digit
        if(parsedCPF.get(10) != calculatedVerifDigit) {
            return false;
        }
        return true;
    }
}
