package de.felix.game.input;

import org.lwjgl.glfw.GLFW;

public class KeyboardInput {
    private final boolean[] keyPressed = new boolean[GLFW.GLFW_KEY_LAST];

    public void keyCallback(int key, int action) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            if (action == GLFW.GLFW_PRESS) {
                keyPressed[key] = true;
            } else if (action == GLFW.GLFW_RELEASE) {
                keyPressed[key] = false;
            }
        }
    }

    public boolean isKeyPressed(int key) {
        return keyPressed[key];
    }
}
