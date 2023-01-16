import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Hello World");

        Scanner sc = new Scanner(System.in);

        String readLine = sc.nextLine();
        System.out.println(validateCPF(parseCPF(readLine)));
    }

    //Filter CPF special characters
    public static List<Integer> parseCPF(String inputCPf) {
        inputCPf = inputCPf.replaceAll("[^0-9]", "");
        System.out.println(inputCPf);
        ArrayList<Integer> parsedCPF = new ArrayList<Integer>();

        for (char digit: inputCPf.toCharArray()) {
            parsedCPF.add(Integer.valueOf(Character.toString(digit)));
        }
        return parsedCPF;
    }

    public static boolean validateCPF(List<Integer> parsedCPF) {
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
