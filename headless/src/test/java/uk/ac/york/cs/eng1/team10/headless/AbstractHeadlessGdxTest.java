package uk.ac.york.cs.eng1.team10.headless;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeEach;
import uk.ac.york.eng1.team10.headless.HeadlessLauncher;

import static org.mockito.Mockito.mock;

public class AbstractHeadlessGdxTest {
    @BeforeEach
    public void setup() {
        Gdx.gl = Gdx.gl20 = mock(GL20.class);
        HeadlessLauncher.main(new String[0]);
    }
}
