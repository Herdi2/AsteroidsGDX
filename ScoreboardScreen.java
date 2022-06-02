import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ScoreboardScreen extends Game implements InputProcessor, Screen {

    private Stage uiStage;
    private InputMultiplexer im;
    private static Label.LabelStyle labelStyle;
    private Label scoreboardTitle;
    private Label scoreboard;
    private Music backgroundMusic;
    private float audioVolume;

    @Override
    public void create() {
        uiStage = new Stage();
        createFont(48);

        // Creates the scoreboard title
        scoreboardTitle = new Label("SCOREBOARD", labelStyle);
        scoreboardTitle.setColor(Color.WHITE);
        scoreboardTitle.setPosition(Launcher.WINDOW_WIDTH/2 - scoreboardTitle.getWidth()/2, Launcher.WINDOW_HEIGHT - scoreboardTitle.getHeight());
        uiStage.addActor(scoreboardTitle);
        scoreboardTitle.setText("SCOREBOARD");

        createFont(24);
        // Creates the back button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.GRAY;
        textButtonStyle.downFontColor = Color.GRAY;
        textButtonStyle.font = labelStyle.font;
        TextButton backButton = new TextButton("BACK", textButtonStyle);
        backButton.setPosition(backButton.getWidth()/2, backButton.getHeight()/2);
        backButton.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       if(event.getType().equals(InputEvent.Type.touchDown)) {
                                           Asteroids.setActiveScreen(new MenuScreen());
                                       }
                                       return false;
                                   }
                               }
        );
        uiStage.addActor(backButton);

        // Create the scoreboard
        createFont(20);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(int j = Asteroids.scoreList.size() - 1; j >= 0; j--) {
            Asteroids.Score e = Asteroids.scoreList.get(j);
            sb.append(e.getName());
            sb.append(" ");
            sb.append(e.getPoints());
            sb.append("\n");
            i++;
        }
        for(int j = 0; j < 10 - i; j++) {
            sb.append("___");
            sb.append(" ");
            sb.append("___");
            sb.append("\n");
        }
        String scoreText = sb.toString();
        scoreboard = new Label(scoreText, labelStyle);
        scoreboard.setColor(Color.WHITE);
        scoreboard.setPosition(Launcher.WINDOW_WIDTH/2 - scoreboard.getWidth()/2, Launcher.WINDOW_HEIGHT - scoreboard.getHeight() - scoreboardTitle.getHeight() - 50);
        uiStage.addActor(scoreboard);
        scoreboard.setText(scoreText);

        // Creates background music
        audioVolume = 0.1f;
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Trouble-on-Mercury_Looping.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(audioVolume);
        backgroundMusic.play();

        // Setup an inputprocessor to handle input events
        im = new InputMultiplexer();
        im.addProcessor(uiStage);
        im.addProcessor(this);
        Gdx.input.setInputProcessor(im);
    }

    private void createFont(int fontSize) {
        labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/Hyperspace.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = fontSize;
        fontParameter.color = Color.WHITE;
        fontParameter.borderWidth = 2;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderStraight = true;
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        labelStyle.font = fontGenerator.generateFont(fontParameter);
    }

    @Override
    public void render(float dt) {
        uiStage.act(dt);

        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiStage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            Asteroids.setActiveScreen(new MenuScreen());
        }
        return false;
    }

    @Override
    public void show() {
        create();
    }

    @Override
    public void hide() {
        backgroundMusic.stop();
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

}
