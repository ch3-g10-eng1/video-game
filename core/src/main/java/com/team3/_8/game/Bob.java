package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * This is the class for the character (Bob), inheriting from Entity
 * @author Lenny
 */
public class Bob extends Entity
{
    public Bob(Sprite sprite, float speed)
    {
        super(sprite, speed);
    }

    /**
     * This method controls the movement of Bob, by moving him around the axis, depending on the input
     */
    @Override
    public void move(){
        float delta = Gdx.graphics.getDeltaTime();

        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || (Gdx.input.isKeyPressed(Input.Keys.D)))) {
            this.sprite.translateX(this.speed * delta);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.A)))) {
            this.sprite.translateX(-speed * delta);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) || (Gdx.input.isKeyPressed(Input.Keys.W)))) {
            this.sprite.translateY(speed * delta);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || (Gdx.input.isKeyPressed(Input.Keys.S)))) {
            this.sprite.translateY(-speed * delta);
        }
    }

    @Override
    protected void createBox(){
        this.collisionBox = new Rectangle(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth() - 3, this.sprite.getHeight() - 3);
    }
}
