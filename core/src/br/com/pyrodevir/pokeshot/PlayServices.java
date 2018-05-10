package br.com.pyrodevir.pokeshot;

public interface PlayServices {

    void singIn();
    void singOut();

    void submitScore(int score);
    void showScore();

    boolean isSignedIn();

}
