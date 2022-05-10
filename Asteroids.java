import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Asteroids extends Game {

    private Game game;

    @Override
    public void create() {
        game = this;
        setScreen(new GameScreen());
    }
}
