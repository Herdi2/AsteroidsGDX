import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The Rock class creates the asteroids/space rocks, the main "enemy".
 */
public class Rock extends Actor{

    private final TextureRegion textureRegion;
    private final Texture texture;
    private final String[] asteroidTypes;
    public Rectangle hitBoxRectangle;

    private int maxSpeed;
    private float xSpeed;
    private float ySpeed;

    public Rock(float x, float y) {
        super();

        asteroidTypes = new String[]{"assets/asteroid1.png", "assets/asteroid2.png", "assets/asteroid3.png"};
        // Load in the asteroid textures
        FileHandle asteroidFile = new FileHandle(asteroidTypes[ThreadLocalRandom.current().nextInt(0, 3)]);
        textureRegion = new TextureRegion();
        texture = new Texture(asteroidFile);
        textureRegion.setRegion(texture);
        setSize(texture.getWidth(), texture.getHeight());
        setHitbox(x, y);

        // Set position
        setPosition(x, y);

        // Set speed
        maxSpeed = 3;
        xSpeed = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed);
        ySpeed = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed);

        // Sets origin to be the middle of the asteroid
        setOrigin(getOriginX() + (float) texture.getWidth()/2, getOriginY() + (float) texture.getHeight()/2);
    }

    /**
     * Automatically gets called by stage. Is used to make the actor act.
     *
     * @param dt
     */
    public void act(float dt) {
        moveBy(xSpeed, ySpeed);
        boundSpaceshipToWorld();
        setHitbox(getX(), getY());
    }

    /**
     * Creates a rectangular hitbox around the asteroid
     */
    private void setHitbox(float x, float y) {
        hitBoxRectangle = new Rectangle(x, y, getWidth(), getHeight());
    }

    /**
     * Bounds the asteroid to the world by letting it wrap around
     * when going offscreen.
     */
    private void boundSpaceshipToWorld() {

        if(getX() + getHeight()/2 < 0) {
            setX(Launcher.WINDOW_WIDTH);
        } else if(getX() > Launcher.WINDOW_WIDTH) {
            setX(0);
        } else if(getY() > Launcher.WINDOW_HEIGHT) {
            setY(0);
        } else if(getY() + getHeight() < 0) {
            setY(Launcher.WINDOW_HEIGHT);
        }
    }

    /**
     * Is automatically called by the stage and draws the texture.
     *
     * @param batch
     * @param parentAlpha
     */
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw(textureRegion,
                getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
