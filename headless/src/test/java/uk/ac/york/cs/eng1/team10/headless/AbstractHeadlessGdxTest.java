package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import uk.ac.york.eng1.team10.headless.HeadlessLauncher;

public class AbstractHeadlessGdxTest {
    @BeforeEach
    public void setup() {
        Gdx.gl = Gdx.gl20 = mock(GL20.class);
        HeadlessLauncher.main(new String[0]);
    }

  @AfterEach
  public void tearDown() {
    Gdx.gl = null;
    Gdx.gl20 = null;
    Gdx.app = null;
    Gdx.graphics = null;
    Gdx.input = null;
  }
}
