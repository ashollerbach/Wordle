
public interface WordleGame{

//return the size of the lexicon
public int getLexiconSize();

//sets userIn as a list of characters 
public void setUserIn(String in);

//returns userIn as a string
public String toString();

//sets the answer
public void setAnswer(String in);

//returns the answer
public String getAnswer();

//checks if word is a valid string
public boolean isValidWord(String in);

//shows the correct/incorrect
public void evalueateWordle();

//removes impossible words
public void removeBlackWords();

//removes words in lexicon 
public void removeYellowWords();

//removes words without letters in the right pos
public void removeGreenWords();

//checks if the answer contains a letter
public boolean contains(String word, char in);

// uses the remove functions and updates lexicon size
public void updateLexicon();

//finds the best word using the entire wordlist
public String findBestWord();

//manually enter the word without an ans
public void manualEntry();

//finds if an answer is correct
public boolean isCorrect();

//finds the best word with the lexicon
public String findBestWord2();

//creates a valid guess list that does not get changed
public void createValidWordList();

}