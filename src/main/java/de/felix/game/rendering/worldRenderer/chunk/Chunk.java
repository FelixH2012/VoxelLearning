package de.felix.game.rendering.worldRenderer.chunk;

import org.joml.Vector3i;

public class Chunk {
    private final int[][][] blocks;
    private final Vector3i position;

    public Chunk(Vector3i position) {
        this.position = position;
        this.blocks = generateBlocks();
    }

    public int[][][] getBlocks() {
        return blocks;
    }

    private int[][][] generateBlocks() {
        int[][][] blocks = new int[16][16][16];
        int groundLevel = 10;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                blocks[x][groundLevel][z] = 1;
            }
        }


        return blocks;
    }



    public Vector3i getPosition() {
        return position;
    }
}
