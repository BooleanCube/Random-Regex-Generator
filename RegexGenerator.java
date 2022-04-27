import generator.RegEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegexGenerator {

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter RegEx:");
        RegEx regex = new RegEx(bf.readLine());
        if(regex.isValid()) {
            String generatedString = regex.generateRandomString();
            System.out.println("Generated Random String:\n" +
                    generatedString);
        } else System.out.println("Please enter a valid RegEx! " +
                "Read this article for more information about RegEx about available RegEx operations:\n" +
                "http://www.categories.acsl.org/wiki/index.php?title=FSAs_and_Regular_Expressions");
    }

}
