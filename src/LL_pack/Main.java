package LL_pack;

import java.security.InvalidParameterException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private Grammar grammar;
    private Scanner sc;

    public Main(Grammar grammar) {
        this.grammar = grammar;
        this.sc = new Scanner(System.in);
    }

    public static void main(String[] args) {

        Main main = new Main(new Grammar());
        String line;
        while (true) {
            try {
                System.out.print("Enter K: ");
                main.grammar.setK(main.sc.nextInt());
                main.sc.nextLine();
                break;
            } catch (InputMismatchException exp) {
                System.out.println("Please enter a number!");
            }
        }
        System.out.println("K: " + main.grammar.getK());
        System.out.println("Enter grammar productions(type 'exit' to stop): ");
        System.out.println("E.g: A->asd|tre|atr");
        while((line = main.sc.nextLine()).toLowerCase().indexOf("exit") == -1){
            try{
                main.grammar.addProduction(Production.parse(line));
            }catch (InvalidParameterException exp){
                System.out.println(exp.getMessage());
            }
            System.out.println("Current grammar:\n" + main.grammar);
        }

        System.out.println("First: " + main.grammar.first("A"));
        main.sc.close();
    }
}
