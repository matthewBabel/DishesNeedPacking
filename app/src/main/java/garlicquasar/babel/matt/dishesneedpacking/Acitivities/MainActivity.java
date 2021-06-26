package garlicquasar.babel.matt.dishesneedpacking.Acitivities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

import garlicquasar.babel.matt.dishesneedpacking.HttpRequest.HttpHandler;
import garlicquasar.babel.matt.dishesneedpacking.Audio.MusicServiceHandler;
import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.GameDisplayFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.GameInputFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores.HighscoreTabFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores.LevelSelectHighscoreFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.LevelDetailFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.LevelSelectFragment;
import garlicquasar.babel.matt.dishesneedpacking.R;

//
public class MainActivity extends AppCompatActivity {


    private Button hsBtn;
    private SignInButton googleSignIn;
    private static TextInputEditText usernameEditText;
    private TextInputLayout usernameEditLayout;
    private static TextView userTxt;

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;

    private static ConstraintLayout parentActivityContainer;
    private static ConstraintLayout mainActivityGroup;
    private static ConstraintLayout bubbleGroup;

    public static FragmentManager fragmentManager;
    public static Context context;

    private boolean bubbleMovementRight[];
    private boolean bubbleMovementUp[];

    private ImageButton musicButton;
    private ImageButton sfxButton;

    public static boolean musicOn = true;
    public static boolean sfxOn = true;

