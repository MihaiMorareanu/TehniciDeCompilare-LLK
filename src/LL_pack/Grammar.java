package LL_pack;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
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


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Production production : this.productions){
            sb.append(production).append("\n");
        }
        return sb.toString();
    }
}
