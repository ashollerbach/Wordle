import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarlo extends JPanel {

    private int[] spread;

    public MonteCarlo() {
        this.spread = new int[7];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBarGraph(g, spread);
    }

    private void drawBarGraph(Graphics g, int[] spread) {
        int width = getWidth();
        int height = getHeight();

        int barWidth = (width - 100) / spread.length;
        int maxValue = getMaxValue(spread);

        for (int i = 0; i < spread.length; i++) {
            int barHeight = (int) ((double) spread[i] / maxValue * (height - 100));
            int x = 50 + i * barWidth;
            int y = height - 50 - barHeight;
            g.setColor(Color.BLUE);
            g.fillRect(x, y, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, barWidth, barHeight);
            // Display x-axis labels
            g.drawString(Integer.toString(i + 1), x + barWidth / 2, height - 30);
        }

        // Display y-axis labels
        for (int i = 0; i <= 10; i++) {
            int value = i * (maxValue / 10);
            int labelY = height - 50 - (int) ((double) value / maxValue * (height - 100));
            g.drawString(Integer.toString(value), 20, labelY);
        }
    }

    private int getMaxValue(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Integer> attemptsList = new ArrayList<>();
        List<String> failedWords = new ArrayList<>();
        int[] spread = new int[7]; // distribution of the attempts
        int repeats = 1000;
        for (int i = 0; i < repeats; i++) {
            Wordle wordle = new Wordle(new FileInputStream(new File("wordlist.txt")));
            wordle.createValidWordList();
            Random rand = new Random();
            int answerIndex = rand.nextInt(2314) + 1;
            wordle.setAnswer(wordle.lexicon.get(answerIndex));
            int attempts = 1;
            String bestWord = "arose";
            while (!bestWord.equals(wordle.getAnswer()) && attempts < 6) {
                attempts++;
                wordle.setUserIn(bestWord);
                wordle.evalueateWordle();
                wordle.updateLexicon();
                bestWord = wordle.findBestWord();
                if(attempts == 6){
                    failedWords.add(wordle.getAnswer());
                }
            }
            attemptsList.add(attempts);
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

        // Create and set up the frame
        JFrame frame = new JFrame("Attempts Distribution");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create and set up the panel
        MonteCarlo panel = new MonteCarlo();
        panel.spread = spread;

        // Add the panel to the frame
        frame.add(panel);

        // Display the frame
        frame.setVisible(true);
    }
}
