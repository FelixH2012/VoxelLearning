package de.felix.game.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class ShaderProgram {

    int programId;

    public ShaderProgram(String vsFile, String fsFile) {

        programId = GL30.glCreateProgram();
        if (programId == 0) {
            int error = GL11.glGetError();
            System.err.println("ERROR: glCreateProgram failed. Error code: " + error);
            System.exit(-1);
        }
        GL30.glAttachShader(programId, buildShader(vsFile, GL30.GL_VERTEX_SHADER));
        System.out.println("Vertex shader compiled successfully");
        GL30.glAttachShader(programId, buildShader(fsFile, GL30.GL_FRAGMENT_SHADER));
        System.out.println("Fragment shader compiled successfully");
        GL30.glLinkProgram(programId);
        GL30.glValidateProgram(programId);

        if (GL30.glGetProgrami(programId, GL30.GL_LINK_STATUS) == 0) {
            System.err.println("ERROR: linking program. Info: " + GL30.glGetProgramInfoLog(programId));
            System.exit(-1);
        }
        if (GL30.glGetProgrami(programId, GL30.GL_VALIDATE_STATUS) == 0) {
            System.err.println("ERROR: validating program. Info:" + GL30.glGetProgramInfoLog(programId));
            System.exit(-1);
        }
    }

    void bindAttributeLocation(int location, String attributeName) {
        GL30.glBindAttribLocation(programId, location, attributeName);
    }

    public void startProgram() {
        GL30.glUseProgram(programId);
    }

    public void stopProgram() {
        GL30.glUseProgram(0);
    }

    private int buildShader(String shaderFile, int shaderType) {
        final int shader = GL30.glCreateShader(shaderType);
        final String shaderSource = getShaderSource(shaderFile);
        GL30.glShaderSource(shader, shaderSource);
        GL30.glCompileShader(shader);

        if (GL30.glGetShaderi(shader, GL30.GL_COMPILE_STATUS) == 0) {
            System.err.println("ERROR: compiling shader. Info: " + GL30.glGetShaderInfoLog(shader));
            System.exit(-1);
        }

        return shader;
    }

    private String getShaderSource(String shaderFile) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        System.out.println("Reading file: " + new File(shaderFile).getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(shaderFile))) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return stringBuilder.toString();
    }

    public int getProgramId() {
        return programId;
    }
}