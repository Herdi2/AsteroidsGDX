import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Asteroids extends Game {

    private Stage gameStage;
    private Spaceship spaceship;


    @Override
    public void create() {
        spaceship = new Spaceship();
        gameStage = new Stage();
        gameStage.addActor(spaceship);
    }

    public void render() {

        gameStage.act(1/60f);

        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameStage.draw();

    }

}
