package LL_pack;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ParseTable {
    private ArrayList<String> nonTerminals;
    private ArrayList<String> productions;
    private Pair<String, Integer>[][] table;
    private int productionsLength;
    private int nonTerminalsLength;

    public ParseTable(){
        System.out.println("Parse table");
        this.nonTerminals = new ArrayList<String>();
        this.productions = new ArrayList<String>();
        this.table = new Pair[5][5];
        this.productionsLength = 5;
        this.nonTerminalsLength = 5;
    }

    public ParseTable(List<String> nonTerminals, List<String> productions) {
        this.nonTerminals = new ArrayList<>(nonTerminals);
        this.productions = new ArrayList<>(productions);
        this.table = new Pair[5][5];
        this.productionsLength = 5;
        this.nonTerminalsLength = 5;
    }


    public ParseTable(Set<String> nonTerminals, Set<String> productions) {
        this.nonTerminals = new ArrayList<>(nonTerminals);
        this.productions = new ArrayList<>(productions);
        this.table = new Pair[5][5];
        this.productionsLength = 5;
        this.nonTerminalsLength = 5;
    }

    public void add(String production, String nonTerminal, String key, Integer val){
        System.out.println("NonTerminals : " + this.nonTerminals);
        System.out.println("Productions: " + this.productions);

        int productionIdx = this.productions.indexOf(production);
        int nonTerminalIdx = this.nonTerminals.indexOf(nonTerminal);
        if(key == null || val == null)
            throw new RuntimeException("<ParseTable - add> Can't add element with null key or value!");

        if(productionIdx == -1){
            this.productions.add(production);
            productionIdx = this.productions.size() - 1;
        }

        if(nonTerminalIdx == -1){
            this.nonTerminals.add(nonTerminal);
            nonTerminalIdx = this.nonTerminals.size() - 1;
        }

        if(productionIdx >= this.productionsLength || nonTerminalIdx >= this.nonTerminalsLength){
            this.productionsLength += 5;
            this.nonTerminalsLength += 5;
            Pair<String, Integer>[][] tempTable = new Pair[this.productionsLength][this.nonTerminalsLength];

            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    tempTable[i][j] = table[i][j];
                }
            }

            this.table = tempTable;
            System.out.println("Resize complete!");
        }

        this.table[productionIdx][nonTerminalIdx] = new Pair<>(key, val);
    }

    public Pair<String, Integer> get(String production, String nonTerminal){
        int productionIdx = this.productions.indexOf(production);
        int nonTerminalIdx = this.nonTerminals.indexOf(nonTerminal);

        return this.table[productionIdx][nonTerminalIdx];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int i = 0; i < nonTerminals.size(); i++) {
            sb.append(nonTerminals.get(i)).append("  ");
        }
        sb.append("\n");

        for (int i = 0; i < productions.size(); i++) {
            for (int j = 0; j < nonTerminals.size(); j++) {
                if(j == 0)
                    sb.append(productions.get(i) + "  ");
                sb.append(table[i][j] + "  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
