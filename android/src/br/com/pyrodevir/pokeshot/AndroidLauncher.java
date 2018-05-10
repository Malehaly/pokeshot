package br.com.pyrodevir.pokeshot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.appodeal.gdx.GdxAppodeal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication implements PlayServices{
    private boolean _doubleBackToExitPressedOnce    = false;
    private GameHelper gameHelper;
    private final static int requestCode = 1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {

            }

            @Override
            public void onSignInSucceeded() {

            }
        };
        gameHelper.setup(gameHelperListener);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MainClass(this), config);

        String appKey = "4f812ebe447e7b379963f9a4cb5ad70da26bc64f8b04de5b";
        GdxAppodeal.disableLocationPermissionCheck();
        GdxAppodeal.initialize(appKey, GdxAppodeal.INTERSTITIAL | GdxAppodeal.BANNER);
	}

    @Override
    public void onBackPressed() {

        Log.i(TAG, "onBackPressed--");
        if (_doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this._doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to quit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                _doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void singIn() {
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e){
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage());
        }
    }

    @Override
    public void singOut() {
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e){
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage());
        }
    }

    @Override
    public void submitScore(int score) {
        if (isSignedIn()){
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(
                    R.string.leaderboard_ranking__score), score);
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn()){
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_ranking__score)), requestCode);
        } else {
            singIn();
        }
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }
}
