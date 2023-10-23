package de.felix.game.rendering.worldRenderer;

import de.felix.game.noise.FastNoiseLite;
import de.felix.game.rendering.Cube;
import de.felix.game.rendering.camera.Camera;
import de.felix.game.rendering.worldRenderer.chunk.Chunk;
import de.felix.game.rendering.worldRenderer.world.World;
import de.felix.game.shader.ShaderProgram;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class WordRenderer {

    private final World world;

    public WordRenderer() {
        this.world = new World();
    }

    private final int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
            4, 0, 3, 5, 4, 3,
            3, 2, 7, 5, 3, 7,
            6, 1, 0, 6, 0, 4,
            2, 1, 6, 2, 6, 7,
            7, 6, 4, 7, 4, 5,
    };

    private ShaderProgram shaderProgram;

    public void render(final Camera camera) {

        if (shaderProgram == null)
            shaderProgram = new ShaderProgram("resources/vertexShader.glsl", "resources/fragmentShader.glsl");
        final FastNoiseLite noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noise.SetFractalGain(.2f);
        noise.SetFractalWeightedStrength(0.5f);

        Vector3i cameraChunkPosition = new Vector3i((int) Math.floor(camera.getPosition().x / 16.0),(int) Math.floor(camera.getPosition().y / 16.0), (int) Math.floor(camera.getPosition().z / 16.0));


        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Vector3i chunkPosition = new Vector3i(
                            cameraChunkPosition.x + x,
                            cameraChunkPosition.y + y / 16,
                            cameraChunkPosition.z + z
                    );

                    Chunk chunk = world.getOrCreateChunk(chunkPosition);
                    if (chunk == null)
                        continue;

                    renderChunk(chunk, camera);
                }
            }
        }
    }

    private void renderChunk(Chunk chunk, final Camera camera) {
        int[][][] blocks = chunk.getBlocks();
        Vector3i chunkPosition = chunk.getPosition();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    int block = blocks[x][y][z];
                    if (block != 0) {
                        Vector3f blockPosition = new Vector3f(chunkPosition.x * 16 + x, chunkPosition.y * 16 + y, chunkPosition.z * 16 + z);
                        Cube cube = new Cube(indices, shaderProgram, camera);
                        cube.render((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z);

                    }
                }
            }
        }
    }
}