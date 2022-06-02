import com.badlogic.gdx.*;
import java.io.*;
import java.util.LinkedList;

/**
 * The main class for handling the different game screens
 */
public class Asteroids extends Game {

    private static Game game;
    // Handles the scoreboard
    static class Score {
        private String name;
        private int points;

        Score(String name, int points) {
            this.name = name;
            this.points = points;
        }

        public String getName() {
            return name;
        }

        public int getPoints() {
            return points;
        }

    }
    public static LinkedList<Score> scoreList;

    @Override
    public void create() {
        scoreList = new LinkedList<>();

        File csvfile = new File("scores.csv");
        if(!csvfile.exists()) {
            try {
                csvfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("scores.csv"));
            String line;
            while((line = br.readLine())!= null) {
                String[] values = line.split(", ");
                scoreList.add(new Score(values[0], Integer.parseInt(values[1])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        game = this;
        setScreen(new MenuScreen());
    }

    public static void setActiveScreen(Screen screen) {
        game.setScreen(screen);
    }
}