    private static ValueAnimator bubbleAnimator[];
    private int bubbleAmt = 8;
    private int movementSpeed = 5;
    private int outerBounds = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // top bar re-color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.background));
        }

        if (MusicServiceHandler.isNotInitialized()) {
            MusicServiceHandler.setup(this);
        }

        context = this;
        hsBtn = findViewById(R.id.btnLocalHighscores);
        userTxt = findViewById(R.id.tvUsername);
        userTxt.setVisibility(View.INVISIBLE);
        googleSignIn = findViewById(R.id.googleSignInBtn);
        usernameEditText = findViewById(R.id.usernameEditText);
        usernameEditLayout = findViewById(R.id.usernameTF);
        parentActivityContainer = findViewById(R.id.mainActivityContainer);
        mainActivityGroup = findViewById(R.id.mainActivityGroup);
        bubbleGroup = findViewById(R.id.bubbleLayout);

        //privacy policy hyperlink


        TextView privacyPolicyTv =(TextView)findViewById(R.id.privacypolicy);
        privacyPolicyTv.setClickable(true);
        privacyPolicyTv.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://www.garlicquasar.com/dishesneedpacking/privacy/'>Privacy Policy</a>";
        privacyPolicyTv.setText(Html.fromHtml(text));

        fragmentManager = getSupportFragmentManager();

        TextView mainTxt = findViewById(R.id.mainNameText);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mainTxt.startAnimation(aniFade);

        // options buttons toggle on or off
        musicButton = findViewById(R.id.musicButton);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicToggle();
            }
        });
        sfxButton = findViewById(R.id.sfxButton);
        sfxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxToggle();
            }
        });

        setMusicButtonImage(musicOn);
        setSfxButtonImage(sfxOn);


        final int deviceWidth = getResources().getDisplayMetrics().widthPixels;
        final int deviceHeight = getResources().getDisplayMetrics().heightPixels;

        //bubble background animation
        ImageView[] bubbles = new ImageView[bubbleAmt];
        bubbleMovementRight = new boolean[bubbleAmt];
        bubbleMovementUp = new boolean[bubbleAmt];
        bubbleAnimator = new ValueAnimator[bubbleAmt];

        for (int i = 0; i < bubbleAmt; i++) {
            bubbles[i] = new ImageView(this);
            bubbles[i].setImageResource(R.drawable.bubble_silver);
        }

        setBubbleImagesRandomly(deviceWidth, deviceHeight, bubbles);

        for (int i = 0; i < bubbleAmt; i++) {
            initBackgroundAnimationForBubble(deviceWidth, deviceHeight, bubbles[i], i);
        }


        /* username edit text listener */
        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            saveUsername();
                            return true;
                        default:
                            break;
                    }
                }

                return false;
            }
        });

        /* google sign in listener*/
        googleSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.googleSignInBtn:
                        googleSignIn();
                        break;
                }
            }
        });

        /* google sign in options */
        signInSilently();
    }

    /** fragment controls **/
    public void levelSelect(View view) {
        SoundEffectPlayer.playClick(this);
        turnOffBubbles();
        parentActivityContainer.removeView(bubbleGroup);
        LevelSelectFragment fragment = new LevelSelectFragment(1);
        startFragment(fragment, "Level Select");
    }

    public void playCustomGame(View view) {
        SoundEffectPlayer.playClick(this);
        turnOffBubbles();
        parentActivityContainer.removeView(bubbleGroup);
        GameInputFragment fragment = new GameInputFragment();
        startFragment(fragment, "Game Input");
    }

    public void viewHighScores(View view) {
        updateScores();
        SoundEffectPlayer.playClick(this);
        parentActivityContainer.removeView(bubbleGroup);
        HighscoreTabFragment fragment = new HighscoreTabFragment();
        LevelSelectHighscoreFragment.levelSelected = 1;
        startFragment(fragment, "Highscore Tab");
    }

    public static void startFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainActivityContainer, fragment, tag);
        fragmentTransaction.commit();
        mainActivityGroup.setVisibility(View.INVISIBLE);
    }

    public static void backAFragment(String fragmentTag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);

        if (fragment != null) {
            fragmentTransaction.remove(fragment).commit();
        }
    }

    public static void backToMain(boolean turnOnBubbles) {
        if (turnOnBubbles) {
            turnOnBubbles();
        }

        removeAllFragments();
        addBubblesToMain();
        mainActivityGroup.setVisibility(View.VISIBLE);
    }

    private boolean isOnFragment(String fragmentTag) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            try {
                if (fragment != null && fragment.getTag().equals(fragmentTag)) {
                    return true;
                }
            } catch (NullPointerException e) {
                // do nothing
            }
        }

        return false;
    }

    private boolean isShowingMainActivity() {
        return mainActivityGroup.getVisibility() == View.VISIBLE;
    }


    private static void removeAllFragments() {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }

    public static void goToLevelDetailScreen(int level) {
        removeAllFragments();
        LevelSelectFragment levelSelectFragment = new LevelSelectFragment(level);
        LevelDetailFragment fragment = new LevelDetailFragment(level);
        startFragment(levelSelectFragment, "Level Select");
        startFragment(fragment, "Level Detail");
    }

    public static void goToGameDisplayScreen(int level) {
        removeAllFragments();

        LevelSelectFragment levelSelectFragment = new LevelSelectFragment(level);
        LevelDetailFragment detailFragment = new LevelDetailFragment(level);


        double[] params = LevelDetailFragment.levelParameters[level - 1];
        GameDisplayFragment fragment = new GameDisplayFragment((int) params[0], (int) params[1],
                (int) params[2], (int)params[3], params[4], (int)params[5], (int)params[6],
                (int)params[7], (int)params[8], (int)params[9], (int)params[10], false);

        startFragment(levelSelectFragment, "Level Select");
        startFragment(detailFragment, "Level Detail");
        startFragment(fragment, "Game Display");
    }

    public static void goToGameDisplayScreenFromCustom(int columnInput, int floorInput, int tupperInput, int bowlInput, double cupInput, int cornerInt, int directionInt, int utensilAmount) {
        removeAllFragments();
        GameInputFragment customFragment = new GameInputFragment();
        GameDisplayFragment fragment = new GameDisplayFragment(columnInput, floorInput,
                tupperInput, bowlInput, cupInput, cornerInt, directionInt,
                utensilAmount, 0, 0, 0, true);

        startFragment(customFragment, "Game Input");
        startFragment(fragment, "Game Display");
    }

    /** bubble controls **/
    private void setBubbleImagesRandomly(int deviceWidth, int deviceHeight, ImageView[] imgs) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(deviceWidth/7, deviceWidth/7);
        ConstraintLayout layoutOuter = findViewById(R.id.mainActivityContainer);
        ConstraintLayout layoutInner = findViewById(R.id.bubbleLayout);


        int count = 0;
        for (ImageView img: imgs) {
            img.setLayoutParams(layoutParams);
            img.setX(new Random().nextInt(deviceWidth));
            img.setY(new Random().nextInt(deviceHeight));
            img.setAlpha(0.7f);
            bubbleMovementRight[count] = new Random().nextBoolean();
            bubbleMovementUp[count] = new Random().nextBoolean();
            layoutInner.addView(img);
            count++;
        }

        setContentView(layoutOuter);
    }

    private void initBackgroundAnimationForBubble(final int deviceWidth, final int deviceHeight, final ImageView img, final int index) {
        bubbleAnimator[index] = ValueAnimator.ofInt(0, 1);
        ValueAnimator animator = bubbleAnimator[index];
        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (bubbleMovementRight[index]) {
                    img.setX(img.getX()+movementSpeed);

                    if (img.getX() >= deviceWidth+outerBounds) {
                        bubbleMovementRight[index] = false;
                    }
                } else {
                    img.setX(img.getX()-movementSpeed);

                    if (img.getX() <= 0-(outerBounds*8)) {
                        bubbleMovementRight[index] = true;
                    }
                }

                if (bubbleMovementUp[index]) {
                    img.setY(img.getY()-movementSpeed);

                    if (img.getY() <= 0-(outerBounds*8)) {
                        bubbleMovementUp[index] = false;
                    }
                } else {
                    img.setY(img.getY()+movementSpeed);

                    if (img.getY() >= deviceHeight+outerBounds) {
                        bubbleMovementUp[index] = true;
                    }
                }
            }
        });
        animator.start();
    }

    public static void turnOffBubbles() {
        if (bubbleGroup.getVisibility() != View.INVISIBLE) {
            bubbleGroup.setVisibility(View.INVISIBLE);
            for (ValueAnimator animator : bubbleAnimator) {
                animator.pause();
            }
        }
    }

    public static void turnOnBubbles() {
        if (bubbleGroup.getVisibility() != View.VISIBLE) {
            bubbleGroup.setVisibility(View.VISIBLE);
            for (ValueAnimator animator : bubbleAnimator) {
                animator.start();
            }
        }
    }

    public static void addBubblesToFragment(String fragmentName) {
        switch(fragmentName) {
            case "Highscore Tab":
                HighscoreTabFragment.layout.addView(bubbleGroup);
                break;
            default:
                break;
        }
    }

    public static void removeBubblesFromFragment(String fragmentName) {
        switch(fragmentName) {
            case "Highscore Tab":
                HighscoreTabFragment.layout.removeView(bubbleGroup);
                break;
            default:
                break;
        }
    }

    public static void addBubblesToMain() {
        parentActivityContainer.addView(bubbleGroup);
    }

    /** data updating **/
    private static void updateScores() {
        HttpHandler api = new HttpHandler();
        api.updateGlobalHighscoreData();

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), MODE_PRIVATE);
        if (preferences.getString("loggedIn", "false").equals("true")) {
            api.updateUserHighscoreData(context);
        }
    }

    public static void setUsernameTextFromPreferences(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(c.getString(R.string.preference_file_key), c.MODE_PRIVATE);
        String username = preferences.getString("username", "");

        userTxt.setText("Welcome " + username + "!");
        Animation aniUserFade = AnimationUtils.loadAnimation(c.getApplicationContext(), R.anim.fade_in_user);
        userTxt.startAnimation(aniUserFade);
        userTxt.setVisibility(View.VISIBLE);

        if (usernameEditText != null && username != null) {
            usernameEditText.setHint("");
            usernameEditText.setText(username);
        }
    }

    private void saveUsername() {
        String username = usernameEditText.getEditableText().toString();

        boolean pass = isProfanity(username);

        if (!pass) {
            makeToast(this, "No Profanity Please", 4000);
        } else if (username.length() > 30) {
            makeToast(this, "Username Too Long", 4000);
        } else {
            HttpHandler api = new HttpHandler();
            api.updateUsername(this, username);

            SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            preferences.edit().putString("username", username).apply();
            preferences.edit().commit();
            setUsernameTextFromPreferences(this);
        }
    }

    /** User Login / Logout **/
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            signInGoogleUser(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(null, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void signInGoogleUser(GoogleSignInAccount account) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        if (preferences.getString("loggedIn", "false").equals("false")) {
            String idToken = account.getIdToken();
            // sending google account to my server
            HttpHandler api = new HttpHandler();
            api.googleLoginUser(this, idToken);
        } else if(!preferences.getString("username", "").equals("")) {
            setUsernameTextFromPreferences(this);
        }

        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            makeToast(this, "Signed In", 2000);
            Button signoutBtn = findViewById(R.id.googleSignOutBtn);
            signoutBtn.setVisibility(View.VISIBLE);
            usernameEditLayout.setVisibility(View.VISIBLE);
            googleSignIn.setVisibility(View.INVISIBLE);
            signoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        } else {
            googleSignIn.setVisibility(View.VISIBLE);
            usernameEditLayout.setVisibility(View.INVISIBLE);
            Button signoutBtn = findViewById(R.id.googleSignOutBtn);
            signoutBtn.setVisibility(View.INVISIBLE);
        }
    }

    /* logout and make google sign in visible again */
    private void logout() {
        HttpHandler api = new HttpHandler();
        api.logout(this);

        googleSignIn.setVisibility(View.VISIBLE);
        usernameEditLayout.setVisibility(View.INVISIBLE);
        Button signoutBtn = findViewById(R.id.googleSignOutBtn);
        signoutBtn.setVisibility(View.INVISIBLE);

        userTxt.setText("");
        mGoogleSignInClient.signOut();
        MainActivity.makeToast(this, "Signed Out", 2000);
    }

    private void signInSilently() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (GoogleSignIn.hasPermissions(account, gso.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            GoogleSignInAccount signedInAccount = account;
            signInGoogleUser(signedInAccount);
        } else {
            /* google client */
            mGoogleSignInClient
                    .silentSignIn()
                    .addOnCompleteListener(
                            this,
                            new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    if (task.isSuccessful()) {
                                        // The signed in account is stored in the task's result.
                                        GoogleSignInAccount signedInAccount = task.getResult();
                                        signInGoogleUser(signedInAccount);
                                    } else {
                                        makeToast(MainActivity.this, "Not Signed In", 3000);
                                    }
                                }
                            });
        }
    }

    /** Overridden Activity control points**/
    @Override
    protected void onResume() {
        super.onResume();
        updateScores();

        TextView usernameText = findViewById(R.id.tvUsername);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        if (preferences.getString("loggedIn", "false").equals("true")) {
            usernameText.setText("Welcome " + preferences.getString("username", "") + "!");
        }

        if (isShowingMainActivity() || isOnFragment("Highscore Tab")) {
            turnOnBubbles();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MusicServiceHandler.pauseMenuAudio();
        MusicServiceHandler.pauseGameAudio();
        turnOffBubbles();
    }

    // turn on audio and bubbles if applicable
    @Override
    protected void onRestart() {
        super.onRestart();
        if (fragmentManager != null && isOnFragment("Game Display")) {
            MusicServiceHandler.startGameAudio();
        } else {
            MusicServiceHandler.startMenuAudio();

            if (isShowingMainActivity() || isOnFragment("Highscore Tab")) {
                turnOnBubbles();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicServiceHandler.killAll();
    }



    /** audio buttons control **/
    private void musicToggle() {
        if (musicOn) {
            makeToast(this, "Music Off", 2000);
            musicButton.setImageDrawable(getResources().getDrawable(R.drawable.music_symbol_off));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    MusicServiceHandler.stopMenuAudio();
                }
            });

            musicOn = false;
        } else {
            makeToast(this, "Music On", 2000);
            musicButton.setImageDrawable(getResources().getDrawable(R.drawable.music_symbol));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    MusicServiceHandler.startMenuAudio();
                }
            });

            musicOn = true;
        }
    }

    private void sfxToggle() {
        if (sfxOn) {
            makeToast(this, "Sound Effects Off", 2000);
            sfxButton.setImageDrawable(getResources().getDrawable(R.drawable.sfx_symbol_off));
            sfxOn = false;
        } else {
            makeToast(this, "Sound Effects On", 2000);
            sfxButton.setImageDrawable(getResources().getDrawable(R.drawable.sfx_symbol));
            sfxOn = true;
        }
    }

    private void setMusicButtonImage(boolean on) {
        if (on) {
            musicButton.setImageDrawable(getResources().getDrawable(R.drawable.music_symbol));
        } else {
            musicButton.setImageDrawable(getResources().getDrawable(R.drawable.music_symbol_off));
        }
    }

    private void setSfxButtonImage(boolean on) {
        if (on) {
            sfxButton.setImageDrawable(getResources().getDrawable(R.drawable.sfx_symbol));
        } else {
            sfxButton.setImageDrawable(getResources().getDrawable(R.drawable.sfx_symbol_off));
        }
    }

    /** helper functions **/
    public static void makeToast(Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //going through a large array of profanity words and phrases to act as a crude profanity filter
    //still will not catch everything which is nearly impossible without human moderation
    public static boolean isProfanity(String username) {
        String[] badwords = context.getResources().getStringArray(R.array.profanity);
        boolean pass = true;
        for (String bad : badwords) {
            if (username.equals(bad)) {
                pass = false;
                break;
            }
        }
        return pass;
    }
}
