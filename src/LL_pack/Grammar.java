package LL_pack;

import java.util.*;

public class Grammar {
    int k;
    private List<Production> productions;

    public Grammar() {
        this.productions = new ArrayList<Production>();
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public void addProduction(Production production){
        productions.add(production);
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    /**
     * Returns list of first 'k' characters as strings from grammar.
     */
    public Set<String> first(String input){
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        HashSet<String> toRet = new HashSet<String>();
        while(true){
            if(idx >= this.k  || idx >= input.length()){
                toRet.add(sb.toString());
                return toRet;
            }

            char currChar = input.charAt(idx);
            if(currChar == '~') continue;
            if(currChar >= 'a' && currChar <= 'z'){
                sb.append(currChar);
            } else if(currChar >= 'A' && currChar <= 'Z') {
                return first_impl(this.k - idx, sb, input.substring(idx));
            }

            ++idx;
        }
    }

    /**
     * Helper for "first" method
     * @param k - remaining characters to append
     * @param sb - already appended characters
     * @param input
     * @return Set of strings
     */
    private Set<String> first_impl(int k, StringBuilder sb, String input){
        System.out.println("K: " + k + " String until now: " + sb.toString() + " Input: " + input);
        HashSet<String> toRet = new HashSet<String>();

        //Get production elements
        char nonTerminalChar;
        if(input.length() > 0)
            nonTerminalChar = input.charAt(0);
        else
            return toRet;

        System.out.println("NonTerminal from input: " + nonTerminalChar);
        input = input.length() > 1 ? input.substring(1) : "";
        String productionStr = null;
        for(Production production : productions){
            System.out.println("Curr nonTerminal: " + production.getNonTerminal().getTag());
            if(production.getNonTerminal().getTag() == nonTerminalChar){
                productionStr = production.toString();
                productionStr = productionStr.substring(productionStr.indexOf(">")+2);
                break;
            }
        }

        if(productionStr == null){
            throw new RuntimeException("No production with " + nonTerminalChar + " as NonTerminal");
        }

        String[] splittedProduction = productionStr.split("\\|");
        for(String production : splittedProduction){
            int kCopy = k;
            StringBuilder sbCopy = new StringBuilder(sb);
            String inputCopy = input;
            if(!production.equals("~")) {
                inputCopy = production + inputCopy;
            }

            if(inputCopy.length() == 0 && sbCopy.toString().length() == 0){
                toRet.add("~");
                continue;
            }

            System.out.println("New string: " + inputCopy);
            if(inputCopy.length() == 0){
                toRet.add(sb.toString());
            } else {
                int idx = 0;
                while(idx < inputCopy.length()){
                    if(kCopy == 0 || idx == inputCopy.length()){
                        toRet.add(sbCopy.toString());
                        break;
                    }else{

                        char currChar = inputCopy.charAt(idx);
                        if(currChar == '~') continue;
                        if(currChar >= 'a' && currChar <= 'z'){
                            sbCopy.append(currChar);
                            --kCopy;
                        }else if(currChar >= 'A' && currChar <= 'Z'){
//                            toRet.addAll(first_impl(kCopy, sbCopy, inputCopy.substring(idx-1)));

                            toRet.addAll(first_impl(kCopy, sbCopy, inputCopy.substring(idx)));
                            break;
                        }

                        ++idx;
                    }
                }
                if(idx == inputCopy.length())
                    toRet.add(sbCopy.toString());
            }

        }

        return toRet;
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Production production : this.productions){
            sb.append(production).append("\n");
        }
        return sb.toString();
    }
}
