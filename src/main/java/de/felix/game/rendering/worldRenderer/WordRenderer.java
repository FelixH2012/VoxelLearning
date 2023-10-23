package de.felix.game.rendering.worldRenderer;

import de.felix.game.noise.FastNoiseLite;
import de.felix.game.rendering.Rectangle;
import de.felix.game.rendering.camera.Camera;
import de.felix.game.shader.ShaderProgram;
import org.joml.Vector3f;

public class WordRenderer {

    private ShaderProgram shaderProgram;

    public WordRenderer() {

    }

    private final int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
            4, 0, 3, 5, 4, 3,
            3, 2, 7, 5, 3, 7,
            6, 1, 0, 6, 0, 4,
            2, 1, 6, 2, 6, 7,
            7, 6, 4, 7, 4, 5,
    };

    public void render(final int numberOfCubes, final Camera camera) {
        if (shaderProgram == null)
            shaderProgram = new ShaderProgram("resources/vertexShader.glsl", "resources/fragmentShader.glsl");


        final FastNoiseLite noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noise.SetFractalGain(2.5f);
        noise.SetFractalWeightedStrength(0.5f);

        final double[] noiseForEachCube = new double[numberOfCubes];

        for (int i = 0; i < numberOfCubes; i++) {
            noiseForEachCube[i] = noise.GetNoise(i, i, i);
        }

        Vector3f[] cubePositions = new Vector3f[numberOfCubes];
        for (int i = 0; i < numberOfCubes; i++) {
            float x = (float) i;
            float y = (float) (-5 + noiseForEachCube[i]);
            float z = (float) i;

            cubePositions[i] = new Vector3f(x, y, z);
        }

        final Rectangle[] rectangles = new Rectangle[numberOfCubes];
        for (int i = 0; i < numberOfCubes; i++) {
            rectangles[i] = new Rectangle(indices, shaderProgram, camera);
        }
        for (int lines = 0; lines < numberOfCubes; lines++) {
            for (int i = 0; i < numberOfCubes; i++) {
                rectangles[i].render((int) cubePositions[i].x, (int) cubePositions[i].y, lines);
            }
        }
    }
}