package com.mygdx.game.game;

import static com.mygdx.game.game.GameSettings.POSITION_ITERATIONS;
import static com.mygdx.game.game.GameSettings.STEP_TIME;
import static com.mygdx.game.game.GameSettings.VELOCITY_ITERATIONS;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.FontBuilder;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.managers.AudioManager;
import com.mygdx.game.screens.SettingsScreen;


public class MyGdxGame extends Game {
	public static final Object FONT_PATH = 1;
	public SpriteBatch batch;

	public OrthographicCamera camera;

	public BitmapFont  commonWhiteFont;
	public BitmapFont largeWhiteFont;

	public BitmapFont commonBlackFont;

	public World world;

	public GameScreen gameScreen;

	float accumulator = 0;

	public Vector3 touch;

	public MenuScreen menuScreen;
	public SettingsScreen settingsScreen;
	public AudioManager audioManager;







	@Override
	public void create () {
		Box2D.init();
		world = new World(new Vector2(0, -10), true);


		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);
		largeWhiteFont = FontBuilder.generate(48, Color.WHITE, GameResources.FONT_PATH);
		commonBlackFont = FontBuilder.generate(24, Color.BLACK, GameResources.FONT_PATH);




		batch = new SpriteBatch();
		audioManager = new AudioManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		settingsScreen = new SettingsScreen(this);


		setScreen(menuScreen);
	}

	public void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += delta;

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}



	@Override
	public void dispose () {
		batch.dispose();
	}
}