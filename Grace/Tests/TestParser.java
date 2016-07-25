package Grace.Tests;

import Grace.Parsing.Parser;
import Grace.Parsing.ParseNode;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;

public class TestParser {

    public static void main(String[] args) throws Exception {

	String source = readFile(args[0]);
	Parser parser = new Parser(args[0],source);

	ParseNode module = parser.parse();
	
	module.debugPrint(new PrintStream(System.out,true), "");
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
