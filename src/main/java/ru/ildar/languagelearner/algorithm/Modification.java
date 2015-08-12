package ru.ildar.languagelearner.algorithm;

/** Modification operation. Carries the information needed for matching the actual string
 * to the expected string, such as modification type and symbol for modification operation. */
public class Modification
{
    /** Modification operation type */
    public enum ModifOperation { DELETE, INSERT, SYMBOLS_EQUAL, DELETE_ALL, INSERT_ALL, REPLACE }

    private ModifOperation modifOperation;
    private Character symbol;

    public Modification() { }
    public Modification(ModifOperation modifOperation)
    {
        this.modifOperation = modifOperation;
    }
    public Modification(ModifOperation modifOperation, Character symbol)
    {
        this.modifOperation = modifOperation;
        this.symbol = symbol;
    }

    /** Modification that needs to be made */
    public ModifOperation getModifOperation()
    {
        return modifOperation;
    }

    /** Symbol for modification operation. Needed for INSERT and REPLACE modifications. */
    public Character getSymbol()
    {
        return symbol;
    }
}
