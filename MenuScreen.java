import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MenuScreen extends Game implements InputProcessor, Screen {

    private Stage uiStage;
    private InputMultiplexer im;
    private static Label.LabelStyle labelStyle;
    private Label asteroidsLabel;
    private TextButton startButton;

    @Override
    public void create() {
        uiStage = new Stage();

        // Create the font and labels
        createFont();
        asteroidsLabel = new Label("ASTEROIDS", labelStyle);
        asteroidsLabel.setColor(Color.WHITE);
        asteroidsLabel.setPosition(Launcher.WINDOW_WIDTH/2 - asteroidsLabel.getWidth()/2, Launcher.WINDOW_HEIGHT-200);
        uiStage.addActor(asteroidsLabel);

        // Create start button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.GRAY;
        textButtonStyle.downFontColor = Color.GRAY;
        textButtonStyle.font = labelStyle.font;
        startButton = new TextButton("START", textButtonStyle);
        startButton.setPosition(Launcher.WINDOW_WIDTH/2 - startButton.getWidth()/2, Launcher.WINDOW_HEIGHT - 300);
        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(event.getType().equals(InputEvent.Type.touchDown)) {
                    Asteroids.setActiveScreen(new GameScreen());
                    return false;
                }
                return false;
            }
        }
        );
        uiStage.addActor(startButton);

        // Create quit button
        TextButton quitButton = new TextButton("QUIT", textButtonStyle);
        quitButton.setPosition(Launcher.WINDOW_WIDTH/2 - quitButton.getWidth()/2, Launcher.WINDOW_HEIGHT - 400);
        quitButton.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        if(event.getType().equals(InputEvent.Type.touchDown)) {
                                            System.exit(0);
                                        }
                                        return false;
                                    }
                                }
        );
        uiStage.addActor(quitButton);

        // Setup an inputprocessor to handle input events
        im = new InputMultiplexer();
        im.addProcessor(uiStage);
        im.addProcessor(this);
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void show() { create(); }


    @Override
    public void render(float dt) {
        uiStage.act(dt);

        asteroidsLabel.setText("ASTEROIDS");
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiStage.draw();
    }

    private void createFont() {
        labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/Vonique 64.ttf"));
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

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ENTER) {
            Asteroids.setActiveScreen(new GameScreen());
        }
        if(keycode == Input.Keys.ESCAPE) {
            System.exit(0);
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
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void hide() {

    }
}
