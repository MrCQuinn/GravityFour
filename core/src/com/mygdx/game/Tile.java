package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Charlie on 12/20/16.
 */
public class Tile {
    private Sprite sprite;
    private Coord current;
    private Sprite mark;
    private int value;
    private boolean deleteThisRound;

    public Tile(Texture texture){
        sprite = new Sprite(texture);
        deleteThisRound = false;
    }

    public void setPosition(Coord c){
        current = c;
        sprite.setPosition(c.getX(),c.getY());
    }

    public Coord getPosition(){
        return current;
    }

    public void setSprite(Texture texture, Coord position){
        sprite.set(new Sprite(texture));
        sprite.setPosition(position.getX(), position.getY());
    }

    public Sprite getSprite(){
        return sprite;
    }

    public int getValue(){
        return value;
    }
    public void setValue(int v){
        value = v;
        if(value == 1){
            mark = new Sprite(new Texture("X.png"));
            //mark.scale(.1f);
            mark.setPosition(current.getX(),current.getY());
        }else if(value == 2){
            mark = new Sprite(new Texture("O.png"));
            //mark.scale(.1f);
            mark.setPosition(current.getX(),current.getY());
        }
    }

    public Sprite getMark(){
        return mark;
    }


}
