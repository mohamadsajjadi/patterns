package MiniJava.codeGenerator.semanticActions;

import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token;

public interface SemanticAction {
    void execute(CodeGenerator context, Token next);
}
