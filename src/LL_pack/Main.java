package LL_pack;

import javax.xml.stream.events.Characters;
import java.util.Arrays;
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
                main.sc.nextLine();
            }
        }

        System.out.println("K: " + main.grammar.getK());
//        System.out.println("Enter grammar productions(type 'exit' to stop): ");
//        System.out.println("E.g: A->asd|tre|atr");
//        while((line = main.sc.nextLine()).toLowerCase().indexOf("exit") == -1) {
//            try{
//                main.grammar.addProduction(Production.parse(line));
//            }catch (InvalidParameterException exp){
//                System.out.println(exp.getMessage());
//            }
//            System.out.println("Current grammar:\n" + main.grammar);
//        }

        /**TEMPORARY LINE READ*/
        main.grammar.addProduction(Production.parse("S->aSB|bA"));
        main.grammar.addProduction(Production.parse("A->aBb|~"));
        main.grammar.addProduction(Production.parse("B->bB|ba"));
        /**********************/



//        System.out.println("First: " + main.grammar.first("bA"));

//        System.out.println("Follow: " + main.grammar.follow('B'));

        main.grammar.populateFollowTable();
        System.out.println("Follow table: " + main.grammar.getFollowTable());

        System.out.println("Start building parse table");
        main.grammar.buildParseTable();


        char firstNonTerm;
        while (true) {
            System.out.print("First production character: ");
            firstNonTerm = main.sc.nextLine().charAt(0);
            if(Character.isAlphabetic(firstNonTerm)){
                firstNonTerm = Character.toUpperCase(firstNonTerm);
                break;
            }else{
                System.out.println("Please enter a letter!");
            }
        }

        String mainInput = null;
        while (true) {
            System.out.print("Input: ");
            mainInput = main.sc.nextLine().trim();
            if(mainInput.length() == 0) {
                System.out.println("Please enter a valid input!");
            } else
                break;
        }

        main.grammar.parseString(firstNonTerm, mainInput);

        main.sc.close();
    }
}
