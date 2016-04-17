package LL_pack;

public abstract class ProductionElement {
    private char tag;
    private boolean isTerminal = false;
    private boolean isLambda = false;

    public char getTag() {
        return tag;
    }

    public void setTag(char tag) {
        this.tag = tag;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public boolean isLambda() {
        return isLambda;
    }

    public void setLambda(boolean lambda) {
        isLambda = lambda;
    }

    public String toString() {
        return "" + tag;
    }
}
