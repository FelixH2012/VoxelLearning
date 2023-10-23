package de.felix.game.rendering.camera;

import de.felix.game.input.KeyboardInput;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final Matrix4f view;
    private final Matrix4f projection;

    private final Vector3f position;

    private float oldMouseX;
    private float oldMouseY;

    private float yaw, pitch;

    public Camera() {
        position = new Vector3f(0, 0, -3);
        view = new Matrix4f();
        projection = new Matrix4f();

        view.rotate((float) Math.toRadians(0), new Vector3f(0, 1, 0));
        view.translate(new Vector3f(0, 0, -3));

        projection.frustum(-1, 1, -1, 1, 1, -1);
    }

    public void update(final KeyboardInput keyboardInput, long window, float deltaTime) {

        float speed = 4f;

        if (pitch >= 80)
            speed = 10;
        else if (pitch <= -80)
            speed = 10;

        move(keyboardInput, deltaTime * (speed) * 10);

        final double[] mouseX = new double[1];
        final double[] mouseY = new double[1];

        glfwGetCursorPos(window, mouseX, mouseY);
        final float newMouseX = (float) mouseX[0];
        final float newMouseY = (float) mouseY[0];

        final float mouseSensitivity = .1f;
        final float dx = (newMouseX - oldMouseX) * mouseSensitivity;
        final float dy = (newMouseY - oldMouseY) * mouseSensitivity;

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;

        rotate(dx, dy);
    }


    public Vector3f getForwardDirection() {
        Vector3f forward = new Vector3f();
        forward.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        forward.y = (float) Math.sin(Math.toRadians(pitch));
        forward.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        return forward;
    }

    public Vector3f getRightDirection() {
        Vector3f right = new Vector3f();
        right.x = (float) -Math.sin(Math.toRadians(yaw));
        right.y = 0;
        right.z = (float) -Math.cos(Math.toRadians(yaw));
        return right;
    }


    public void move(KeyboardInput keyboardInput, float deltaTime) {
        float speed = 0.1f;

        if (keyboardInput.isKeyPressed(GLFW.GLFW_KEY_W)) {
            position.add(getForwardDirection().mul(speed * deltaTime));
        }
        if (keyboardInput.isKeyPressed(GLFW.GLFW_KEY_S)) {
            position.sub(getForwardDirection().mul(speed * deltaTime));
        }
        if (keyboardInput.isKeyPressed(GLFW.GLFW_KEY_A)) {
            position.sub(getRightDirection().mul(speed * deltaTime));
        }
        if (keyboardInput.isKeyPressed(GLFW.GLFW_KEY_D)) {
            position.add(getRightDirection().mul(speed * deltaTime));
        }
    }


    public void updateProjection(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        projection.identity();
        projection.perspective(45.0f, aspectRatio, 0.1f, 100.0f);
    }

    public void rotate(float dx, float dy) {
        yaw += dx;
        pitch -= dy;

        if (pitch > 90f) {
            pitch = 90f;
        } else if (pitch < -90f) {
            pitch = -90f;
        }

        float directionX = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        float directionY = (float) Math.sin(Math.toRadians(pitch));
        float directionZ = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));

        view.identity().lookAt(new Vector3f(position.x, position.y, position.z),
                new Vector3f(position.x + directionX, position.y + directionY, position.z + directionZ),
                new Vector3f(0, 1, 0));
    }


    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getView() {
        return view;
    }

    public Matrix4f getProjection() {
        return projection;
    }
}
