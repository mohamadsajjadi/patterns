package MiniJava.codeGenerator;

import MiniJava.codeGenerator.semanticActions.*;
import MiniJava.codeGenerator.semanticActions.AddAction;
import MiniJava.codeGenerator.semanticActions.AssignAction;
import MiniJava.codeGenerator.semanticActions.PrintAction;
import MiniJava.codeGenerator.semanticActions.SemanticAction;
import MiniJava.codeGenerator.semanticActions.SubAction;
import MiniJava.errorHandler.ErrorHandler;
import MiniJava.scanner.token.Token;
import MiniJava.semantic.symbol.Symbol;
import MiniJava.semantic.symbol.SymbolTableFacade;
import MiniJava.semantic.symbol.SymbolType;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
    private Memory memory = new Memory();
    private Stack<Address> ss = new Stack<Address>();
    private Stack<String> symbolStack = new Stack<>();
    private Stack<String> callStack = new Stack<>();
    private SymbolTableFacade symbolTableFacade;
    private Map<Integer, SemanticAction> semanticActionsMap;
    private int currentAddress;

    public CodeGenerator() {
        symbolTableFacade = new SymbolTableFacade(memory);
        initializeSemanticActions();
    }

    // Query Method: Checks if the function code exists in the map of actions
    public boolean isActionDefined(int func) {
        return semanticActionsMap.containsKey(func);
    }

    // Query Method: Retrieves the action corresponding to the function code
    public SemanticAction getSemanticAction(int func) {
        return semanticActionsMap.get(func);
    }

    private void initializeSemanticActions() {
        semanticActionsMap = new HashMap<>();
        semanticActionsMap.put(10, new AddAction());
        semanticActionsMap.put(11, new SubAction());
        semanticActionsMap.put(9, new AssignAction());
        semanticActionsMap.put(18, new PrintAction());
    }

    // Modifier Method: Executes the corresponding action for the function code
    public void performSemanticAction(int func, Token next) {
        if (isActionDefined(func)) {
            SemanticAction action = getSemanticAction(func);
            action.execute(this, next);
        } else {
            throw new IllegalArgumentException("Undefined semantic action: " + func);
        }
    }

    public void printMemory() {
        memory.pintCodeBlock();
    }

    private void defMain() {
        // ss.pop();
        memory.add3AddressCode(ss.pop().num, Operation.JP,
                new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
        String methodName = "main";
        String className = symbolStack.pop();

        symbolTableFacade.defineMethod(className, methodName, SymbolType.Bool, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    // public void spid(Token next){
    // symbolStack.push(next.value);
    // }
    public void checkID() {
        symbolStack.pop();
        if (ss.peek().varType == varType.Non) {
            // TODO : error
        }
    }

    public void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {

                Symbol s = symbolTableFacade.getVariable(className, methodName, next.value);
                varType t = varType.Int;
                switch (s.getType()) {
                case Bool:
                    t = varType.Bool;
                    break;
                case Int:
                    t = varType.Int;
                    break;
                }
                ss.push(new Address(s.getAddress(), t));

            } catch (Exception e) {
                ss.push(new Address(0, varType.Non));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, varType.Non));
        }
        symbolStack.push(next.value);
    }

    public void fpid() {
        ss.pop();
        ss.pop();

        Symbol s = symbolTableFacade.getField(symbolStack.pop(), symbolStack.pop());
        varType t = varType.Int;
        switch (s.getType()) {
        case Bool:
            t = varType.Bool;
            break;
        case Int:
            t = varType.Int;
            break;
        }
        ss.push(new Address(s.getAddress(), t));

    }

    public void kpid(Token next) {
        ss.push(symbolTableFacade.getAddress(next.value));
    }

    public void intpid(Token next) {
        ss.push(new Address(Integer.parseInt(next.value), varType.Int, TypeAddress.Imidiate));
    }

    public void startCall() {
        // TODO: method ok
        ss.pop();
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolTableFacade.consumeNextParameter(className, methodName);
        callStack.push(className);
        callStack.push(methodName);

        // symbolStack.push(methodName);
    }

    public void call() {
        // TODO: method ok
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            symbolTableFacade.getNextParameter(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }
        varType t = varType.Int;
        switch (symbolTableFacade.getMethodReturnType(className, methodName)) {
        case Int:
            t = varType.Int;
            break;
        case Bool:
            t = varType.Bool;
            break;
        }
        Address temp = new Address(memory.getTemp(), t);
        ss.push(temp);
        memory.add3AddressCode(Operation.ASSIGN, new Address(temp.num, varType.Address, TypeAddress.Imidiate),
                new Address(symbolTableFacade.getMethodReturnAddress(className, methodName), varType.Address), null);
        memory.add3AddressCode(Operation.ASSIGN,
                new Address(memory.getCurrentCodeBlockAddress() + 2, varType.Address, TypeAddress.Imidiate),
                new Address(symbolTableFacade.getMethodCallerAddress(className, methodName), varType.Address), null);
        memory.add3AddressCode(Operation.JP,
                new Address(symbolTableFacade.getMethodAddress(className, methodName), varType.Address), null, null);

        // symbolStack.pop();
    }

    public void arg() {
        // TODO: method ok

        String methodName = callStack.pop();
        // String className = symbolStack.pop();
        try {
            Symbol s = symbolTableFacade.getNextParameter(callStack.peek(), methodName);
            varType t = varType.Int;
            switch (s.getType()) {
            case Bool:
                t = varType.Bool;
                break;
            case Int:
                t = varType.Int;
                break;
            }
            Address param = ss.pop();
            if (param.varType != t) {
                ErrorHandler.printError("The argument type isn't match");
            }
            memory.add3AddressCode(Operation.ASSIGN, param, new Address(s.getAddress(), t), null);

            // symbolStack.push(className);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);

    }

    public void assign() {
        Address s1 = ss.pop();
        Address s2 = ss.pop();
        // try {
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in assign is different ");
        }
        // }catch (NullPointerException d)
        // {
        // d.printStackTrace();
        // }
        memory.add3AddressCode(Operation.ASSIGN, s1, s2, null);
    }

    public void add() {
        Address temp = new Address(memory.getTemp(), varType.Int);
        Address s2 = ss.pop();
        Address s1 = ss.pop();

        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        memory.add3AddressCode(Operation.ADD, s1, s2, temp);
        ss.push(temp);
    }

    public void sub() {
        Address temp = new Address(memory.getTemp(), varType.Int);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        memory.add3AddressCode(Operation.SUB, s1, s2, temp);
        ss.push(temp);
    }

    public void mult() {
        Address temp = new Address(memory.getTemp(), varType.Int);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        memory.add3AddressCode(Operation.MULT, s1, s2, temp);
        // memory.saveMemory();
        ss.push(temp);
    }

    public void label() {
        ss.push(new Address(memory.getCurrentCodeBlockAddress(), varType.Address));
    }

    public void save() {
        ss.push(new Address(memory.saveMemory(), varType.Address));
    }

    public void _while() {
        memory.add3AddressCode(ss.pop().num, Operation.JPF, ss.pop(),
                new Address(memory.getCurrentCodeBlockAddress() + 1, varType.Address), null);
        memory.add3AddressCode(Operation.JP, ss.pop(), null, null);
    }

    public void jpf_save() {
        Address save = new Address(memory.saveMemory(), varType.Address);
        memory.add3AddressCode(ss.pop().num, Operation.JPF, ss.pop(),
                new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null);
        ss.push(save);
    }

    public void jpHere() {
        memory.add3AddressCode(ss.pop().num, Operation.JP,
                new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
    }

    public void print() {
        memory.add3AddressCode(Operation.PRINT, ss.pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        memory.add3AddressCode(Operation.EQ, s1, s2, temp);
        ss.push(temp);
    }

    public void less_than() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        memory.add3AddressCode(Operation.LT, s1, s2, temp);
        ss.push(temp);
    }

    public void and() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Bool || s2.varType != varType.Bool) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        memory.add3AddressCode(Operation.AND, s1, s2, temp);
        ss.push(temp);
    }

    public void not() {
        Address temp = new Address(memory.getTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Bool) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        memory.add3AddressCode(Operation.NOT, s1, s2, temp);
        ss.push(temp);
    }

    public void defClass() {
        ss.pop();
        symbolTableFacade.defineClass(symbolStack.peek());
    }

    public void defMethod() {
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTableFacade.defineMethod(className, methodName, SymbolType.Bool, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void popClass() {
        symbolStack.pop();
    }

    public void extend() {
        ss.pop();
        symbolTableFacade.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        ss.pop();
        symbolTableFacade.defineField(symbolStack.pop(), symbolStack.peek(), SymbolType.Int);
    }

    public void defVar() {
        ss.pop();

        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTableFacade.defineMethodLocalVariable(className, methodName, var, SymbolType.Int);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        // TODO : call ok

        String methodName = symbolStack.pop();
        Address s = ss.pop();
        SymbolType t = symbolTableFacade.getMethodReturnType(symbolStack.peek(), methodName);
        varType temp = varType.Int;
        switch (t) {
        case Int:
            break;
        case Bool:
            temp = varType.Bool;
        }
        if (s.varType != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        memory.add3AddressCode(Operation.ASSIGN, s,
                new Address(symbolTableFacade.getMethodReturnAddress(symbolStack.peek(), methodName), varType.Address,
                        TypeAddress.Indirect),
                null);
        memory.add3AddressCode(Operation.JP,
                new Address(symbolTableFacade.getMethodCallerAddress(symbolStack.peek(), methodName), varType.Address),
                null, null);

        // symbolStack.pop();
    }

    public void defParam() {
        // TODO : call Ok
        ss.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTableFacade.defineMethodParameter(className, methodName, param, SymbolType.Int);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void lastTypeBool() {
        symbolTableFacade.setLastType(SymbolType.Bool);
    }

    public void lastTypeInt() {
        symbolTableFacade.setLastType(SymbolType.Int);
    }

    public void main() {

    }
}
