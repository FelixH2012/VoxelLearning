package de.felix.game.rendering;

import de.felix.game.rendering.camera.Camera;
import de.felix.game.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Rectangle {

    private final int[] indices;
    private final int vaoID;
    private final ShaderProgram shaderProgram;
    private final Matrix4f model;
    private final Camera camera;

    public Rectangle(int[] indices, ShaderProgram shaderProgram, Camera camera) {
        this.indices = indices;
        this.shaderProgram = shaderProgram;
        this.model = new Matrix4f();
        this.camera = camera;

        this.vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        float[] positions = new float[] {
                // VO
                -0.5f,  0.5f,  0.5f,
                // V1
                -0.5f, -0.5f,  0.5f,
                // V2
                0.5f, -0.5f,  0.5f,
                // V3
                0.5f,  0.5f,  0.5f,
                // V4
                -0.5f,  0.5f, -0.5f,
                // V5
                0.5f,  0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };


        final int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);

        final int iboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render(int x, int y, int z) {
        shaderProgram.startProgram();
        glBindVertexArray(vaoID);

        model.identity();
        model.translate(x, y, z);
        model.rotate((float) Math.toRadians(0), new Vector3f(0, 1, 0));

        int modelLocation = GL20.glGetUniformLocation(shaderProgram.getProgramId(), "model");
        GL20.glUniformMatrix4fv(modelLocation, false, model.get(new float[16]));

        int viewLocation = GL20.glGetUniformLocation(shaderProgram.getProgramId(), "view");
        GL20.glUniformMatrix4fv(viewLocation, false, camera.getView().get(new float[16]));

        int projectionLocation = GL20.glGetUniformLocation(shaderProgram.getProgramId(), "projection");
        GL20.glUniformMatrix4fv(projectionLocation, false, camera.getProjection().get(new float[16]));

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        shaderProgram.stopProgram();
    }
}