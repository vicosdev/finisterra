package object;

import game.handlers.AssetHandler;
import game.handlers.HandlerState;
import game.screens.CharacterScreen;
import game.systems.render.world.AnimationRenderingSystem;
import object.systems.FaceChangerSystem;
import game.systems.anim.IdleAnimationSystem;
import game.systems.anim.MovementAnimationSystem;
import game.systems.camera.CameraFocusSystem;
import game.systems.camera.CameraMovementSystem;
import game.systems.camera.CameraSystem;
import game.systems.physics.PlayerInputSystem;
import com.artemis.Entity;
import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

import static com.artemis.E.E;

public class ObjectCreator extends Game {

    private static final float GAME_SCREEN_ZOOM = 4f;

    private PolygonSpriteBatch spriteBatch; // This is only used in GameScreen

    private World world;

    private int player;

    @Override
    public void create() {
        Gdx.app.debug("AOGame", "Opening Objects Creator...");
        AssetHandler.load();
        if(AssetHandler.getState() == HandlerState.LOADED)
            Gdx.app.debug("AOGame", "Handler loaded!");
        this.spriteBatch = new PolygonSpriteBatch();
        initWorld();
        postWorldInit();
        addCharacter();
        setScreen(new CharacterScreen(world, player));
    }

    private void initWorld() {
        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder();
        worldConfigBuilder.with(new SuperMapper())
                // Player movement
                .with(new PlayerInputSystem())
                .with(new FaceChangerSystem())
                .with(new MovementAnimationSystem())
                .with(new IdleAnimationSystem())
                // Camera
                .with(new CameraSystem(ObjectCreator.GAME_SCREEN_ZOOM))
                .with(new CameraFocusSystem())
                .with(new CameraMovementSystem())
                // Logic systems
                .with(new TagManager())
                // Rendering
                .with(WorldConfigurationBuilder.Priority.NORMAL + 2, new AnimationRenderingSystem(spriteBatch));
//                .with(WorldConfigurationBuilder.Priority.NORMAL + 2, new CharacterRenderingSystem(spriteBatch));

        world = new World(worldConfigBuilder.build()); // preload Artemis world
    }

    private void postWorldInit() {
        Entity cameraEntity = world.createEntity();
        E(cameraEntity)
                .aOCamera(true)
                .pos2D();
        world.getSystem(TagManager.class).register("camera", cameraEntity);
    }

    private void addCharacter() {
        player = world.create();
        E(player)
                .headIndex(4)
                .bodyIndex(101)
                .shieldIndex(1)
                .weaponIndex(3)
                .heading()
                .moving(true)
                .aOPhysics()
                .focused()
                .worldPos()
                .character();
    }

    public int getPlayer() {
        return player;
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

}