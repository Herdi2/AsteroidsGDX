import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Asteroids extends Game implements InputProcessor {

    private Stage gameStage;
    private Spaceship spaceship;

    // Window dimensions
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    // Handles input
    private InputMultiplexer im;

    @Override
    public void create() {
        spaceship = new Spaceship();
        gameStage = new Stage();
        gameStage.addActor(spaceship);

        // Setup an inputprocessor to handle input events
        im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        im.addProcessor(this);
    }

    public void render() {

        gameStage.act(1/60f);

        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameStage.draw();

    }

    /**
     * The methods below are required of the InputProcessor interface
     */


    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.SPACE) {
            Particle laser = new Particle(spaceship);
            gameStage.addActor(laser);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        spaceship.setMouse(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        spaceship.setMouse(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
