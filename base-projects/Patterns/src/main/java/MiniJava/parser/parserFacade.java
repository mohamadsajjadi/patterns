// New Facade Class
package MiniJava.parser;

import MiniJava.errorHandler.ErrorHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParserFacade {
    private Parser parser;

    public ParserFacade() {
        parser = new Parser();
    }

    public void parseFile(String filePath) {
        try {
            Scanner sc = new Scanner(new File(filePath));
            parser.startParse(sc);
        } catch (FileNotFoundException e) {
            ErrorHandler.printError(e.getMessage());
        }
    }
}
