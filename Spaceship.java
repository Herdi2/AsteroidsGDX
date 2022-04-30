import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Spaceship extends Actor {

    private final TextureRegion texture;
    private float spaceshipX;
    private float spaceshipY;

    public Spaceship() {
        super();

        // Set the spaceship coordinates
        spaceshipX = 400;
        spaceshipY = 300;

        // Load in the spaceship textures
        FileHandle spaceshipFile = new FileHandle("assets/spaceship.png");
        texture = new TextureRegion();
        texture.setRegion(new Texture(spaceshipFile));
        this.setPosition(spaceshipX, spaceshipY);
        setSize(50, 50);
    }

    /**
     * Automatically gets called by stage. Is used to make the actor act.
     *
     * @param dt
     */
    public void act(float dt) {

        // Check user input
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveBy(0, 5);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveBy(0, -5);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveBy(5, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveBy(-5, 0);
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
        batch.draw(texture,
                getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
