import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordleTest {
    public static void main(String[] args) throws FileNotFoundException{
        Wordle wordle = new Wordle(new FileInputStream(new File("wordlist.txt")));
        wordle.createValidWordList();
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the answer: ");
        String ans = scan.nextLine();
        wordle.setAnswer(ans);
        System.out.println();
        scan.close();
        String bestWord = "arose";
        int attempts = 1;
        while(!bestWord.equals(wordle.getAnswer())){
            System.out.println("Attempt #" + attempts);
            attempts++;
            System.out.println("The best word is " + bestWord);
            wordle.setUserIn(bestWord);
            wordle.evalueateWordle();
            wordle.updateLexicon();
            if(wordle.getLexiconSize() > .01 * wordle.validWords.size()){
                bestWord = wordle.findBestWord();
            } else{
                bestWord = wordle.findBestWord2();
            }
            System.out.println("Lexicon size: " + wordle.lexicon.size() + 1 + "\n");
        }
        System.out.println("The answer is " + bestWord);
        System.out.print("Answer found in " + attempts++ + " attemps");
    }
}
