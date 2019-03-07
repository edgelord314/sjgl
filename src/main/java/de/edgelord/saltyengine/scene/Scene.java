/*
 * Copyright 2018 Malte Dostal
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.edgelord.saltyengine.scene;

import de.edgelord.saltyengine.components.SimplePhysicsComponent;
import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.core.physics.Force;
import de.edgelord.saltyengine.effect.light.LightSystem;
import de.edgelord.saltyengine.gameobject.DrawingRoutine;
import de.edgelord.saltyengine.gameobject.FixedTask;
import de.edgelord.saltyengine.gameobject.GameObject;
import de.edgelord.saltyengine.transform.Coordinates2f;
import de.edgelord.saltyengine.ui.UISystem;

import java.awt.geom.AffineTransform;
import java.util.*;

/**
 * This class represents what is currently drawn and calculated.
 * This includes:
 * {@link GameObject}s within {@link #gameObjects},
 * {@link FixedTask}s within {@link #fixedTasks},
 * {@link DrawingRoutine}s within {@link #drawingRoutines}
 * and the {@link UISystem} {@link #ui}
 * as well as a {@link LightSystem} stored in {@link #lightSystem}
 * <p>
 * The current scene is stored in {@link SceneManager#getCurrentScene()}.
 * For more information, please take a look at the documentation of that class.
 * <p>
 * IMPORTANT: Do nothing with GFX in any implementations of this class. If you do so, these GFX will be applied to the
 * scene that was active before!
 */
public class Scene {

    public static final Object concurrentBlock = "3141592653589793";

    private float gravity = SimplePhysicsComponent.DEFAULT_GRAVITY_ACCELERATION;
    private float friction = Force.DEFAULT_FRICTION;
    private boolean gravityEnabled = false;

