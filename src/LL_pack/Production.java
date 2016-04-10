package LL_pack;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Production {

    NonTerminal nonTerminal;
    ArrayList<ArrayList<ProductionElement>> elements;

    public Production(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        this.elements = new ArrayList<ArrayList<ProductionElement>>();
    }

    public Production(NonTerminal nonTerminal, ArrayList<ArrayList<ProductionElement>> elements) {
        this.nonTerminal = nonTerminal;
        this.elements = elements;
    }

    public NonTerminal getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public ArrayList<ArrayList<ProductionElement>> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ArrayList<ProductionElement>> elements) {
        this.elements = elements;
    }

    public static Production parse(String toParse) throws InvalidParameterException{
        String[] mainSplit = toParse.split("->");
        NonTerminal mainVar = new NonTerminal(mainSplit[0].trim().toUpperCase().charAt(0));

        if(mainSplit.length != 2)
            throw new InvalidParameterException("Incorrect line. Must match the following pattern: VAR -> elements(|elements...)");

        String[] components = mainSplit[1].trim().split("\\|");
        ArrayList<ArrayList<ProductionElement>> toSet = new ArrayList<ArrayList<ProductionElement>>();
        //Check if letter is upperCase or lowerCase
        for(String component : components){
            component = component.trim();
            ArrayList<ProductionElement> tempList = new ArrayList<ProductionElement>();
            for(int idx = 0; idx < component.length(); idx++){
                char currChar = component.charAt(idx);
                if(currChar >= 'A' && currChar <= 'Z')
                    tempList.add(new NonTerminal(currChar));
                else if(currChar >= 'a' && currChar <='z' )
                    tempList.add(new Terminal(currChar));
                else
                    throw new InvalidParameterException("Only letters from a - z and A - Z. You entered: " + currChar);
            }
            toSet.add(tempList);
        }
        return new Production(mainVar, toSet);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int idx1 = 0; idx1 < this.elements.size(); idx1++){
            List<ProductionElement> tempList = this.elements.get(idx1);
            for (ProductionElement productionElement : tempList){
                sb.append(productionElement);
            }
            if(idx1 + 1 != this.elements.size())
                sb.append("|");
        }

        return nonTerminal.getTag() + " -> " + sb.toString();
    }
}
