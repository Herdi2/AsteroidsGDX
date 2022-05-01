import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Spaceship extends Actor {

    private final TextureRegion textureRegion;
    private final Texture texture;
    private float spaceshipX;
    private float spaceshipY;

    public Spaceship() {
        super();

        // Set the spaceship coordinates
        spaceshipX = 400;
        spaceshipY = 300;

        // Load in the spaceship textures
        FileHandle spaceshipFile = new FileHandle("assets/spaceship.png");
        textureRegion = new TextureRegion();
        texture = new Texture(spaceshipFile);
        textureRegion.setRegion(texture);
        this.setPosition(spaceshipX, spaceshipY);
        setSize(texture.getWidth(), texture.getHeight());

        // Sets origin to be the middle of the spaceship
        setOrigin(getOriginX() + (float) texture.getWidth()/2, getOriginY() + (float) texture.getHeight()/2);
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
        batch.draw(textureRegion,
                getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
