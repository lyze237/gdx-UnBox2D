# gdx-UnBox2D (Unity Box2D)

A libGDX library to couple Unity's behaviour system and execution order with [Box2D](https://box2d.org/) (and also [Dyn4j](https://dyn4j.org/)).

[![License](https://img.shields.io/github/license/lyze237/gdx-UnBox2D)](https://github.com/lyze237/gdx-UnBox2D/blob/main/LICENSE)
[![Jitpack](https://jitpack.io/v/lyze237/gdx-UnBox2D.svg)](https://jitpack.io/#lyze237/gdx-UnBox2D)
[![Donate](https://img.shields.io/badge/Donate-%3C3-red)](https://coffee.lyze.dev)
[![Donate](https://img.shields.io/badge/JavaDoc-blue)](https://coffee.lyze.dev)

## What is this whole thing about?

* Unity's Game Objects and Behaviour system is a lot of fun to work with
* This library tries to re-implement the [execution order loop](https://docs.unity3d.com/Manual/ExecutionOrder.html) and this whole Game Object and Behaviour system (start, update, ...)
* Everything is also coupled with Box2D:
  * Physics steps run at the correct time (fixedUpdate, ...)
  * All your Behaviours receive proper physics notifications (onCollisionEnter, ...)

## Installation and Tutorial

Check the [wiki](https://github.com/lyze237/gdx-UnBox2D/wiki) for infos!

## Example

```java
public class CoolGame extends Game {
    private Viewport viewport;

    private SpriteBatch batch;

    private UnBox unBox;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        viewport = new FitViewport(30, 10);
        viewport.getCamera().translate(-5, 0, 0);

        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();

        // Create an instance of the library, with no gravity
        unBox = new UnBox<>(new NoPhysicsWorld()); // Alternative if you want to use physics: new UnBox<>(new Box2dPhysicsWorld(new World(new Vector2(0, 0), true)));

        // Create two game objects, those get automatically added to the libraries instance
        var rightGo = new GameObject(unBox);
        var leftGo = new GameObject(unBox);
        
        // Attach a Box2D body
        new Box2dBehaviour(BodyDefType.DynamicBody, rightGo);
        new Box2dBehaviour(BodyDefType.DynamicBody, leftGo);

        // Attach a logging behaviour to both of the game objects
        new SoutBehaviour("Right GO", false, rightGo);
        new SoutBehaviour("Left GO", false, leftGo);

        // Attach a movement behaviour to both game objects
        new MoveBehaviour(true, rightGo);
        new MoveBehaviour(false, leftGo);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Step through physics and update loops
        unBox.preRender(Gdx.graphics.getDeltaTime());

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Render the state
        batch.begin();
        unBox.render(batch);
        batch.end();

        // Debug render all Box2d bodies (if you are using a physics world)
        //debugRenderer.render(unBox.getPhysicsWorld().getWorld(), viewport.getCamera().combined);

        // Clean up render loop
        unBox.postRender();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
```
