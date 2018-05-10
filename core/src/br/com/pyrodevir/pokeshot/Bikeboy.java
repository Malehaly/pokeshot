package br.com.pyrodevir.pokeshot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static br.com.pyrodevir.pokeshot.Constants.backgroundVel;
import static br.com.pyrodevir.pokeshot.Constants.screenx;
import static br.com.pyrodevir.pokeshot.Constants.screeny;

public class Bikeboy {

    private Rectangle body;
    private Rectangle body2;
    private Texture[] frames;
    private Texture[] floatFrames;
    private float framesCount;
    private boolean isGoingUp;

    public Bikeboy (){
        body = new Rectangle(0.1f*screenx, 0.1f*screeny, 0.2f*screenx, 0.2f*screenx);
        frames = new Texture[2];
        frames[0] = new Texture("bikeboy1.png");
        frames[1] = new Texture("bikeboy2.png");
        isGoingUp = false;

        body2 = new Rectangle(0.8f*screenx, 0.4f*screeny, 0.15f*screenx, 0.15f*screenx);
        floatFrames = new Texture[2];
        floatFrames[0] = new Texture("mew1.png");
        floatFrames[1] = new Texture("mew2.png");
    }

    public void draw (SpriteBatch batch){
        batch.draw(frames[(int) framesCount % 2],
                body.x - (frames[0].getWidth()/12), body.y - (frames[0].getWidth()/12),
                body.getWidth(), body.getHeight(), 0, 0,
                frames[(int) framesCount % 2].getWidth(), frames[(int) framesCount % 2].getHeight(), false, false);
        batch.draw(floatFrames[(int) framesCount % 2],
                body2.x - (floatFrames[0].getWidth()/12), body2.y - (floatFrames[0].getWidth()/12),
                body2.getWidth(), body2.getHeight(), 0, 0,
                floatFrames[(int) framesCount % 2].getWidth(), floatFrames[(int) framesCount % 2].getHeight(), false, false);
    }

    public void update(float time){
        framesCount += 2*time;
        if (body.y < screeny/40){
            body.y = screeny/40;
            isGoingUp = true;
        } else if( body.y + body.getHeight() > (screeny/4)) {
            isGoingUp = false;
            body.y = (screeny/4) - body.getHeight();
        }

        if (isGoingUp){
            body.y += -backgroundVel*time*1.5f;
        } else {
            body.y += backgroundVel*time*1.5f;
        }
    }

    public void dispose(){
        for(int i=0; i <=1; i++){
            frames[i].dispose();
            floatFrames[i].dispose();
        }

    }
}
