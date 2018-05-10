package br.com.pyrodevir.pokeshot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static br.com.pyrodevir.pokeshot.Constants.screenx;
import static br.com.pyrodevir.pokeshot.Constants.screeny;

public class Button {

    private Rectangle body;
    private boolean pressed = false;
    private float resize = 1.1f;
    private Texture texture;

    public Button(Texture texture, int posy){
        this.texture = texture;
        body = new Rectangle(screenx/4, posy, screenx/2, screeny/15);
    }

    public void draw(SpriteBatch batch){
        if(pressed){
            batch.draw(texture, (body.x - (body.getWidth()*(resize-1))/2),
                    (body.y - (body.getHeight()*(resize-1))/2), body.getWidth()*resize, body.getHeight()*resize);
        }else{
            batch.draw(texture, body.x, body.y, body.getWidth(), body.getHeight());
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

    public void dispose(){
        texture.dispose();
    }

}
