package uk.ac.york.eng1.team10.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.team3._8.game.MazeGame;

public class HeadlessLauncher {
    public static void main(String[] args) {
        generateHeadlessApp();
    }

    private static Application generateHeadlessApp() {
        return new HeadlessApplication(new MazeGame(), getDefaultConfiguration());
    }

    private static HeadlessApplicationConfiguration getDefaultConfiguration() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        // -1 causes no rendering
        config.updatesPerSecond = -1;
        return config;
    }
}
