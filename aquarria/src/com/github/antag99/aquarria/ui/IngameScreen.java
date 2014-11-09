package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.antag99.aquarria.Aquarria;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.gdx.GraphicsDelegate;
import com.github.antag99.aquarria.ui.world.WorldRenderer;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldGenerator;
import com.github.antag99.aquarria.world.WorldView;

public class IngameScreen extends AquarriaScreen {
	public static final float PIXELS_PER_METER = 16;

	private World world;
	private WorldView worldView;
	private WorldRenderer worldRenderer;
	
	private PlayerEntity player;
	private Skin skin;
	private IngameInterface ingameInterface;
	
	private Vector2 tmpVector2 = new Vector2();
	private Vector3 tmpVector3 = new Vector3();
	
	private float debugGameSpeed = 1f;
	private GraphicsDelegate debugGraphics;

	public IngameScreen(Aquarria aquarria) {
		super(aquarria);
	}
	
	@Override
	public void load() {
		super.load();
		
		SkinParameter skinParameter = new SkinParameter("images/ui/atlas/ui.atlas");
		AssetManager assetManager = aquarria.getAssetManager();
		assetManager.load("skins/ingame.json", Skin.class, skinParameter);
		
		worldRenderer = new WorldRenderer();
		worldRenderer.queueAssets(assetManager);
		
		debugGraphics = new GraphicsDelegate(Gdx.graphics) {
			@Override
			public float getDeltaTime() {
				return super.getDeltaTime() * debugGameSpeed;
			}
		};
		Gdx.graphics = debugGraphics;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		AssetManager assetManager = aquarria.getAssetManager();
		skin = assetManager.get("skins/ingame.json");
		worldRenderer.getAssets(assetManager);
		
		world = new World(1024, 512);
		new WorldGenerator().generate(world, MathUtils.random.nextLong());
		
		player = new PlayerEntity();
		player.setX(world.getSpawnX());
		player.setY(world.getSpawnY());
		world.addEntity(player);
		
		worldView = new WorldView();
		worldView.setWorld(world);
		
		worldRenderer.setView(worldView);
		
		ingameInterface = new IngameInterface(skin);
		ingameInterface.setPlayer(player);
		
		root.stack(worldRenderer, ingameInterface).expand().fill();
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			worldRenderer.setDrawTileGrid(!worldRenderer.getDrawTileGrid());
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			worldRenderer.setDrawEntityBoxes(!worldRenderer.getDrawEntityBoxes());
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F7)) {
			debugGameSpeed *= 0.5f;
			if(debugGameSpeed < 0.125f) {
				debugGameSpeed = 4f;
			}
			System.out.println("Game speed: " + (int)(debugGameSpeed * 100) + "%");
		}
		
		delta = Gdx.graphics.getDeltaTime();
		
		world.update(delta);

		OrthographicCamera cam = worldView.getCamera();

		cam.position.x = MathUtils.clamp(player.getX() + player.getWidth() * 0.5f, cam.viewportWidth / 2f, world.getWidth() - cam.viewportWidth / 2f);
		cam.position.y = MathUtils.clamp(player.getY() + player.getHeight() * 0.5f, cam.viewportHeight / 2f, world.getHeight() - cam.viewportHeight / 2f);
		cam.zoom = 1f;

		cam.update();
		
		Vector2 mousePosition = tmpVector2.set(Gdx.input.getX(), Gdx.input.getY());
		mousePosition = aquarria.getStage().screenToStageCoordinates(mousePosition);
		if(aquarria.getStage().hit(mousePosition.x, mousePosition.y, true) == null) {
			// The camera handles the different coordinate systems too, reset to input coordinates
			mousePosition = tmpVector2.set(Gdx.input.getX(), Gdx.input.getY());
			Vector3 worldFocus = cam.unproject(tmpVector3.set(mousePosition, 0f));
			if(worldFocus.x >= 0f && worldFocus.y >= 0f &&
					worldFocus.x < world.getWidth() &&
					worldFocus.y < world.getHeight()) {
				player.setWorldFocus(worldFocus.x, worldFocus.y);
			}
		} else {
			player.setWorldFocus(null);
		}
		
		if(!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
		if(player.getWorldFocus() != null) {
			if(Gdx.input.justTouched()) {
				if(!player.getRepeatUsingItem()) {
					player.setRepeatUsingItem(true);
				}
				player.setUsedItem(ingameInterface.getFocusItem());
			}
			
			if(!Gdx.input.isTouched()) {
				if(player.getRepeatUsingItem()) {
					player.setRepeatUsingItem(false);
				}
			}
		} else {
			player.setRepeatUsingItem(false);
			player.setUsingItem(false);
			player.setUseTime(0f);
			player.setUsedItem(null);
		}
		} else if(player.getWorldFocus() != null){
			int liquidX = (int)player.getWorldFocus().x;
			int liquidY = (int)player.getWorldFocus().y;
			
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				world.getLiquidManager().setLiquid(liquidX, liquidY, 255);
			} else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				world.getLiquidManager().setLiquid(liquidX, liquidY, 0);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		OrthographicCamera cam = worldView.getCamera();
		cam.viewportWidth = width / PIXELS_PER_METER;
		cam.viewportHeight = height / PIXELS_PER_METER;
		cam.update();
		super.resize(width, height);
	}
	
	@Override
	public void show() {
		super.show();
		Stage stage = aquarria.getStage();
		stage.setKeyboardFocus(ingameInterface);
		stage.setScrollFocus(ingameInterface);
	}
}
