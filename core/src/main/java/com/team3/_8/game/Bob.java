package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * This is the class for the character (Bob), inheriting from Entity
 * @author Lenny
 */
public class Bob extends CollidableEntity {

    private String[] inventory = new String[5];// Creates an inventory of size 5, which will be filled with String IDs
    private int inventoryEnd = 0;

    public Bob(Sprite sprite, float speed){
        super(sprite, speed);
    }

    public Bob(Sprite sprite, float speed, float widthChange, float heightChange){
        super(sprite, speed, widthChange, heightChange);
    }

    /**
     * This method controls the movement of Bob, by moving him around the axis,
     * depending on the input
     * @param movement_halter the directions that bob cannot move, false allowing movement
     *      0-left, 1-top, 2-right, 3-bottom
     */
    @Override
    public void move(boolean[] movement_halter){
        float delta = Gdx.graphics.getDeltaTime();


        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
             (Gdx.input.isKeyPressed(Input.Keys.D)))
              && !movement_halter[0]) {
            this.sprite.translateX(this.speed * delta);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) ||
                  (Gdx.input.isKeyPressed(Input.Keys.A)))
                   && !movement_halter[2]) {
            this.sprite.translateX(-speed * delta);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) ||
             (Gdx.input.isKeyPressed(Input.Keys.W)))
              && !movement_halter[3]) {
            this.sprite.translateY(speed * delta);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) ||
                (Gdx.input.isKeyPressed(Input.Keys.S)))
                && !movement_halter[1]) {
            this.sprite.translateY(-speed * delta);
        }
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }

    public boolean addInventory(String inventory){
        if (this.inventoryEnd < 6){
            this.inventory[this.inventoryEnd] = inventory;
            this.inventoryEnd++;
            return true;
        }
        return false;
    }
}
