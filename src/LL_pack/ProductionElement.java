package LL_pack;

public abstract class ProductionElement {
    char tag;
    boolean isTerminal = false;

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

    public String toString() {
        return "" + tag;
    }
}
