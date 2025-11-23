package uk.ac.york.cs.eng1.team10.headless;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.team3._8.game.Bob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BobTests extends AbstractHeadlessGdxTest {
    private Bob bob;
    private Sprite bobSprite;

    @BeforeEach
    public void createBob() {
        TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(100, 500);
        bobSprite.setSize(15, 15);
        bob = new Bob(bobSprite, 60, -3);
    }

    @Test
    public void testSpeed() {
        int speed = 100;
        bob.setSpeed(speed);
        assertEquals(speed, bob.getSpeed());
    }
}