    private List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());
    private List<FixedTask> fixedTasks = Collections.synchronizedList(new ArrayList<>());
    private List<DrawingRoutine> drawingRoutines = Collections.synchronizedList(new ArrayList<>());
    private LightSystem lightSystem = null;
    private UISystem ui = new UISystem();

    public Scene() {

    }

    /**
     * Returns the first {@link GameObject} found in this scene with the given tag.
     *
     * @param tag the tag
     * @return the first found <code>GameObject</code> with the given tag
     */
    public GameObject getGameObjectByTag(String tag) {
        synchronized (concurrentBlock) {
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject gameObject = gameObjects.get(i);

                if (gameObject.getTag().equals(tag)) {
                    return gameObject;
                }
            }
        }

        return null;
    }

    /**
     * Returns all {@link GameObject}s in this <code>Scene</code> with the given tag.
     *
     * @param tag the tag
     * @return an array of all the <code>GameObject</code>s with the given tag
     */
    public GameObject[] getGameObjectsByTag(String tag) {

        ArrayList<GameObject> objects = new ArrayList<>();

        synchronized (concurrentBlock) {
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject gameObject = gameObjects.get(i);

                if (gameObject.getTag().equals(tag)) {
                    objects.add(gameObject);
                }
            }
        }

        return (GameObject[]) objects.toArray();
    }

    public void disableGravity() {
        synchronized (concurrentBlock) {
            gravityEnabled = false;
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject gameObject = gameObjects.get(i);

                gameObject.getPhysics().setGravityEnabled(false);
            }
        }
    }

    public void enableGravity() {
        synchronized (concurrentBlock) {
            gravityEnabled = true;
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject gameObject = gameObjects.get(i);

                gameObject.getPhysics().setGravityEnabled(true);
            }
        }
    }

    public void addFixedTask(FixedTask fixedTask) {

        synchronized (concurrentBlock) {
            fixedTasks.add(fixedTask);
        }
    }

    public void addDrawingRoutine(DrawingRoutine drawingRoutine) {
        synchronized (concurrentBlock) {
            drawingRoutines.add(drawingRoutine);
        }
    }

    public void addGameObject(GameObject gameObject) {

        synchronized (concurrentBlock) {
            gameObject.getPhysics().setGravityEnabled(gravityEnabled);
            gameObjects.add(gameObject);
        }
    }

    public void addGameObject(int index, GameObject gameObject) {
        synchronized (concurrentBlock) {
            gameObject.getPhysics().setGravityEnabled(gravityEnabled);
            gameObjects.add(index, gameObject);
        }
    }

    public void removeGameObject(GameObject gameObject) {
        synchronized (concurrentBlock) {
            gameObjects.remove(gameObject);
        }
    }

    public void clearGameObjects() {
        synchronized (concurrentBlock) {
            gameObjects.clear();
        }
    }

    public void removeFixedTask(FixedTask fixedTask) {
        synchronized (concurrentBlock) {
            fixedTasks.remove(fixedTask);
        }
    }

    public void clearFixedTasks() {
        synchronized (concurrentBlock) {
            fixedTasks.clear();
        }
    }

    public void removeDrawingRoutine(DrawingRoutine drawingRoutine) {
        synchronized (concurrentBlock) {
            drawingRoutines.remove(drawingRoutine);
        }
    }

    public void clearDrawingRoutines() {
        synchronized (concurrentBlock) {
            drawingRoutines.clear();
        }
    }

    public void doFixedTasks() {

        synchronized (concurrentBlock) {
            for (FixedTask fixedTask : fixedTasks) {

                fixedTask.onFixedTick();
            }
        }
    }

    public void draw(SaltyGraphics saltyGraphics) {

        synchronized (concurrentBlock) {
            for (DrawingRoutine drawingRoutine : drawingRoutines) {
                if (drawingRoutine.getDrawingPosition() == DrawingRoutine.DrawingPosition.BEFORE_GAMEOBJECTS) {
                    drawingRoutine.draw(saltyGraphics);
                }
            }
        }

        synchronized (concurrentBlock) {
            for (GameObject gameObject : gameObjects) {
                AffineTransform before = saltyGraphics.getGraphics2D().getTransform();
                Coordinates2f rotationCentre = gameObject.getTransform().getRotationCentreAbsolute();
                saltyGraphics.setRotation(gameObject.getRotationDegrees(), rotationCentre);

                gameObject.draw(saltyGraphics);
                gameObject.doComponentDrawing(saltyGraphics);

                saltyGraphics.setTransform(before);
            }
        }

        synchronized (concurrentBlock) {
            for (DrawingRoutine drawingRoutine : drawingRoutines) {
                if (drawingRoutine.getDrawingPosition() == DrawingRoutine.DrawingPosition.AFTER_GAMEOBJECTS) {
                    drawingRoutine.draw(saltyGraphics);
                }
            }
        }

        Game.getCamera().tmpResetViewToGraphics(saltyGraphics);

        if (lightSystem != null) {
            lightSystem.draw(saltyGraphics);
        }

        if (ui != null) {
            ui.drawUI(saltyGraphics);
        }

        Game.getDefaultGFXController().doGFXDrawing(saltyGraphics);
    }

    public void onFixedTick() {

        doFixedTasks();

        synchronized (concurrentBlock) {

            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject gameObject = gameObjects.get(i);

                if (!gameObject.isInitialized()) {
                    gameObject.initialize();
                    gameObject.setInitialized(true);
                }

                gameObject.doCollisionDetection(gameObjects);
                gameObject.doComponentOnFixedTick();
                gameObject.doFixedTick();
            }
        }

        Game.getDefaultGFXController().doGFXFixedTick();

        if (ui != null) {

            ui.onFixedTick();
        }
    }

    public void setUI(UISystem uiSystem) {
        this.ui = uiSystem;
    }

    public UISystem getUI() {
        return ui;
    }

    public int getGameObjectCount() {
        synchronized (concurrentBlock) {
            return gameObjects.size();
        }
    }

    public int getDrawingRoutineCount() {
        synchronized (concurrentBlock) {
            return drawingRoutines.size();
        }
    }

    public int getFixedTaskCount() {
        synchronized (concurrentBlock) {
            return fixedTasks.size();
        }
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setGravityEnabled(boolean gravityEnabled) {
        if (gravityEnabled) {
            enableGravity();
        } else {
            disableGravity();
        }
    }

    public LightSystem getLightSystem() {
        return lightSystem;
    }

    public void setLightSystem(LightSystem lightSystem) {
        this.lightSystem = lightSystem;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<FixedTask> getFixedTasks() {
        return fixedTasks;
    }

    public List<DrawingRoutine> getDrawingRoutines() {
        return drawingRoutines;
    }
}
