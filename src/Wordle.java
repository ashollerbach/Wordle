import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wordle implements WordleGame {

    // Letter object used to determine correctness for user interface
    public class Letter {
        char letter;
        // -1 for black, 0 for yellow, 1 for green
        int color;

        Letter(char letter, int color) {
            this.letter = letter;
            this.color = color;
        }
    }

    List<String> lexicon; // list of possible words
    List<Letter> listOfLetters = new ArrayList<Letter>(); // list of letters used to display correctness
    List<Character> userIn = new ArrayList<Character>(); // the guess the user inputs
    List<Character> answer = new ArrayList<Character>(); // TODO
    String ans; // correct answer

    List<String> validWords = new ArrayList<String>(); // list of all reasonable guesses

    // wordle object that contains the lexicon
    public Wordle(InputStream in) {
        try {
            lexicon = new ArrayList<String>();
            @SuppressWarnings("resource")
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            while (s.hasNext()) {
                String str = s.next();
                lexicon.add(str.toLowerCase());
                s.nextLine();
            }
            in.close();
        } catch (java.io.IOException e) {
            System.err.println("Error reading from InputStream.");
            System.exit(1);
        }
    }

    // returns lexicon size
    public int getLexiconSize() {
        return lexicon.size();
    }

    // method used to set the user input and removes the word already guessed
    public void setUserIn(String in) {
        if (isValidWord(in)) {
            while (!userIn.isEmpty()) {
                userIn.remove(0);
            }
            in = in.toLowerCase();
            for (int i = 0; i < 5; i++) {
                userIn.add(in.charAt(i));
            }
            lexicon.remove(in);
        }
    }

    // converts a list of characters into a string to be displayed to the user
    public String toString(List<Character> in) {
        String out = "";
        for (char charIn : in) {
            out += String.valueOf(charIn);
        }
        return out;
    }

    // sets the correct answer
    public void setAnswer(String in) {
        if (isValidWord(in)) {
            ans = in;
            in = in.toLowerCase();
            int count = 0;
            while (count < 5) {
                answer.add(in.charAt(count));
                count++;
            }
        } else {
            System.out.println(in + " is not a valid word.");
        }
    }

    public String getAnswer() {
        return toString(answer);
    }

    public boolean isValidWord(String in) {
        if (in.length() != 5) {
            System.out.println(in + " is not a valid word");
            return false;
        } else {
            return true;
        }
    }

    // removes all words with the given letter if it is not in the wordle
    public void removeBlackWords() {
        for (Letter e : listOfLetters) {
            if (e.color == -1) {
                for (int i = 0; i < lexicon.size(); i++) {
                    if (contains(lexicon.get(i), e.letter)) {
                        lexicon.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    // removes all words with a letter in the same spot as the yellow letter
    public void removeYellowWords() {
        for (Letter e : listOfLetters) {
            if (e.color == 0) {
                for (int i = 0; i < lexicon.size(); i++) {
                    if (!contains(lexicon.get(i), e.letter)) {
                        lexicon.remove(i);
                        i--;
                    } else if (lexicon.get(i).charAt(listOfLetters.indexOf(e)) == e.letter) {
                        lexicon.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    // removes words with a letter in the wrong spot
    public void removeGreenWords() {
        for (int i = 0; i < 5; i++) {
            if (listOfLetters.get(i).color == 1) {
                for (int j = 0; j < lexicon.size(); j++) {
                    if (lexicon.get(j).charAt(i) != listOfLetters.get(i).letter) {
                        lexicon.remove(j);
                        j--;
                    }
                }
            }
        }
    }

    // compares the guess to the answer
    public void evalueateWordle() {
        while (!listOfLetters.isEmpty()) {
            listOfLetters.remove(0);
        }
        int count = 0;
        for (char c : userIn) {
            if (c == ans.charAt(count)) {
                listOfLetters.add(new Letter(c, 1));
            } else if (contains(ans, c)) {
                listOfLetters.add(new Letter(c, 0));
            } else {
                listOfLetters.add(new Letter(c, -1));
            }
            count++;
        }
    }

    // sees if a word has a certain character
    public boolean contains(String word, char in) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == in) {
                return true;
            }
        }
        return false;
    }

    // removes all invalid words
    public void updateLexicon() {
        removeBlackWords();
        removeGreenWords();
        removeYellowWords();
    }

    /**
     * start with an array of every character in the alphabet
     * every time a character is read add 1 to that letter
     * find the 5 most used letters in the possible wordle answers list
     * guess a word that knocks most guesses out
     * 
     * @return the word that knocks most answers out
     */
    public String findBestWord() {
        String bestWord = lexicon.get(0);
        Letter[] freqLetters = new Letter[5];
        // instantiate an empty alphabet set
        List<Letter> alphabet = new ArrayList<Letter>();
        for (char alphabetAdd = 'a'; alphabetAdd <= 'z'; alphabetAdd++) {
            alphabet.add(new Letter(alphabetAdd, 0));
        }

        // goes through each word in the possible answers and finds the frequency of
        // each character
        for (String word : lexicon) {
            // evaluates each letter in the alphabet
            for (char a : word.toCharArray()) {
                alphabet.get(a - 97).color++;
            }
        }

        // sets the first 5 frequent letters to be a,b,c,d,e
        for (int i = 0; i <= 4; i++) {
            freqLetters[i] = alphabet.get(i);
        }
        // reads through the array and picks the 5 most common letters
        for (Letter alpha : alphabet) {
            // pick most common letter
            // if a new most common letter is found replace the least common one with that
            // new letter
            for (int i = 0; i < 5; i++) {
                if (alpha.color > freqLetters[i].color) {
                    freqLetters[i] = alpha;
                    break;
                }
            }
        }

        // finds a satifactory word in the possible guesses list
        for (String guess : lexicon) {
            int count = 0;
            for (Letter L : freqLetters) {
                if (guess.contains(String.valueOf(L.letter))) {
                    count++;
                }
            }
            if (count >= 4) {
                bestWord = guess;
            }
            count = 0;
        }
        lexicon.remove(bestWord);
        return bestWord;
    }

    //
    public void manualEntry() {
        String in = "";
        while (!listOfLetters.isEmpty()) {
            listOfLetters.remove(0);
        }
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the word: ");
        in = scan.nextLine();
        setUserIn(in);
        for (int i = 0; i < 5; i++) {
            System.out.print("Enter the color of the letter in position "
                    + i + ": ");
            int c = scan.nextInt();
            listOfLetters.add(new Letter(userIn.get(i), c));
        }
    }

    public boolean isCorrect() {
        if (listOfLetters.isEmpty()) {
            return false;
        }
        for (Letter e : listOfLetters) {
            if (e.color != 1) {
                return false;
            }
        }
        return true;
    }

    public String findBestWord2() {
        String bestWord = lexicon.get(0);
        int currentcount = 0;
        int bestcount = 0;

        for (String word : lexicon) {
            for (int i = 0; i < 5; i++) {
                for (String word2 : lexicon) {
                    if (contains(word2, word.charAt(i))) {
                        currentcount++;
                    }
                    if (word.charAt(i) == word2.charAt(i)) {
                        currentcount++;
                    }
                }
            }
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (word.charAt(j) == word.charAt(k) && j != k) {
                        currentcount /= 1.3;
                    }
                }
            }
            if (currentcount > bestcount) {
                bestWord = word;
                bestcount = currentcount;
            }
            currentcount = 0;
        }
        return bestWord;
    }

    // creates a list of valid guesses
    public void createValidWordList() {
        Scanner sc = new Scanner("validWords.txt");
        while (sc.hasNextLine()) {
            validWords.add(sc.nextLine());
        }
        sc.close();
    }
}