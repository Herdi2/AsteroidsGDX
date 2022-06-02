import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main game screen were the game is played and game actors are rendered.
 *
 */
public class GameScreen extends Game implements InputProcessor, Screen, Sound {

    private Stage gameStage;
    private Spaceship spaceship;
    private float spaceshipHealth;
    private Label gameOverLabel;
    private Label gameOverInstructions;
    private Label healthLabel;

    /**
     * POINTS
     * NOTE: ROCK_WIDTH IS HARDCODED
     * Large rock width = 96 px -> 100 p
     * Medium rock width = 96/2 px -> 50 p
     * Small rock width = 96/4 px -> 25 p
     */
    private Label pointsLabel;
    private int points;
    private final float ROCK_WIDTH = 96;

    // Handles input
    private InputMultiplexer im;

    // Handles game actors
    private ArrayList<Rock> asteroids;
    private ArrayList<Particle> lasers;

    // Handles label creation
    private static Label.LabelStyle labelStyle;

    // Handles sound effects and music
    private long audioLength;
    private float audioVolume;
    private Sound spaceshipShoot;
    private Sound bangLarge;
    private Sound bangMedium;
    private Sound bangSmall;
    private Music backgroundMusic;

    // Handles the score saving
    class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String name) {
            scoreCalculation(name.substring(0, 21), points);
            for(Asteroids.Score s : Asteroids.scoreList) {
                System.out.printf("Name: %s, Points: %d \n", s.getName(), s.getPoints());
            }
        }

        @Override
        public void canceled () {
            Asteroids.setActiveScreen(new MenuScreen());
        }
    }
    MyTextInputListener listener;

    @Override
    public void create() {
        createFont();
        spaceship = new Spaceship();
        gameStage = new Stage();
        lasers = new ArrayList<>();
        // Create asteroids
        asteroids = new ArrayList<>();
        spawnFourAsteroids();
        gameStage.addActor(spaceship);

        // Create spaceship health
        spaceshipHealth = 100;
        healthLabel = new Label("HEALTH: " + spaceshipHealth, labelStyle);
        healthLabel.setColor(Color.GREEN);
        healthLabel.setPosition(0, 0);
        gameStage.addActor(healthLabel);

        // Handles points
        points = 0;
        pointsLabel = new Label("POINTS: " + points, labelStyle);
        pointsLabel.setColor(Color.WHITE);
        pointsLabel.setPosition(0, Launcher.WINDOW_HEIGHT - pointsLabel.getHeight());
        gameStage.addActor(pointsLabel);

        // Create game over screen
        gameOverLabel = new Label("GAME OVER", labelStyle);
        gameOverLabel.setColor(Color.WHITE);
        gameOverLabel.setPosition(Launcher.WINDOW_WIDTH/2 - gameOverLabel.getWidth()/2, Launcher.WINDOW_HEIGHT-200);
        gameOverInstructions = new Label("PRESS 'R' TO RESTART \n \nPRESS 'Q' TO QUIT", labelStyle);
        gameOverInstructions.setColor(Color.WHITE);
        gameOverInstructions.setPosition(Launcher.WINDOW_WIDTH/2 - gameOverInstructions.getWidth()/2, Launcher.WINDOW_HEIGHT- 200 - gameOverLabel.getHeight()*3);

        // Create sound effects
        audioLength = 1L;
        audioVolume = 0.1f;
        spaceshipShoot = Gdx.audio.newSound(Gdx.files.internal("assets/fire.ogg"));
        bangLarge = Gdx.audio.newSound(Gdx.files.internal("assets/bangLarge.ogg"));
        bangMedium = Gdx.audio.newSound(Gdx.files.internal("assets/bangMedium.ogg"));
        bangSmall = Gdx.audio.newSound(Gdx.files.internal("assets/bangSmall.ogg"));

        // Creates background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Sodium-Vapor_Looping.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(audioVolume);
        backgroundMusic.play();

        // Set score listener
        listener = new MyTextInputListener();

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

        // Remove lasers outside the screen
        lasers.removeIf(p -> p.getX() < 0 || p.getX() > 800 || p.getY() < 0 || p.getY() > 600);

        // Check for lasers colliding with asteroids and remove both if they do
        ArrayList<Rock> currAsteroids = new ArrayList<>(asteroids);
        for(Particle laser : lasers) {
            for(Rock asteroid : currAsteroids) {
                if(laser.hitBoxRectangle.overlaps(asteroid.hitBoxRectangle)) {
                    laser.remove();
                    asteroid.remove();
                    destroy(asteroid);
                    pointCalculation(asteroid);
                    asteroidBang(asteroid);
                }
            }
        }
        lasers.removeIf(laser -> laser.getStage() == null);
        asteroids.removeIf(asteroid -> asteroid.getStage() == null);

        // Checks for spaceship colliding with asteroids
        // and reduces spaceship health if true
        for(Rock e : asteroids) {
            if(e.hitBoxRectangle.overlaps(spaceship.hitBoxRectangle)) {
                spaceshipHealth--;
                healthLabel.setText("HEALTH: " + spaceshipHealth);
                // Set color of spaceship health
                if(spaceshipHealth <= 70 && spaceshipHealth >= 30) {
                    healthLabel.setColor(Color.YELLOW);
                } else if(spaceshipHealth < 30) {
                    healthLabel.setColor(Color.RED);
                }
                if(spaceshipHealth <= 0) {
                    spaceship.remove();
                    healthLabel.remove();
                    gameStage.addActor(gameOverLabel);
                    gameStage.addActor(gameOverInstructions);
                    playerDead();
                    spaceshipHealth = 0x7fffffff; // To stop the playerDead() from looping
                }
            }
        }

        // Checks amount of asteroids and adds four if none
        if(asteroids.size() == 0) {
            spawnFourAsteroids();
        }

        gameStage.draw();
    }

    private void playerDead() {
        Gdx.input.getTextInput(listener, "NAME", "", "");
    }

    private void pointCalculation(Rock asteroid) {
        float width = asteroid.getWidth();
        if(width == ROCK_WIDTH) {
            points += 100;
        } else if(width == ROCK_WIDTH/2) {
            points += 50;
        } else {
            points += 25;
        }
        pointsLabel.setText("POINTS: " + points);
    }

    private void asteroidBang(Rock asteroid) {
        float width = asteroid.getWidth();
        if(width == ROCK_WIDTH) {
            bangLarge.play(audioVolume);
        } else if(width == ROCK_WIDTH/2) {
            bangMedium.play(audioVolume);
        } else {
            bangSmall.play(audioVolume);
        }
    }

    /**
     * Creates four asteroids in a rectangular position with one in each corner on the screen.
     */
    private void spawnFourAsteroids() {
        asteroids.add(new Rock(200, 150));
        asteroids.add(new Rock(200, 450));
        asteroids.add(new Rock(600, 450));
        asteroids.add(new Rock(600, 150));

        for(Rock r : asteroids) {
            gameStage.addActor(r);
        }
    }

    private void createFont() {
        labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/Hyperspace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 48;
        fontParameter.color = Color.WHITE;
        fontParameter.borderWidth = 2;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderStraight = true;
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        labelStyle.font = fontGenerator.generateFont(fontParameter);
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
     * Adds the points earned from the current session to a list of all scores.
     * @param name The player chosen name
     * @param points The points earned in the session
     */
    private void scoreCalculation(String name, int points) {

        // If list is empty
        if(Asteroids.scoreList.size() == 0) {
            Asteroids.scoreList.add(new Asteroids.Score(name, points));
            return;
        }

        // If we overtook the highscore
        if(Asteroids.scoreList.getLast().getPoints() < points) {
            Asteroids.scoreList.add(new Asteroids.Score(name, points));
            return;
        }

        // Sorts the linked list
        for(int i = 0; i < Asteroids.scoreList.size(); i++) {
            if(Asteroids.scoreList.get(i).getPoints() >= points) {
                Asteroids.scoreList.add(i, new Asteroids.Score(name, points));
                break;
            }
        }

        // Restricts amount of scores to 10
        if(Asteroids.scoreList.size() > 10) {
            Asteroids.scoreList.remove();
        }

    }

    /**
     * The methods below are required of the InputProcessor interface
     */

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.SPACE && spaceship.getStage() != null) {
            Particle p = new Particle(spaceship);
            gameStage.addActor(p);
            lasers.add(p);
            spaceshipShoot.play(audioVolume);
        }
        if(keycode == Input.Keys.R) {
            Asteroids.setActiveScreen(new GameScreen());
        }
        if(keycode == Input.Keys.Q) {
            Asteroids.setActiveScreen(new MenuScreen());
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
        backgroundMusic.stop();
    }

    /*Required of the Sound interface*/
    @Override
    public long play() {
        return 0;
    }

    @Override
    public long play(float v) {
        return 0;
    }

    @Override
    public long play(float v, float v1, float v2) {
        return 0;
    }

    @Override
    public long loop() {
        return 0;
    }

    @Override
    public long loop(float v) {
        return 0;
    }

    @Override
    public long loop(float v, float v1, float v2) {
        return 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public void stop(long l) {

    }

    @Override
    public void pause(long l) {

    }

    @Override
    public void resume(long l) {

    }

    @Override
    public void setLooping(long l, boolean b) {

    }

    @Override
    public void setPitch(long l, float v) {

    }

    @Override
    public void setVolume(long l, float v) {

    }

    @Override
    public void setPan(long l, float v, float v1) {

    }
}
