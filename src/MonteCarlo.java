import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarlo {

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> attemptsList = new ArrayList<Integer>();
        List<String> failedWords = new ArrayList<String>();
        int[] spread = new int[7]; // distribution of the attempts
        int repeats = 100;
        for (int i = 0; i < repeats; i++) {
            Wordle wordle = new Wordle(new FileInputStream(new File("wordlist.txt")));
            wordle.createValidWordList();
            Random rand = new Random();
            int answerIndex = rand.nextInt(2314) + 1;
            wordle.setAnswer(wordle.lexicon.get(answerIndex));
            int attempts = 1;
            String bestWord = "arose";
            while (!bestWord.equals(wordle.ans) && attempts < 6) {
                attempts++;
                wordle.setUserIn(bestWord);
                wordle.evalueateWordle();
                wordle.updateLexicon();
                if(wordle.getLexiconSize() > .01 * wordle.validWords.size()){
                    bestWord = wordle.findBestWord();
                } else {
                    bestWord = wordle.findBestWord2();
                }
                if(attempts == 6){
                    failedWords.add(wordle.getAnswer());
                }
            }
            attemptsList.add(attempts);
            attempts = 0;
        }
        for (int i = 0; i < 6; i++) {
            spread[i] = 0;
        }
        for (int i : attemptsList) {
            spread[i]++;
        }
        int n = 0;
        for (int i : spread) {
            n++;
            if (n < 7) {
                System.out.println("Answers with " + n + " guesses: " + i);
            } else {
                System.out.println("Answers with " + n + " or more guesses: " + i);

            }
        }
        System.out.println("\nFailed words:");
        for(String answer: failedWords){
            System.out.print(answer + ", ");
        }
    }
}
