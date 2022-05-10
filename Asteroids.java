import com.badlogic.gdx.*;

/**
 * The main class for handling the different game screens
 */
public class Asteroids extends Game {

    private static Game game;

    @Override
    public void create() {
        game = this;
        setScreen(new MenuScreen());
    }

    public static void setActiveScreen(Screen screen) {
        game.setScreen(screen);
    }
}
