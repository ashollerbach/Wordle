import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WordleClient {

    public static void main(String[] args) throws FileNotFoundException {
        String bestWord;
        Wordle wordle = new Wordle(new FileInputStream(new File("wordlist.txt")));
        while (!wordle.isCorrect()) {
            bestWord = wordle.findBestWord();
            System.out.println("The best word is " + bestWord);
            wordle.manualEntry();
            wordle.updateLexicon();
            if (wordle.lexicon.size() == 1) {
                System.out.println("The wordle must be "
                        + wordle.lexicon.get(0));
                break;
            }
        }
    }
}
