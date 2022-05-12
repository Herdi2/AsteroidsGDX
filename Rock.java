import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

    public Rock(float x, float y) {
        super();
        System.out.println("ROCK SPAWNED");
        asteroidTypes = new String[]{"assets/asteroid1.png", "assets/asteroid2.png", "assets/asteroid3.png"};
        // Load in the asteroid textures
        FileHandle asteroidFile = new FileHandle(asteroidTypes[ThreadLocalRandom.current().nextInt(0, 3)]);
        textureRegion = new TextureRegion();
        texture = new Texture(asteroidFile);
        textureRegion.setRegion(texture);
        setSize(texture.getWidth(), texture.getHeight());

        // Set position
        setPosition(x, y);

        // Sets origin to be the middle of the asteroid
        setOrigin(getOriginX() + (float) texture.getWidth()/2, getOriginY() + (float) texture.getHeight()/2);
    }

    /**
     * Automatically gets called by stage. Is used to make the actor act.
     *
     * @param dt
     */
    public void act(float dt) {


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
