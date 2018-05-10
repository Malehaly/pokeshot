package br.com.pyrodevir.pokeshot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static br.com.pyrodevir.pokeshot.Constants.backgroundVel;
import static br.com.pyrodevir.pokeshot.Constants.screenx;
import static br.com.pyrodevir.pokeshot.Constants.screeny;

public class Background {

    private Texture texture;
    private float posx1;
    private float posx2;

    public Background(){
        texture = new Texture("background.png");

        posx1 = 0;
        posx2 = screenx;
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, posx1, 0, screenx, screeny);
        batch.draw(texture, posx2, 0, screenx, screeny);
    }

    public void update(float time){
        posx1 += time * backgroundVel;
        posx2 += time * backgroundVel;

        if(posx1 + screenx <= 0){
            posx1 = screenx;
            posx2 = 0;
        }
        if(posx2 + screenx <= 0){
            posx2 = screenx;
            posx1 = 0;
        }
    }

    public void dispose(){
        texture.dispose();
    }
}
