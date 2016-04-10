package LL_pack;

public class NonTerminal extends ProductionElement {

    Production production;

    public NonTerminal(char tag) {
        super.tag = tag;
        this.production = null;
    }

    public NonTerminal(Production production, char tag) {
        super.tag = tag;
        this.production = production;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }
}