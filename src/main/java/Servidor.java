import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        //Validate if CPF is bigger os smaller than 11
        if (parsedCPF.size() != 11) {
            return false;
        }

        //Validate if CPF contains zeros
        boolean zeroValidationFlag = true;
        for(int i = 10; i >= 0; i--) {
            if (parsedCPF.get(i) != 0) {
                zeroValidationFlag = false;
                break;
            }
        }
        if(zeroValidationFlag) {return false;}

       //FIRST DIGIT OPERATIONS
        int calculatedVerifDigit1 = calculateVerifDigit(parsedCPF, 1);
        //Validate first digit
        if(parsedCPF.get(9) != calculatedVerifDigit1) {
            return false;
        }

        //SECOND DIGIT OPERATIONS
        int calculatedVerifDigit2 = calculateVerifDigit(parsedCPF, 2);
        //Validate second digit
        if(parsedCPF.get(10) != calculatedVerifDigit2) {
            return false;
        }
        return true;
    }

    //digit should be 1 or 2
    private static int calculateVerifDigit(List<Integer> parsedCPF, int digit) {
        //Obtaining the module of the calculation
        int digitsCalculation = 0;
        for(int i = (9 + digit); i >= 2; i--) {
            digitsCalculation += parsedCPF.get((9 + digit)-i)*i;
        }
        double moduleCalculation = digitsCalculation % 11;
        //Obtaining the calculated first verification digit
        int calculatedVerifDigit;
        if(moduleCalculation == 0 || moduleCalculation == 1) {
            calculatedVerifDigit = 0;
        } else {
            calculatedVerifDigit = (int) (11 - moduleCalculation);
        }

        return calculatedVerifDigit;
    }
}
