package LL_pack;

public class Terminal extends ProductionElement{

    public Terminal(char tag) {
        super.tag = tag;
        super.isTerminal = true;
    }
}
