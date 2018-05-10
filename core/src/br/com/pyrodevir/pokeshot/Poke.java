package br.com.pyrodevir.pokeshot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

import static br.com.pyrodevir.pokeshot.Constants.pokeSize;

public class Poke {

    private Rectangle body;
    private Texture pokeTexture;
    private Texture bgTexture;
    private int textureNumber;
    private float resize = 1.1f;
    private int power;
    private List<String> type;
    private boolean pressed = false;

    public Poke(int posx, int posy, int textureNumber, int power, List<String> type){
        body = new Rectangle(posx, posy, pokeSize, pokeSize);
        pokeTexture = new Texture("pokes/"+textureNumber+".png");
        bgTexture = new Texture("pokebg.png");
        this.textureNumber = textureNumber;
        this.power = power;
        this.type = type;

    }

    public void draw(SpriteBatch batch){
        batch.draw(bgTexture, body.x, body.y, body.getWidth(), body.getHeight());
        batch.draw(pokeTexture, body.x, body.y, body.getWidth(), body.getHeight());
        if(pressed){
            batch.draw(bgTexture, (body.x - (body.getWidth()*(resize-1))/2),
                    (body.y - (body.getHeight()*(resize-1))/2), body.getWidth()*resize, body.getHeight()*resize);
            batch.draw(pokeTexture, (body.x - (body.getWidth()*(resize-1))/2),
                    (body.y - (body.getHeight()*(resize-1))/2), body.getWidth()*resize, body.getHeight()*resize);
        }else{
            batch.draw(bgTexture, body.x, body.y, body.getWidth(), body.getHeight());
            batch.draw(pokeTexture, body.x, body.y, body.getWidth(), body.getHeight());
        }
    }


    public boolean isPressed(int x, int y){
        if (body.x <= x && body.x + body.getWidth() >= x && body.y <= y && body.y + body.getHeight() >= y){
            pressed = true;
        } else {
            pressed = false;
        }
        return pressed;
    }

    public boolean getPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public int getPower() { return power; }

    public void setPower(int power) { this.power = power; }

    public List<String> getType() { return type; }

    public int getTextureNumber() { return textureNumber; }

    public void dispose(){
        pokeTexture.dispose();
        bgTexture.dispose();
    }

}


