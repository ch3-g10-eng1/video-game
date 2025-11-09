package com.team3._8.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Map;
import java.util.Set;

/**
 * This class creates the HUD of the game.
 *
 * <p>The idea is that you pass textures that will be rendered through to the class, then you can
 * have all the decision-making in here.
 *
 * <p>This avoids lots of passing textures through.
 *
 * @author Lenny, Henry
 */
public class HUD {

  private final Batch HUDbatch;
  private final Texture keycard;
  private final Texture pause;

  public HUD(Batch HUDbatch, int events) {
    this.HUDbatch = HUDbatch;
    this.keycard = new Texture("keycard.png");
    this.pause = new Texture("libgdx.png");
  }

  /**
   * Draws the HUD data, such as time left & items collected
   *
   * @param font BitmapFont: The font used to render the timer
   * @param timer String: The formatted timer string
   * @param events Map: Contains data to display the event status
   * @param bob Bob: The Bob character, to extract the contents of his inventory from
   * @param isPaused boolean: Whether to draw paused HUD
   * @param viewport Viewport: Used to get the windows size for arranging text
   */
  public void draw(
      BitmapFont font,
      String timer,
      Map<String, Integer> events,
      Bob bob,
      boolean isPaused,
      Viewport viewport) {

    // Gets screen size to arrange text
    float windowX = viewport.getScreenX();
    float windowY = viewport.getScreenY();
    float windowWidth = viewport.getScreenWidth();
    float windowHeight = viewport.getScreenHeight();

    // Backs up old HUDbatch settings to restore later
    Matrix4 previous = this.HUDbatch.getProjectionMatrix().cpy();

    // Changes HUDbatch to use screen co-ordinates not game one
    Matrix4 ortho = new Matrix4().setToOrtho2D(0, 0, windowWidth, windowHeight);
    this.HUDbatch.setProjectionMatrix(ortho);

    GlyphLayout layout = new GlyphLayout();
    String[] HUDText = {
      timer,
      "Positive: " + events.get("Positive"),
      "Negative: " + events.get("Negative"),
      "Hidden: " + events.get("Hidden")
    };
    float y = windowHeight - 10f - font.getCapHeight();
    // Sets text scale based on window width
    // 0.0015625 is just a scaling factor that equates to 1 at initial screen render size (640)
    font.getData().setScale((0.0015625f) * windowWidth);

    this.HUDbatch.begin();

    // Draws each line of text
    for (String text : HUDText) {
      layout.setText(font, text);
      float x = windowWidth - 10f - layout.width;

      font.draw(this.HUDbatch, text, windowX + x, windowY + y);
      y -= font.getLineHeight();
    }

    // Restores settings & draws textures
    this.HUDbatch.setProjectionMatrix(previous);
    font.getData().setScale(1);
    this.drawTextures(bob, font, isPaused);

    this.HUDbatch.end();
  }

  /**
   * This method will draw the items collected on the screen
   *
   * @param bob Bob: The bob object so we can have a look at his inventory
   */
  private void drawTextures(Bob bob, BitmapFont font, boolean isPaused) {
    Set<String> bobInventory;
    bobInventory = bob.getInventory();

    for (String item : bobInventory) {
      switch (item) { // Switch for extendability
        case "Keycard":
          this.HUDbatch.draw(this.keycard, 10, 325, 50, 50);
          if (isPaused) {
            font.draw(this.HUDbatch, "This keycard can be used to unlock something...", 10, 325);
          }
          break;
        default:
      }
    }
  }

  /**
   * Draws pause screen onto the current active window
   *
   * @param font the font of any text on the pause screen to be used
   * @param viewport the current viewport of the game
   */
  public void pauseScreen(BitmapFont font, Viewport viewport) {
    this.HUDbatch.begin();
    font.draw(this.HUDbatch, "PAUSED", 340, 375);
    this.HUDbatch.draw(
        this.pause, (float) viewport.getScreenX() / 2, (float) viewport.getScreenY() / 2, 100, 100);
    this.HUDbatch.end();
  }
}
