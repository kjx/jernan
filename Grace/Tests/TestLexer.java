package Grace.Tests;

import Grace.Parsing.Lexer;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;

public class TestLexer {

    public static void main(String[] args) throws Exception {

	String source = readFile(args[0]);
	Lexer lexer = new Lexer(args[0],source);

        while (!lexer.done())
        {
	    System.out.println(lexer.current);
	    lexer.nextToken();
	}
	

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
