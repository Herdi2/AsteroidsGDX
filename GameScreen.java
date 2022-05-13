import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.ArrayList;

/**
 * The main game screen were the game is played and game actors are rendered.
 *
 */
public class GameScreen extends Game implements InputProcessor, Screen {

    private Stage gameStage;
    private Spaceship spaceship;

    // Window dimensions
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    // Handles input
    private InputMultiplexer im;

    private ArrayList<Rock> asteroids;
    private ArrayList<Particle> lasers;


    @Override
    public void create() {
        spaceship = new Spaceship();
        gameStage = new Stage();
        lasers = new ArrayList<>();
        // Create asteroids
        asteroids = new ArrayList<>();
        spawnFourAsteroids();

        gameStage.addActor(spaceship);

        // Setup an inputprocessor to handle input events
        im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        im.addProcessor(this);
    }

    @Override
    public void render(float dt) {
        gameStage.act(dt);

        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.draw();

        // Remove lasers outside the screen
        lasers.removeIf(p -> p.getX() < 0 || p.getX() > 800 || p.getY() < 0 || p.getY() > 600);

        // Check for lasers colliding with asteroids and remove both if they do
        ArrayList<Rock> currAsteroids = new ArrayList<>();
        currAsteroids.addAll(asteroids);
        for(Particle laser : lasers) {
            for(Rock asteroid : currAsteroids) {
                if(laser.hitBoxRectangle.overlaps(asteroid.hitBoxRectangle)) {
                    laser.remove();
                    asteroid.remove();
                    // Generate two new smaller asteroids
                    destroy(asteroid);

                }
            }
        }

        lasers.removeIf(laser -> laser.getStage() == null);
        asteroids.removeIf(asteroid -> asteroid.getStage() == null);

        // Checks for spaceship colliding with asteroids
        for(Rock e : asteroids) {
            if(e.hitBoxRectangle.overlaps(spaceship.hitBoxRectangle)) {
                System.out.println("HP--");
            }
        }

        // Checks amount of asteroids and adds four if none
        if(asteroids.size() == 0) {
            spawnFourAsteroids();
        }
    }

    private void spawnFourAsteroids() {
        asteroids.add(new Rock(200, 150));
        asteroids.add(new Rock(200, 450));
        asteroids.add(new Rock(600, 450));
        asteroids.add(new Rock(600, 150));

        for(Rock r : asteroids) {
            gameStage.addActor(r);
        }
    }

    /**
     * Is called when an asteroid is destroyed. Will create to new asteroids if
     * the asteroid destroyed was big enough.
     *
     * @param asteroid The destroyed asteroid
     */
    private void destroy(Rock asteroid) {

        // Too small to generate more
        if(asteroid.getWidth() <= 24) {
            return;
        }

        float xPos = asteroid.getX();
        float yPos = asteroid.getY();

        Rock asteroid1 = new Rock(xPos + asteroid.getWidth()/2, yPos + asteroid.getHeight()/2, asteroid.getWidth()/2, asteroid.getHeight()/2);
        Rock asteroid2 = new Rock(xPos - asteroid.getWidth()/2, yPos - asteroid.getHeight()/2, asteroid.getWidth()/2, asteroid.getHeight()/2);

        gameStage.addActor(asteroid1);
        gameStage.addActor(asteroid2);

        asteroids.add(asteroid1);
        asteroids.add(asteroid2);
    }

    /**
     * The methods below are required of the InputProcessor interface
     */

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.SPACE) {
            Particle p = new Particle(spaceship);
            gameStage.addActor(p);
            lasers.add(p);
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

    /* Required by the Screen interface, called when screen is in focus*/
    @Override
    public void show() {
        create();
    }

    @Override
    public void hide() {

    }
}
