package de.felix;

import de.felix.game.Game;
import de.felix.game.GameData;
public class Main {
    private static final Game game = new Game(new GameData(800, 600, "Voxel Game Engine", false));
    public static void main(String[] args) {
        game.runGameLoop();
    }
}