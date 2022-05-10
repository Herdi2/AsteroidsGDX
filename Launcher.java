import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Launcher {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    public static void main(String[] args) {
        Game myGame = new Asteroids();
        LwjglApplication launcher =
                new LwjglApplication(myGame, "Asteroids", WINDOW_WIDTH, WINDOW_HEIGHT);
    }

}
