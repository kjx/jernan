package Grace.Tests;

import Grace.Parsing.Parser;
import Grace.Parsing.ParseNode;
import Grace.Parsing.ObjectParseNode;
import Grace.Execution.Node;
import Grace.Execution.ExecutionTreeTranslator;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;

public class TestAST {

    public static void main(String[] args)  {

	String source = readFile(args[0]);
	Parser parser = new Parser(args[0],source);

	ObjectParseNode module = (ObjectParseNode)parser.parse();
	Node ast = new ExecutionTreeTranslator().translate(module);

	ast.debugPrint(new PrintStream(System.out,true), "");
    }

    public static String readFile(String filename) {
            File f = new File(filename);
            try {
                byte[] bytes = Files.readAllBytes(f.toPath());
                return new String(bytes,"UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
    }
}
