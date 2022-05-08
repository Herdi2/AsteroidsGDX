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
import java.lang.Math.*;

/**
 * The spaceship class, creating the player controlled main game actor.
 */
public class Spaceship extends Actor {

    private final TextureRegion textureRegion;
    private final Texture texture;

    private float spaceshipX;
    private float spaceshipY;
    private Vector2 velocityVec;
    private float speed;

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private int mouseX;
    private int mouseY;
    private int originPosX;
    private int originPosY;
    private int distanceToMouseLimit;

    public Spaceship() {
        super();

        // Set the spaceship coordinates
        spaceshipX = 400;
        spaceshipY = 300;

        // Set spaceship velocity
        speed = 5.0f;
        velocityVec = new Vector2(1, 1);
        distanceToMouseLimit = 10;

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
            velocityVec.setAngle(getRotation() + 90);
            velocityVec = velocityVec.nor().scl(speed);

            // Avoids visual glitches when the cursor is in the middle of the spaceship
            originPosX = (int) (getX() + getOriginX());
            originPosY = (int) (getY() + getOriginY());
            if((originPosX <= mouseX + distanceToMouseLimit && originPosX >= mouseX - distanceToMouseLimit) &&
                    (originPosY <= mouseY + distanceToMouseLimit && originPosY >= mouseY - distanceToMouseLimit)) {
                moveBy(0, 0);
            } else {
                moveBy(velocityVec.x, velocityVec.y);
            }
        }

        // Bound spaceship to world
        boundSpaceshipToWorld();

        // Spaceship rotation
        rotate();
    }

    /**
     * Bounds the spaceship to the world by letting it wrap around
     * when going offscreen.
     */
    private void boundSpaceshipToWorld() {

        if(getX() + getHeight()/2 < 0) {
            setX(WINDOW_WIDTH);
        } else if(getX() > WINDOW_WIDTH) {
            setX(0);
        } else if(getY() > WINDOW_HEIGHT) {
            setY(0);
        } else if(getY() + getHeight() < 0) {
            setY(WINDOW_HEIGHT);
        }
    }

    /**s
     * Set the mouse position.
     *
     * @param screenX x coordinate of mouse
     * @param screenY y coordinate of mouse
     */
    public void setMouse(int screenX, int screenY) {
        this.mouseX = screenX;
        this.mouseY = WINDOW_HEIGHT - screenY;
    }

    /**
     * Rotate the spaceship according to the mouse position.
     */
    public void rotate() {
        /**
         * To understand the geometry of the game screen, the following source was used:
         * https://gamedev.stackexchange.com/questions/81810/accurate-sprite-rotation-with-mouse-movement
         */
        // Calculate angle of rotation
        float deltaX = (float) mouseX - getX() - getWidth()/2;
        float deltaY = (float) mouseY - getY() - getHeight()/2;
        float theta = MathUtils.radiansToDegrees * MathUtils.atan2(deltaY, deltaX);

        // Apply the rotation
        if(theta < 0) theta += 360;
        setRotation(theta - 90); // Offset for the spaceship png
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
