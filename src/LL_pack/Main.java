package LL_pack;

import java.security.InvalidParameterException;
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
        System.out.println("Enter grammar productions(type 'exit' to stop): ");
        System.out.println("E.g: A->asd|tre|atr");
        while((line = main.sc.nextLine()).toLowerCase().indexOf("exit")==-1){
//            System.out.println("Line: " + line);
            try{
                main.grammar.addProduction(Production.parse(line));
            }catch (InvalidParameterException exp){
                System.out.println(exp.getMessage());
            }

            System.out.println("Current grammar:\n" + main.grammar);
        }
    }
}
