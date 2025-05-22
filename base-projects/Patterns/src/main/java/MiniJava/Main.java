package MiniJava;

import MiniJava.parser.ParserFacade;

public class Main {
    public static void main(String[] args) {
        ParserFacade parserFacade = new ParserFacade();
        parserFacade.parseFile("src/main/resources/code");
    }
}
