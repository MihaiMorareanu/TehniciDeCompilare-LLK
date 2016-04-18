package LL_pack;

import javafx.util.Pair;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    int k;
    private List<Production> productions;

    private Map<NonTerminal, Set<String>> followTable;
    private Set<NonTerminal> nonTerminals;
    private ParseTable parseTable;

    public Grammar() {
        this.productions = new ArrayList<>();
        this.nonTerminals = new HashSet<>();
        this.parseTable = new ParseTable();
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public void addProduction(Production production){
        nonTerminals.add(production.getNonTerminal());
        productions.add(production);
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Set<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(Set<NonTerminal> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    /*Helper*/
    private String joinListElements(List list, String token){
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        while (it.hasNext()){
            String currStr = it.next().toString();
            sb.append(currStr).append(token);
            if(it.hasNext())
                sb.append(token);
        }

        return sb.toString();
    }

    /*Helper*/
    private boolean hasKSizeElemsSubList(Set<String> input){
        for(String currStr : input){
            if(currStr.length() != this.k)
                return false;
        }
        return true;
    }



    /**
     * Returns list of first 'k' characters as strings from grammar.
     */
    public Set<String> first(String input){
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        int appened = 0;
        HashSet<String> toRet = new HashSet<String>();
        while(true){
            if(appened >= this.k  || idx >= input.length()){
                toRet.add(sb.toString());
                return toRet;
            }

            char currChar = input.charAt(idx);
            if(currChar == '~'){
                ++idx;
                continue;
            }

            if(currChar >= 'a' && currChar <= 'z'){
                sb.append(currChar);
                ++appened;
            } else if(currChar >= 'A' && currChar <= 'Z') {
                return first_impl(this.k - appened, sb, input.substring(idx));
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
//        System.out.println("K: " + k + " String until now: " + sb.toString() + " Input: " + input);
        HashSet<String> toRet = new HashSet<String>();

        //Get production elements
        char nonTerminalChar;
        if(input.length() > 0)
            nonTerminalChar = input.charAt(0);
        else
            return toRet;

//        System.out.println("NonTerminal from input: " + nonTerminalChar);
        input = input.length() > 1 ? input.substring(1) : "";
        String productionStr = null;
        for(Production production : productions){
//            System.out.println("Curr nonTerminal: " + production.getNonTerminal().getTag());
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

//            System.out.println("New string: " + inputCopy);
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

    /**
     * Follow method
     * */
    public Set<String> follow(char input){
        HashSet<String> toRet = new HashSet<>();

        input = Character.toUpperCase(input);

        Iterator<NonTerminal> it = nonTerminals.iterator();
        NonTerminal nonTerminal = null;
        while (it.hasNext()){
            NonTerminal tempNonTerminal = it.next();
            if(tempNonTerminal.getTag() == input){
                nonTerminal = tempNonTerminal;
                break;
            }
        }

        if(nonTerminal == null){
            throw new RuntimeException("You have tried to perform follow from NonTerminal element!");
        }

        for(Production production : productions){
            for(int idx = 0; idx < production.getElements().size(); idx++){
                ArrayList<ProductionElement> subProductionElementList = production.getElements().get(idx);
                for(int idx2 = 0; idx2 < subProductionElementList.size(); idx2++){
                    ProductionElement productionElement = subProductionElementList.get(idx2);
                    if(productionElement.getTag() == nonTerminal.getTag()){
                        if(idx2 + 1 < subProductionElementList.size()){
                            List<ProductionElement> firstElements = subProductionElementList.subList(idx2+1, subProductionElementList.size());
                            Set<String> firstChars = first(joinListElements(firstElements, ""));
//                            System.out.println("Follow "+ nonTerminal.getTag() +" = First: " + firstChars + " Follow: " + production.getNonTerminal().getTag());
//                            System.out.println("Has corect format elements : " + hasKSizeElemsSubList(firstChars));
                            if(hasKSizeElemsSubList(firstChars)){
                                firstChars.add("$");
                                return firstChars;
                            }else{
                                Set<String> fromFollow = follow(production.getNonTerminal().getTag());

//                                System.out.println("From first: " + firstChars);
//                                System.out.println("From follow: " + fromFollow);

                                HashSet<String> tempSet = new HashSet<>();
                                Iterator<String> it1 = firstChars.iterator();
                                while (it1.hasNext()){
                                    String str1 = it1.next();
                                    int sizeToAppend = this.k - str1.length();
                                    Iterator<String> it2 = fromFollow.iterator();
                                    while (sizeToAppend > 0 && it2.hasNext()){
                                        String str2 = it2.next();
                                        if(str2.length() > sizeToAppend){
                                            str2 = str2.substring(0, sizeToAppend);
                                        }
                                        tempSet.add(str1+str2);
                                    }
                                }
//                                System.out.println("Return: " + tempSet);
                                toRet.addAll(tempSet);
                                return toRet;
                            }
                        } else {
//                            System.out.println("Follow "+ nonTerminal.getTag() +" = First: ~ Follow: " + production.getNonTerminal().getTag());
                             toRet.addAll(follow(production.getNonTerminal().getTag()));
                        }
                    }
                }
            }
        }

        return toRet;
    }

    /**
     * Populate follow table
     * */
    public void populateFollowTable(){
        this.followTable = new HashMap<>();

        for(NonTerminal nonTerminal : nonTerminals){
            followTable.put(nonTerminal, follow(nonTerminal.getTag()));
        }
    }

    /**
     * Returns follow table
     * */
    public Map<NonTerminal, Set<String>> getFollowTable() {
        return this.followTable;
    }

    public void buildParseTable(){
        int currIdx = 1;
        if(this.followTable == null)
            throw new RuntimeException("Follow table is null so ParseTable can't be build!");

        for(Production production : productions){
            NonTerminal nonTerminal = production.getNonTerminal();
            Iterator<ArrayList<ProductionElement>> it1 = production.getElements().iterator();
            while (it1.hasNext()){
                String tempProduction = joinListElements(it1.next(), "");
                Set<String> fromFirst = first(tempProduction);
                Set<String> fromFollow = this.followTable.get(nonTerminal);
                //Case when returned list contains only "~"
                if(fromFirst.size() == 1){
                    //Case when result from first has only one element
                    String tempComponent = fromFirst.iterator().next();
                    if(tempComponent.equals("~")){
                        for(String currStr : fromFollow){
                            this.parseTable.add(currStr, ""+nonTerminal.getTag(), tempProduction, currIdx);
                        }
                    } else {
                        Set<String> tempSet = new HashSet<>();
                        for(String currFollow : fromFollow){
                            String tempStr = tempComponent + currFollow;
                            if(tempStr.length() > 2){
                                tempStr = tempStr.substring(0, 2);
                            }

                            tempSet.add(tempStr);
                        }
                        for(String tempStr : tempSet)
                            this.parseTable.add(tempStr, ""+nonTerminal.getTag(), tempProduction, currIdx);
                    }
                } else {
                    if(hasKSizeElemsSubList(fromFirst)){
                        for(String currStr : fromFirst){
                            this.parseTable.add(currStr, ""+nonTerminal.getTag(), tempProduction, currIdx);
                        }
                    } else {
                        HashSet<String> tempSet = new HashSet<String>();
                        System.out.println("From first: " + fromFirst);
                        for(String currFromFirst : fromFirst){
                            if(currFromFirst.length() == 2){
                                tempSet.add(currFromFirst);
                            }else{
                                for(String currFromFollow : fromFollow){
                                    String copyCurrFromFirst = currFromFirst + currFromFollow;
                                    if(copyCurrFromFirst.length() > 2)
                                        copyCurrFromFirst = copyCurrFromFirst.substring(0, 2);

                                    tempSet.add(copyCurrFromFirst);
                                }
                            }
                        }

                        for(String tempStr : tempSet){
                            this.parseTable.add(tempStr, ""+nonTerminal.getTag(), tempProduction, currIdx);
                        }
                    }
                }
                ++currIdx;
            }
        }

        System.out.println(this.parseTable);
    }


    public String parseString(char firstChar, String input){
        String firstHolder = firstChar+"$";
        String inputHolder = input + "$";
        StringBuilder result = new StringBuilder();


        while(true){
            System.out.println("( "+inputHolder+", "+firstHolder+", "+result.toString()+" )");

            char first1 = inputHolder.charAt(0);
            char first2 = firstHolder.charAt(0);

            if(first1 == '$' && first2 == '$') break;

            if(!(first1 == first2 && Character.isLowerCase(first1) && Character.isLowerCase(first2))) {
                if(Character.isLowerCase(first2))
                    break;
                else{
                    firstHolder = firstHolder.substring(1);
                    String tempProd = inputHolder.substring(0, this.k);
                    Pair fromParseTable = this.parseTable.get(tempProd, ""+first2);
                    if(!fromParseTable.getKey().equals("~"))
                        firstHolder = fromParseTable.getKey() + firstHolder;
                    result.append(fromParseTable.getValue());
                }
            }else{
                inputHolder = inputHolder.substring(1);
                firstHolder = firstHolder.substring(1);
            }
        }

        if(firstHolder.equals("$")  && inputHolder.equals("$")){
            System.out.println("Result: " + result.toString());
            return result.toString();
        }
        else{
            System.out.println("Error");
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Production production : this.productions){
            sb.append(production).append("\n");
        }
        return sb.toString();
    }
}
