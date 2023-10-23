package de.felix.game.rendering.camera;

import de.felix.game.GameData;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final Matrix4f view;
    private final Matrix4f projection;

    private Vector3f position;

    private Vector3f rotation;

    private float moveSpeed;
    private float mouseSensitivity = .1f;
    private float oldMouseX;
    private float oldMouseY;

    private float yaw, pitch;

    public Camera() {
        this.rotation = new Vector3f(0,0,0);
        position = new Vector3f(0, 0, -3);
        view = new Matrix4f();
        projection = new Matrix4f();

        view.rotate((float) Math.toRadians(0), new Vector3f(0, 1, 0));
        view.translate(new Vector3f(0, 0, -3));

        projection.frustum(-1, 1, -1, 1, 1, -1);
    }

    public void update(long window) {

        float speed = 0.25f;

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            move(speed * (float) Math.sin(Math.toRadians(yaw)), 0, -speed * (float) Math.cos(Math.toRadians(yaw)));
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            move(-speed * (float) Math.sin(Math.toRadians(yaw)), 0, speed * (float) Math.cos(Math.toRadians(yaw)));
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            move(-speed * (float) Math.cos(Math.toRadians(yaw - 90)), 0, -speed * (float) Math.sin(Math.toRadians(yaw - 90)));
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            move(speed * (float) Math.cos(Math.toRadians(yaw - 90)), 0, speed * (float) Math.sin(Math.toRadians(yaw - 90)));
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            move(0, speed, 0);
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            move(0, -speed, 0);
        }

        double[] xpos = new double[1];
        double[] ypos = new double[1];

        glfwGetCursorPos(window, xpos, ypos);
        float newMouseX = (float) xpos[0];
        float newMouseY = (float) ypos[0];

        float dx = (newMouseX - oldMouseX) * mouseSensitivity;
        float dy = (newMouseY - oldMouseY) * mouseSensitivity;

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;

        rotate(dx, dy);
    }


    public void move(float dx, float dy, float dz) {
        Vector3f direction = new Vector3f((float) Math.cos(Math.toRadians(yaw)), 0, (float) Math.sin(Math.toRadians(yaw)));
        Vector3f right = new Vector3f((float) Math.sin(Math.toRadians(yaw - 90)), 0, (float) Math.cos(Math.toRadians(yaw - 90)));
        Vector3f up = new Vector3f(0, 1, 0);

        position.add(new Vector3f(direction).mul(dx));
        position.add(new Vector3f(right).mul(dy));
        position.add(new Vector3f(up).mul(dz));
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



    public Matrix4f getView() {
        return view;
    }

    public Matrix4f getProjection() {
        return projection;
    }
}
