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

package de.edgelord.saltyengine.ui;

import de.edgelord.saltyengine.components.Component;
import de.edgelord.saltyengine.core.annotations.DefaultPlacement;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.core.interfaces.Drawable;
import de.edgelord.saltyengine.core.interfaces.FixedTickRoutine;
import de.edgelord.saltyengine.core.stereotypes.ComponentContainer;
import de.edgelord.saltyengine.input.KeyboardInputHandler;
import de.edgelord.saltyengine.input.MouseInputHandler;
import de.edgelord.saltyengine.transform.Coordinates;
import de.edgelord.saltyengine.transform.Dimensions;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.utils.SaltySystem;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@DefaultPlacement(method = DefaultPlacement.Method.TOP_LEFT_CORNER)
public abstract class UIElement extends ComponentContainer implements Drawable, FixedTickRoutine, MouseInputHandler, KeyboardInputHandler {

    public static final String BUTTON = "de.edgelord.saltyengine.uiElements.button";
    public static final String LABEL = "de.edgelord.saltyengine.uiElements.label";
    public static final String TEXT_BOX = "de.edgelord.saltyengine.uiElements.textBox";
    public static final String CONTAINER = "de.edgelord.saltyengine.uiElements.container";
    public static final String STATE_DISPLAY_ELEMENT = "de.edgelord.saltyengine.uiElements.stateDisplayElement";
    public static final String SETTINGS_ELEMENT = "de.edgelord.saltyengine.uiElements.settingsElement";
    public static final String SWING_ELEMENT = "de.edgelord.saltyengine.uiElements.swingElement";
    private final List<Component> components = new CopyOnWriteArrayList<>();
    private Font font = SaltySystem.defaultFont;
    private Color backgroundColor = Color.DARK_GRAY;
    private Color foregroundColor = Color.WHITE;
    private boolean mouseHoversOver = false;
    private boolean suppressClipping = false;
    private boolean focused = false;

    public UIElement(final Vector2f position, final float width, final float height, final String tag) {
        super(tag);

        setTransform(new Transform(position, new Dimensions(width, height)));
    }

    public UIElement(final Transform transform, final String tag) {
        this(transform.getPosition(), transform.getWidth(), transform.getHeight(), tag);
    }

    @Override
    public void onFixedTick() {
    }

    /**
     * A final implementation that calls both {@link #drawBackground(SaltyGraphics)}
     * and {@link #drawForeground(SaltyGraphics)} with the graphics being
     * prepared correctly.
     *
     * @param saltyGraphics the graphics ot render to.
     */
    @Override
    public final void draw(final SaltyGraphics saltyGraphics) {
        prepareGraphics(saltyGraphics);
        drawBackground(saltyGraphics);
        saltyGraphics.setColor(getForegroundColor());
        drawForeground(saltyGraphics);
    }

    /**
     * Draws the background of the ui element.
     *
     * @param saltyGraphics the graphics to draw to.
     */
    public abstract void drawBackground(SaltyGraphics saltyGraphics);

    /**
     * Draws the foreground of this ui element.
     *
     * @param saltyGraphics the graphics to draw to.
     */
    public abstract void drawForeground(SaltyGraphics saltyGraphics);

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
    }

    @Override
    public void mouseExitedScreen(final MouseEvent e) {
    }

    public void mouseExited(final Transform cursor) {
    }

    @Override
    public void mouseEnteredScreen(final MouseEvent e) {
    }

    public void mouseEntered(final Transform cursor) {
    }

    @Override
    public void mouseWheelMoved(final MouseEvent e) {
    }

    public void mouseHover(final Transform cursor) {
    }

    @Override
    public void addComponent(final Component component) {
        components.add(component);
    }

    @Override
    public void removeComponent(final String identifier) {
        components.removeIf(component -> component.getName().equals(identifier));
    }

    @Override
    public void removeComponent(final Component component) {
        components.remove(component);
    }

    @Override
    public List<Component> getComponents() {
        return components;
    }

    @Override
    public Component getComponent(final String identifier) {

        for (final Component component : components) {
            if (component.getName().equals(identifier)) {
                return component;
            }
        }

        return null;
    }

    private void prepareGraphics(final SaltyGraphics graphics) {
        graphics.setColor(getBackgroundColor());
        graphics.setFont(getFont());
    }

    /**
     * This method is called whenever this
     * <code>UIElement</code> is removed from a
     * {@link UISystem}
     */
    public void onRemove() {
    }

    public Font getFont() {
        return font;
    }

    public void setFont(final Font font) {
        this.font = font;
    }

    @Override
    public Coordinates getCoordinates() {
        return getPosition().toCoordinates();
    }

    public boolean isSuppressClipping() {
        return suppressClipping;
    }

    public void setSuppressClipping(final boolean suppressClipping) {
        this.suppressClipping = suppressClipping;
    }

    public boolean mouseHoversOver() {
        return mouseHoversOver;
    }

    protected void setMouseHoversOver(final boolean mouseHoversOver) {
        this.mouseHoversOver = mouseHoversOver;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(final Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    /**
     * Gets {@link #focused}.
     *
     * @return the value of {@link #focused}
     */
    protected boolean isFocused() {
        return focused;
    }

    /**
     * Sets {@link #focused}.
     *
     * @param focused the new value of {@link #focused}
     */
    protected void setFocused(final boolean focused) {
        this.focused = focused;
    }
}
