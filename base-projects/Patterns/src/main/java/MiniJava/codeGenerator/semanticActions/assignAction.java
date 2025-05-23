package MiniJava.codeGenerator.semanticActions;

import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token;

public class AssignAction implements SemanticAction {
    @Override
    public void execute(CodeGenerator context, Token next) {
        context.assign();
    }
}
