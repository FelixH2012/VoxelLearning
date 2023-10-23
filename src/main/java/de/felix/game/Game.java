package de.felix.game;

import de.felix.game.input.KeyboardInput;
import de.felix.game.rendering.camera.Camera;
import de.felix.game.rendering.worldRenderer.WordRenderer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    private final GameData gameData;
    private long window;

    private final WordRenderer wordRenderer;

    public Game(final GameData gameData) {
        this.gameData = gameData;
        createWindowAndInitializeLWJGL();

        this.wordRenderer = new WordRenderer();

    }

    private void createWindowAndInitializeLWJGL() {
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        window = glfwCreateWindow(gameData.width(), gameData.height(), gameData.title(), 0, 0);

        if (window == 0)
            throw new IllegalStateException("Failed to create window!");

        final GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (videoMode == null) {
            glfwTerminate();
            System.out.println("Failed to get video mode!");
            return;
        }
        glfwSetWindowPos(window, (videoMode.width() - gameData.width()) / 2, (videoMode.height() - gameData.height()) / 2);

        glfwShowWindow(window);
    }

    double lastFrameTime = glfwGetTime();

    public void runGameLoop() {
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        final Camera camera = new Camera();

        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            camera.updateProjection(width, height);
            glViewport(0, 0, width, height);
        });


        final KeyboardInput keyboardInput = new KeyboardInput();
        glfwSetKeyCallback(window, (window1, key, scancode, action, mods) -> keyboardInput.keyCallback(key, action));

        while (!glfwWindowShouldClose(window)) {

            if (keyboardInput.isKeyPressed(GLFW_KEY_ESCAPE))
                glfwSetWindowShouldClose(window, true);
            if (keyboardInput.isKeyPressed(GLFW_KEY_F11)) {
                glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, 1920, 1080, 144);
            }
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

            final double currentFrameTime = glfwGetTime();
            final float deltaTime = (float) (currentFrameTime - lastFrameTime);
            lastFrameTime = currentFrameTime;

            camera.update(keyboardInput, window, deltaTime);

            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            wordRenderer.render(camera);

            glfwSwapBuffers(window);
        }
        glfwTerminate();
    }
}
