package de.felix.game.rendering.worldRenderer.world;

import de.felix.game.rendering.camera.Camera;
import de.felix.game.rendering.worldRenderer.chunk.Chunk;
import org.joml.Vector3i;

import java.util.ArrayList;

public class World {
    private final ArrayList<ArrayList<ArrayList<Chunk>>> chunks;

    public World() {
        this.chunks = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        int dynamicSize = 3;
        for (int x = 0; x < dynamicSize; x++) {
            ArrayList<ArrayList<Chunk>> xChunks = new ArrayList<>();
            for (int y = 0; y < dynamicSize; y++) {
                ArrayList<Chunk> yChunks = new ArrayList<>();
                for (int z = 0; z < dynamicSize; z++) {
                    yChunks.add(new Chunk(new Vector3i(x, y, z)));
                }
                xChunks.add(yChunks);
            }
            chunks.add(xChunks);
        }
    }



    public Chunk getChunk(Vector3i position) {
        return chunks.get(position.x).get(position.y).get(position.z);
    }

    public Vector3i getChunkPositionFromPlayer(Camera camera) {
        return new Vector3i(
                (int) Math.floor(camera.getPosition().x / 16.0),
                (int) Math.floor(camera.getPosition().y / 16.0),
                (int) Math.floor(camera.getPosition().z / 16.0)
        );
    }

    public Chunk getOrCreateChunk(Vector3i position) {
        if (position.x < 0 || position.y < 0 || position.z < 0)
            return null;
        if (chunks.get(position.x).get(position.y).get(position.z) == null) {
            chunks.get(position.x).get(position.y).set(position.z, new Chunk(position));
        }
        return chunks.get(position.x).get(position.y).get(position.z);
    }

}
