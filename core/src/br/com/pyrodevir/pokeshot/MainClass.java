package br.com.pyrodevir.pokeshot;

import com.appodeal.gdx.GdxAppodeal;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static br.com.pyrodevir.pokeshot.Constants.clockNormal;
import static br.com.pyrodevir.pokeshot.Constants.pokeSize;
import static br.com.pyrodevir.pokeshot.Constants.screenx;
import static br.com.pyrodevir.pokeshot.Constants.screeny;

public class MainClass extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture clockIcon;
    private Texture versusIcon;
    private Texture pokeshot;
    private Background bg;
    private Bikeboy bikeboy;
    private Poke poke1;
    private Poke poke2;
    private boolean newPoke = false;
    private float clock;
    private int score = 0;
    private List<Integer> statPower = new ArrayList<Integer>();
    private Button startButton;
    private Button restartButton;
    private Button rankingButton;
    private Button ranking2Button;
    private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();
    private Music backSound;
    private int mode = 0; //0-start 1-normal 2-lose
    private int adsCounter = 1;
    private PlayServices playServices;

    public MainClass(PlayServices playServices){
        this.playServices = playServices;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        bg = new Background();
        bikeboy = new Bikeboy();
        clock = 1f;
        clockIcon = new Texture("clock.png");
        versusIcon = new Texture("versus.png");
        pokeshot = new Texture("pokeshot.png");

        FreeTypeFontGenerator.setMaxTextureSize(2048);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (0.081f * screenx);
        parameter.color = new Color(Color.FIREBRICK);
        font = generator.generateFont(parameter);
        generator.dispose();

        backSound = Gdx.audio.newMusic(Gdx.files.internal("poketheme.mp3"));
        backSound.setLooping(true);
        backSound.setVolume(0.5f);
        startButton = new Button(new Texture("starticon.png"), (int) ((screeny / 2) + (0.2f * screeny)));
        rankingButton = new Button(new Texture("ranking.png"), (int) ((screeny / 2) + (0.1f * screeny)));
        restartButton = new Button(new Texture("starticon.png"), (int) ((screeny / 2 - 0.25f * screeny)));
        ranking2Button= new Button(new Texture("ranking.png"), (int) ((screeny / 2 - 0.35f * screeny)));
    }

    @Override
    public void render() {
        input();
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        draw();
        batch.end();
    }

    public void draw() {
        bg.draw(batch);
        bikeboy.draw(batch);
        batch.draw(pokeshot, 0.2f*screenx, 0.85f*screeny, 0.6f*screenx, 0.1f*screeny);
        if (mode == 0) {
            startButton.draw(batch);
            rankingButton.draw(batch);
            batch.draw(pokeshot, 0.2f*screenx, 0.85f*screeny, 0.6f*screenx, 0.1f*screeny);
        } else if (mode == 2) {
            restartButton.draw(batch);
            ranking2Button.draw(batch);
            batch.draw(pokeshot, 0.2f*screenx, 0.85f*screeny, 0.6f*screenx, 0.1f*screeny);
        }
        if (mode == 1 || mode == 2) {
            GdxAppodeal.show(GdxAppodeal.BANNER_BOTTOM);
            if (poke1 != null && poke2 != null) {
                poke1.draw(batch);
                poke2.draw(batch);
                batch.draw(clockIcon, screenx / 2 - 0.05f * screenx, screeny / 2 + pokeSize - 0.075f * screenx, 0.1f * screenx, 0.1f * screenx);
                batch.draw(versusIcon, screenx / 2 - 0.05f * screenx, screeny / 2 + pokeSize / 3, 0.1f * screenx, 0.1f * screenx);
                font.draw(batch, String.valueOf((int) clock), (screenx - layoutWidth(font, String.valueOf((int) clock))) / 2,
                        (screeny / 2 + pokeSize));
            }
            font.draw(batch, "score: " + String.valueOf(score), (screenx - layoutWidth(font, "score: " + String.valueOf(score))) / 2,
                    (0.98f * screeny));
            if (mode == 2) {
                font.draw(batch, pokeName(poke1.getTextureNumber()), (screenx * 0.05f),
                        (screeny / 2 + pokeSize + 0.05f * screeny));
                font.draw(batch, pokeName(poke2.getTextureNumber()), (screenx / 2 + 0.05f * screenx),
                        (screeny / 2 + pokeSize + 0.05f * screeny));
                font.draw(batch, "power: " + String.valueOf(statPower.get(0)), (screenx * 0.05f),
                        (screeny / 2 - 0.01f * screeny));
                font.draw(batch, "power: " + String.valueOf(statPower.get(1)), (screenx / 2 + 0.05f * screenx),
                        (screeny / 2 - 0.01f * screeny));
                font.draw(batch, "bonus: " + String.valueOf(poke1.getPower() - statPower.get(0)), (screenx * 0.05f),
                        (screeny / 2 - 0.06f * screeny));
                font.draw(batch, "bonus: " + String.valueOf(poke2.getPower() - statPower.get(1)), (screenx / 2 + 0.05f * screenx),
                        (screeny / 2 - 0.06f * screeny));
                font.draw(batch, "total: " + String.valueOf(poke1.getPower()), (screenx * 0.05f),
                        (screeny / 2 - 0.12f * screeny));
                font.draw(batch, "total: " + String.valueOf(poke2.getPower()), (screenx / 2 + 0.05f * screenx),
                        (screeny / 2 - 0.12f * screeny));
            }
        }
    }

    public void update(float time) {
        backSound.play();
        bg.update(time);
        bikeboy.update(time);
        if (mode == 1) {
            clock -= time;
            if (newPoke) {
                newPoke = false;
                Random random = new Random();
                int pokeNumber = random.nextInt(151) + 1;
                int pokeNumber2 = random.nextInt(151) + 1;
                if (pokeNumber == pokeNumber2 && pokeNumber >= 76) {
                    pokeNumber2 = random.nextInt(75) + 1;
                } else if (pokeNumber == pokeNumber2 && pokeNumber <= 75) {
                    //nextint() of 76 is from 0 to 75, which more 76 can reach 76 to 151;
                    pokeNumber2 = random.nextInt(76) + 76;
                }
                if (pokeNumber != pokeNumber2) {
                    statPower.clear();
                    poke1 = new Poke(screenx / 2 - (int) (pokeSize) - (int) (0.05f * screenx), screeny / 2, pokeNumber,
                            powerTable(pokeNumber), typeTable(pokeNumber));
                    poke2 = new Poke(screenx / 2 + (int) (0.05f * screenx), screeny / 2, pokeNumber2,
                            powerTable(pokeNumber2), typeTable(pokeNumber2));
                    battle(poke1.getType(), poke2.getType());
                    if (poke1.getPower() == poke2.getPower()) {
                        newPoke = true;
                        //previous method - pokenumber would undraw a battle. not fair.
                        /*if (pokeNumber > pokeNumber2) {
                            poke1.setPower(poke1.getPower() + 1);
                        } else {
                            poke2.setPower(poke2.getPower() + 1);
                        }*/
                    }
                    clock = clockNormal;
                }
            }
            if (clock <= 0) {
                mode = 2;
            }
        }

    }

    public void input() {
        if(Gdx.input.justTouched()){
            int x = Gdx.input.getX();
            int y = screeny - Gdx.input.getY();
            if (mode == 0) {
                startButton.isPressed(x, y);
                rankingButton.isPressed(x, y);
            } else if (mode == 1) {
                poke1.isPressed(x, y);
                poke2.isPressed(x, y);
            } else if (mode == 2) {
                restartButton.isPressed(x, y);
                ranking2Button.isPressed(x, y);
            }
        } else if (!Gdx.input.isTouched() && poke1 != null && poke1.getPressed()) {
            if (poke1.getPower() > poke2.getPower()) {
                score++;
                newPoke = true;
            } else {
                lose();
            }
            poke1.setPressed(false);
        } else if (!Gdx.input.isTouched() && poke2 != null && poke2.getPressed()) {
            if (poke2.getPower() > poke1.getPower()) {
                score++;
                newPoke = true;
            } else {
                lose();
            }
            poke2.setPressed(false);
        } else if (!Gdx.input.isTouched() && (startButton.getPressed() || restartButton.getPressed())) {
            mode = 1;
            score = 0;
            newPoke = true;
            startButton.setPressed(false);
            restartButton.setPressed(false);
        } else if (!Gdx.input.isTouched() && (rankingButton.getPressed() || ranking2Button.getPressed())) {
            playServices.showScore();
            rankingButton.setPressed(false);
            ranking2Button.setPressed(false);
        }
    }

    public void lose(){
        if(adsCounter >=5){
            GdxAppodeal.show(GdxAppodeal.INTERSTITIAL);
            adsCounter = 0;
        }
        adsCounter++;
        mode = 2;
        playServices.submitScore(score);
    }

    @Override
    public void pause() {
        if (mode==1){
            lose();
        }
        super.pause();
    }

    public int powerTable(int pokeNumber) {
        int pokePower = 0;
        List<Integer> power1 = Arrays.asList(1, 4, 7, 10, 13, 16, 29, 32, 43, 60, 63, 66, 69, 74, 92);
        List<Integer> power2 = Arrays.asList(19, 21, 23, 25, 27, 35, 37, 39, 41, 46, 48, 50, 52, 54, 56, 58,
                72, 77, 79, 81, 84, 86, 88, 90, 96, 98, 100, 102, 104, 109, 111, 116, 118, 120, 129, 133, 138, 140, 147);
        List<Integer> power3 = Arrays.asList(2, 5, 8, 11, 14, 17, 30, 33, 44, 61, 64, 67, 70, 75, 93);
        List<Integer> power4 = Arrays.asList(20, 22, 24, 26, 28, 36, 38, 40, 42, 47, 49, 51, 53, 55, 57, 59, 73, 78, 80,
                82, 83, 85, 87, 89, 91, 95, 97, 99, 101, 103, 105, 106, 107, 108, 110, 112, 113, 114, 115, 117, 119, 121, 122,
                123, 124, 125, 126, 127, 128, 130, 131, 132, 134, 135, 136, 137, 139, 141, 142, 143, 148);
        List<Integer> power5 = Arrays.asList(3, 6, 9, 12, 15, 18, 31, 34, 45, 62, 65, 68, 71, 76, 94, 144, 145, 146, 149, 150, 151);
        if (power1.contains(pokeNumber)) {
            pokePower += 1;
        } else if (power2.contains(pokeNumber)) {
            pokePower += 2;
        } else if (power3.contains(pokeNumber)) {
            pokePower += 3;
        } else if (power4.contains(pokeNumber)) {
            pokePower += 4;
        } else if (power5.contains(pokeNumber)) {
            pokePower += 5;
        }
        statPower.add(pokePower);
        return pokePower;
    }

    public List<String> typeTable(int pokeNumber) {
        List<String> type = new ArrayList<String>();

        List<Integer> typeNormal = Arrays.asList(19, 20, 52, 53, 108, 113, 115, 128, 132, 133, 137, 143, 16, 17, 18, 21, 22, 83, 84, 85, 39, 40);
        List<Integer> typeGrass = Arrays.asList(1, 2, 3, 43, 44, 45, 46, 47, 69, 70, 71, 102, 103, 114);
        List<Integer> typeFire = Arrays.asList(4, 5, 37, 38, 58, 59, 77, 78, 126, 136, 6);
        List<Integer> typeWater = Arrays.asList(7, 8, 9, 54, 55, 60, 61, 86, 90, 98, 99, 116, 117, 118, 119, 120, 129, 134, 130, 79, 80, 121, 62, 72,
                73, 138, 139, 140, 141, 87, 91, 131);
        List<Integer> typeBug = Arrays.asList(10, 11, 13, 14, 15, 46, 47, 48, 49, 127, 12, 123);
        List<Integer> typePoison = Arrays.asList(1, 2, 3, 13, 14, 15, 23, 24, 29, 30, 32, 33, 43, 44, 45, 48, 49, 69, 70, 71, 88, 89, 109, 110, 41, 42,
                72, 73, 31, 34, 92, 93, 94);
        List<Integer> typeFly = Arrays.asList(149, 16, 17, 18, 21, 22, 83, 84, 85, 41, 42, 130, 12, 123, 6, 142);
        List<Integer> typeElectric = Arrays.asList(25, 26, 100, 101, 125, 135, 81, 82);
        List<Integer> typeGround = Arrays.asList(27, 28, 50, 51, 104, 105, 31, 34, 74, 75, 76, 95, 111, 112);
        List<Integer> typeRock = Arrays.asList(74, 75, 76, 95, 111, 112, 138, 139, 140, 141, 142);
        List<Integer> typePsychic = Arrays.asList(63, 64, 65, 96, 97, 102, 103, 79, 80, 121, 122, 124);
        List<Integer> typeFairy = Arrays.asList(35, 36, 39, 40, 122);
        List<Integer> typeFighter = Arrays.asList(56, 57, 66, 67, 68, 106, 107, 62);
        List<Integer> typeIce = Arrays.asList(124, 87, 91, 131);
        List<Integer> typeGhost = Arrays.asList(92, 93, 94);
        List<Integer> typeSteel = Arrays.asList(81, 82);
        List<Integer> typeDragon = Arrays.asList(147, 148, 149);
        List<Integer> typeSpecial1 = Arrays.asList(144); //birds
        List<Integer> typeSpecial2 = Arrays.asList(145);
        List<Integer> typeSpecial3 = Arrays.asList(146);
        List<Integer> typeSpecial4 = Arrays.asList(151); //mew
        List<Integer> typeSpecial5 = Arrays.asList(150); //mewtwo

        if (typeNormal.contains(pokeNumber)) {
            type.add("typeNormal");
        }
        if (typeGrass.contains(pokeNumber)) {
            type.add("typeGrass");
        }
        if (typeFire.contains(pokeNumber)) {
            type.add("typeFire");
        }
        if (typeWater.contains(pokeNumber)) {
            type.add("typeWater");
        }
        if (typeBug.contains(pokeNumber)) {
            type.add("typeBug");
        }
        if (typePoison.contains(pokeNumber)) {
            type.add("typePoison");
        }
        if (typeFly.contains(pokeNumber)) {
            type.add("typeFly");
        }
        if (typeElectric.contains(pokeNumber)) {
            type.add("typeElectric");
        }
        if (typeGround.contains(pokeNumber)) {
            type.add("typeGround");
        }
        if (typeRock.contains(pokeNumber)) {
            type.add("typeRock");
        }
        if (typePsychic.contains(pokeNumber)) {
            type.add("typePsychic");
        }
        if (typeFairy.contains(pokeNumber)) {
            type.add("typeFairy");
        }
        if (typeFighter.contains(pokeNumber)) {
            type.add("typeFighter");
        }
        if (typeIce.contains(pokeNumber)) {
            type.add("typeIce");
        }
        if (typeGhost.contains(pokeNumber)) {
            type.add("typeGhost");
        }
        if (typeSteel.contains(pokeNumber)) {
            type.add("typeSteel");
        }
        if (typeDragon.contains(pokeNumber)) {
            type.add("typeDragon");
        }
        if (typeSpecial1.contains(pokeNumber)) {
            type.add("typeSpecial1");
        }
        if (typeSpecial2.contains(pokeNumber)) {
            type.add("typeSpecial2");
        }
        if (typeSpecial3.contains(pokeNumber)) {
            type.add("typeSpecial3");
        }
        if (typeSpecial4.contains(pokeNumber)) {
            type.add("typeSpecial4");
        }
        if (typeSpecial5.contains(pokeNumber)) {
            type.add("typeSpecial5");
        }
        return type;
    }

    public void battle(List<String> poke1types, List<String> poke2types) {
        // removed same type bonus. like poison vs poison. like ghost against normal and normal against ghost.
        // advantage counts as +2, it don't count +3 if the defense has bonus too.
        for (String type1 : poke1types) {
            for (String type2 : poke2types) {
                if (type1.equals("typeNormal")) {
                    if (type2.equals("typeRock")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeFighter")) poke2.setPower(poke2.getPower() + 2);
                }
                if (type1.equals("typeFighter")) {
                    if (type2.equals("typeNormal")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeRock")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeSteel")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeIce")) poke1.setPower(poke1.getPower() + 2);

                    if (type2.equals("typeFly")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typePoison")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typePsychic")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeFairy")) poke2.setPower(poke2.getPower() + 1);

                    if (type2.equals("typeGhost")) poke2.setPower(poke2.getPower() + 4);
                }
                if (type1.equals("typeFly")) {
                    if (type2.equals("typeGround")) poke1.setPower(poke1.getPower() + 4);

                    if (type2.equals("typeFighter")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 2);

                    if (type2.equals("typeRock")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeElectric")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeIce")) poke2.setPower(poke2.getPower() + 2);
                }
                if (type1.equals("typePoison")) {
                    if (type2.equals("typeFairy")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeFighter")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeGround")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typePsychic")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeRock")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeGhost")) poke2.setPower(poke2.getPower() + 1);

                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 4);
                }
                if (type1.equals("typeGround")) {
                    if (type2.equals("typeElectric")) poke1.setPower(poke1.getPower() + 4);

                    if (type2.equals("typeRock")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typePoison")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeSteel")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeFire")) poke1.setPower(poke1.getPower() + 2);

                    if (type2.equals("typeBug")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeWater")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeGrass")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeIce")) poke2.setPower(poke2.getPower() + 2);

                    if (type2.equals("typeFly")) poke2.setPower(poke2.getPower() + 4);
                }
                if (type1.equals("typeRock")) {
                    if (type2.equals("typeIce")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeFly")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeFire")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typePoison")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeNormal")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeFighter")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeGround")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeGrass")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeWater")) poke2.setPower(poke2.getPower() + 2);
                }
                if (type1.equals("typeBug")) {
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typePsychic")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGround")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typePoison")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeGhost")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeFairy")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeFly")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeRock")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeFire")) poke2.setPower(poke2.getPower() + 2);
                }
                if (type1.equals("typeGhost")) {
                    if (type2.equals("typeFighter")) poke1.setPower(poke1.getPower() + 4);

                    if (type2.equals("typePsychic")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typePoison")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 1);
                }
                if (type1.equals("typeSteel")) {
                    if (type2.equals("typePoison")) poke1.setPower(poke1.getPower() + 4);

                    if (type2.equals("typeIce")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeRock")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeFairy")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeNormal")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeFly")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typePsychic")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeDragon")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeFire")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeGround")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeFighter")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeWater")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeElectric")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeFire")) {
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeSteel")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeIce")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeFairy")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeGround")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeWater")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeRock")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeDragon")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeWater")) {
                    if (type2.equals("typeRock")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGround")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeFire")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeSteel")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeIce")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeElectric")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeGrass")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeDragon")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeGrass")) {
                    if (type2.equals("typeRock")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGround")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeWater")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeElectric")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeBug")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeFly")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typePoison")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeIce")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeFire")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeDragon")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeElectric")) {
                    if (type2.equals("typeFly")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeWater")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeSteel")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeDragon")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeGrass")) poke2.setPower(poke2.getPower() + 1);

                    if (type2.equals("typeGround")) poke2.setPower(poke2.getPower() + 4);
                }
                if (type1.equals("typePsychic")) {
                    if (type2.equals("typeFighter")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typePoison")) poke1.setPower(poke1.getPower() + 2);

                    if (type2.equals("typeBug")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeGhost")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeIce")) {
                    if (type2.equals("typeFly")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGround")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeDragon")) poke1.setPower(poke1.getPower() + 2);

                    if (type2.equals("typeFighter")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeRock")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeFire")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeWater")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeDragon")) {
                    if (type2.equals("typeFire")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeWater")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeGrass")) poke1.setPower(poke1.getPower() + 1);
                    if (type2.equals("typeElectric")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 1);
                    if (type2.equals("typeIce")) poke2.setPower(poke2.getPower() + 2);

                    if (type2.equals("typeFairy")) poke2.setPower(poke2.getPower() + 4);
                }
                if (type1.equals("typeFairy")) {
                    if (type2.equals("typeFighter")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeDragon")) poke1.setPower(poke1.getPower() + 2);
                    if (type2.equals("typeBug")) poke1.setPower(poke1.getPower() + 1);

                    if (type2.equals("typePoison")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeSteel")) poke2.setPower(poke2.getPower() + 2);
                    if (type2.equals("typeFire")) poke2.setPower(poke2.getPower() + 1);
                }
                if (type1.equals("typeSpecial1")) {
                    poke1.setPower(poke1.getPower() + 2);
                } //articuno
                if (type1.equals("typeSpecial2")) {
                    poke1.setPower(poke1.getPower() + 3);
                } //zapdos
                if (type1.equals("typeSpecial3")) {
                    poke1.setPower(poke1.getPower() + 1);
                } //moltres
                if (type1.equals("typeSpecial4")) {
                    poke1.setPower(poke1.getPower() + 4);
                } //mew
                if (type1.equals("typeSpecial5")) {
                    poke1.setPower(poke1.getPower() + 5);
                } //mewtwo

                if (type2.equals("typeSpecial1")) {
                    poke2.setPower(poke2.getPower() + 2);
                } //articuno
                if (type2.equals("typeSpecial2")) {
                    poke2.setPower(poke2.getPower() + 3);
                } //zapdos
                if (type2.equals("typeSpecial3")) {
                    poke2.setPower(poke2.getPower() + 1);
                } //moltres
                if (type2.equals("typeSpecial4")) {
                    poke2.setPower(poke2.getPower() + 4);
                } //mew
                if (type2.equals("typeSpecial5")) {
                    poke2.setPower(poke2.getPower() + 5);
                } //mewtwo
            }
        }
    }

    private String pokeName(int pokeNumber) {
        List<String> names = Arrays.asList("Null", "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle",
                "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto",
                "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash",
                "Nidoran-F", "Nidorina", "Nidoqueen", "Nidoran-M", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales",
                "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth",
                "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag",
                "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell",
                "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro",
                "Magnemite", "Magneton", "Farfetchd", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster",
                "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute",
                "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon",
                "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther",
                "Jynx", "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon",
                "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno",
                "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew");
        String pokeName = names.get(pokeNumber);
        return pokeName;
    }

    //aux of score font;
    private float layoutWidth(BitmapFont font, String text) {
        layout.reset();
        layout.setText(font, text);
        return layout.width;
    }

    @Override
    public void dispose() {
        if (poke1 != null && poke2 != null){
            poke1.dispose();
            poke2.dispose();
        }
        bg.dispose();
        bikeboy.dispose();
        clockIcon.dispose();
        versusIcon.dispose();
        startButton.dispose();
        restartButton.dispose();
        rankingButton.dispose();
        ranking2Button.dispose();
        backSound.dispose();
        font.dispose();
        batch.dispose();
    }
}
