package MiniJava.codeGenerator.semanticActions;

import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token;

public class SubAction implements SemanticAction {
    @Override
    public void execute(CodeGenerator context, Token next) {
        context.sub();
    }
}
