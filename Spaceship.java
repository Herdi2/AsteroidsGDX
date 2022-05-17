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

/**
 * The spaceship class, creating the player controlled main game actor.
 */
public class Spaceship extends Actor {

    private final TextureRegion textureRegion;
    private final Texture texture;

    private float spaceshipX;
    private float spaceshipY;
    private Vector2 velocityVec;
    private Vector2 accelVec;
    private float maxSpeed;
    private float acceleration;
    private float deceleration;
    public Rectangle hitBoxRectangle;



    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private int mouseX;
    private int mouseY;

    public Spaceship() {
        super();

        // Set the spaceship coordinates
        spaceshipX = 400;
        spaceshipY = 300;

        // Set spaceship velocity
        acceleration = 250;
        deceleration = 150;
        maxSpeed = 200;
        velocityVec = new Vector2(0, 0);
        accelVec = new Vector2(0, 0);

        // Load in the spaceship textures
        FileHandle spaceshipFile = new FileHandle("assets/spaceship.png");
        textureRegion = new TextureRegion();
        texture = new Texture(spaceshipFile);
        textureRegion.setRegion(texture);
        this.setPosition(spaceshipX, spaceshipY);
        setSize(32, 48);
        setHitbox(spaceshipX, spaceshipY);

        // Sets origin to be the middle of the spaceship
        setOrigin(getOriginX() + (float) getWidth()/2, getOriginY() + (float) getHeight()/2);
    }

    /**
     * Automatically gets called by stage. Is used to make the actor act.
     *
     * @param dt
     */
    public void act(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerate(dt);
        }

        boundSpaceshipToWorld();
        rotate();
        applyPhysics(dt);

        setHitbox(getX(), getY());

    }

    /**
     * Creates a rectangular hitbox around the spaceship
     */
    private void setHitbox(float x, float y) {
        hitBoxRectangle = new Rectangle(x, y, getWidth(), getHeight());
    }

    /**
     * Accelerate the spaceship in the direction it is pointing at.
     *
     * @param dt time between frames
     */
    private void accelerate(float dt) {
        accelVec.x = acceleration;
        accelVec.setAngle(getRotation() + 90);
        velocityVec.add(accelVec.scl(dt));
    }

    /**
     * Applies physics to the spaceship, to make it decelerate and
     * hold its speed between 0 and maxSpeed.
     *
     * This method of accelerating an object was taken from
     * Lee Stemkoski's "Java Game Development with LibGDX"
     *
     * @param dt time between frames
     */
    private void applyPhysics(float dt) {
        float speed = velocityVec.len();

        if(accelVec.len() == 0) {
            speed -= deceleration * dt;
        }

        speed = MathUtils.clamp(speed, 0, maxSpeed);

        velocityVec.setLength(speed);

        moveBy(velocityVec.x * dt, velocityVec.y * dt);

        accelVec.setZero();
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

    /**
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
