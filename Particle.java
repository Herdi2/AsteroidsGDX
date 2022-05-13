import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Particle extends Actor {

    private final TextureRegion textureRegion;
    private final Texture texture;
    private Vector2 velocityVec;
    public Rectangle hitBoxRectangle;

    public Particle(Spaceship spaceship) {
        super();

        // Load in the particle textures
        FileHandle particleFile = new FileHandle("assets/particle.png");
        textureRegion = new TextureRegion();
        texture = new Texture(particleFile);
        textureRegion.setRegion(texture);
        setHitbox(getX(), getY());

        // Sets origin to be the middle of the particle
        setOrigin(getX() + (float) texture.getWidth()/2, getY() + (float) texture.getHeight()/2);

        // Set the position of the particle
        Vector2 v = new Vector2(10, 10);
        v.setAngle(spaceship.getRotation());
        this.setPosition(spaceship.getX() + spaceship.getWidth()/2, spaceship.getY() + spaceship.getHeight()/2);
        setSize(2, 2);


        // Sets the motion angle and speed
        velocityVec = new Vector2(5, 5);
        velocityVec.setAngle(spaceship.getRotation() - 90);

        // The particles are faded in to appears as though the shoot out from the front
        addAction(Actions.fadeOut(0f));
        addAction(Actions.fadeIn(0.1f));

    }

    /**
     * Automatically gets called by stage. Is used to make the actor act.
     *
     * @param dt
     */
    public void act(float dt) {
        super.act(dt);
        moveBy(-velocityVec.x, -velocityVec.y);
        setHitbox(getX(), getY());
    }

    /**
     * Creates a rectangular hitbox around the particle
     */
    private void setHitbox(float x, float y) {
        hitBoxRectangle = new Rectangle(x, y, getWidth(), getHeight());
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

