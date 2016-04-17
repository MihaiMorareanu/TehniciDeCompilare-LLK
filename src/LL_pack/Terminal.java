package LL_pack;

public class Terminal extends ProductionElement{

    public Terminal(char tag) {
        super.setTag(tag);
        super.setTerminal(true);
    }

    public Terminal(char tag, boolean isLambda){
        super.setTag(tag);
        super.setTerminal(true);
        super.setLambda(isLambda);
    }
}
