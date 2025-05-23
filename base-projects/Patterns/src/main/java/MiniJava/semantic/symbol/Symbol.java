package MiniJava.semantic.symbol;

public class Symbol {

    // Private fields to ensure encapsulation
    private SymbolType type;
    private int address;

    // Constructor to initialize the fields
    public Symbol(SymbolType type, int address) {
        this.type = type;
        this.address = address;
    }

    // Getter for type
    public SymbolType getType() {
        return type;
    }

    // Setter for type
    public void setType(SymbolType type) {
        // Optionally, you could add validation here to control how the type is set
        this.type = type;
    }

    // Getter for address
    public int getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(int address) {
        // Optionally, you could add validation to ensure a valid address is set
        this.address = address;
    }
}
