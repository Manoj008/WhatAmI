package com.manoj.whatami;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14, alpha, delate, button;
    Button[] buttons;
    Button bb1, bb2, bb3, bb4, bb5, bb6, bb7, bb8, bb9, bb10;
    Button[] bbuttons;
    Random random = new Random();
    String ans;
    int[] occupy = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] gotHint;
    int number;
    HashMap<Integer, Integer> filled,ansHash,hash;
    boolean b = false;
    LinearLayout now;
    RelativeLayout next;
    ImageView gem1, gem2, gem3;
    int level = 0;
    TextView textLevel, textCoins, gemText, ques;
    ImageView imageView;

    Dialog correctDialog, deleteDialog;
    Button yesc, noc, yesd, nod;
    TextView text, increaseLevel, watchText;
    ProgressBar load;
    ImageView back;

    Animation bouncy;
    SharedPreferences sharedPreferences;

    String[] quesList;
    String[] ansList;

    int coins = 300;

    RelativeLayout main;

    MediaPlayer ans_correct;
    MediaPlayer coins_i;
    MediaPlayer level_i;

    Dialog dialog;
    boolean yes;
    Button playVideo;

    AdColonyInterstitialListener listener;
    AdColonyAdOptions options;
    AdColonyInterstitial ad;

    String str;
    HashMap<Integer,Integer> answer,hints,vanish;
    String question="";

    final private String APP_ID = "app94ee12ff9db6430bac";
    final private String ZONE_ID = "vze9a6e1f933794646b9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);

        filled=new HashMap<Integer, Integer>();
        ansHash=new HashMap<Integer, Integer>();
        hints=new HashMap<Integer, Integer>();
        vanish=new HashMap<Integer, Integer>();

        question=sharedPreferences.getString("question","");
        ansHash=getHashMap("answer");
        hints=getHashMap("hints");
        if(hints==null)
            hints=new HashMap<>();
        coins = sharedPreferences.getInt("coins", 300);
        level = sharedPreferences.getInt("level", 0);
        yes = sharedPreferences.getBoolean("sound", true);
        ans_correct = MediaPlayer.create(this, R.raw.anscorrect);
        level_i = MediaPlayer.create(this, R.raw.levelin);
        coins_i = MediaPlayer.create(this, R.raw.coin);

        main = findViewById(R.id.main);
        textLevel = findViewById(R.id.levelText);
        now = findViewById(R.id.now);
        next = findViewById(R.id.next);

        ques = findViewById(R.id.ques);

        gem1 = findViewById(R.id.gem1);
        gem2 = findViewById(R.id.gem2);
        gem3 = findViewById(R.id.gem3);
        textCoins = findViewById(R.id.coins);
        gemText = findViewById(R.id.gemtext);
        textCoins.setText(String.valueOf(coins));
        back = findViewById(R.id.back);
        increaseLevel = findViewById(R.id.increaseLevel);
        increaseLevel.setText(String.valueOf(level + 1));
        imageView = findViewById(R.id.image1);

        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("userid");

/** Pass options with user id set with configure */
        AdColony.configure(this, appOptions, APP_ID, ZONE_ID);

        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward adColonyReward) {
                load.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "20 coins are added in your account", Toast.LENGTH_LONG).show();
                coins = coins + 20;
                textCoins.setText(String.valueOf(coins));
                sharedPreferences.edit().putInt("coins", coins).apply();
                watchText.setText("watch video to get 30 coins");
            }
        });

        quesList = Main2Activity.quesList;

        ansList = Main2Activity.ansList;


        bouncy = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.watch);
        playVideo = dialog.findViewById(R.id.watchV);
        load=dialog.findViewById(R.id.load);
        watchText = dialog.findViewById(R.id.watchText);
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load.setVisibility(View.VISIBLE);
                if (ad != null) {
                    dialog.dismiss();
                    ad.show();
                } else {
                    AdColony.requestInterstitial(ZONE_ID, listener, options);
                }
            }
        });

        correctDialog = new Dialog(MainActivity.this);
        correctDialog.setContentView(R.layout.correctword);
        yesc = correctDialog.findViewById(R.id.yes);
        noc = correctDialog.findViewById(R.id.no);
        text = correctDialog.findViewById(R.id.text);
        text.setText("do you want to reveal a correct letter for 50 coins");
        yesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coins >= 50) {
                    getHint();
                    coins = coins - 50;
                    textCoins.setText(String.valueOf(coins));
                    sharedPreferences.edit().putInt("coins", coins).apply();

                } else {
                    watchText.setText("You do not have sufficient coins\n" +
                            "watch video to get 30 coins");
                    watchVideo();
                }
                correctDialog.dismiss();
            }
        });
        noc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctDialog.dismiss();
            }
        });
        deleteDialog = new Dialog(MainActivity.this);
        deleteDialog.setContentView(R.layout.correctword);
        yesd = deleteDialog.findViewById(R.id.yes);
        nod = deleteDialog.findViewById(R.id.no);
        text = deleteDialog.findViewById(R.id.text);
        text.setText("Do you want to delete letters that are not part of the solution for 80 coins");
        yesd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coins >= 80 && !b) {
                    coins=coins-80;
                    textCoins.setText(String.valueOf(coins));
                    sharedPreferences.edit().putInt("coins", coins).apply();
                    deleteExtra();
                }
                else {
                    watchText.setText("You do not have sufficient coins\n" +
                            "watch video to get 30 coins");
                    watchVideo();
                }
                deleteDialog.dismiss();
            }
        });

        nod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo();
            }
        });
        textCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchVideo();
            }
        });

        buttons = new Button[]{b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12, h13, h14, alpha, delate};
        bbuttons = new Button[]{bb1, bb2, bb3, bb4, bb5, bb6, bb7, bb8, bb9, bb10};


        button = findViewById(R.id.button);

        bbuttons[0] = (Button) findViewById(R.id.bb1);
        bbuttons[1] = (Button) findViewById(R.id.bb2);
        bbuttons[2] = (Button) findViewById(R.id.bb3);
        bbuttons[3] = (Button) findViewById(R.id.bb4);
        bbuttons[4] = (Button) findViewById(R.id.bb5);
        bbuttons[5] = (Button) findViewById(R.id.bb6);
        bbuttons[6] = (Button) findViewById(R.id.bb7);
        bbuttons[7] = (Button) findViewById(R.id.bb8);
        bbuttons[8] = (Button) findViewById(R.id.bb9);
        bbuttons[9] = (Button) findViewById(R.id.bb10);


        buttons[0] = (Button) findViewById(R.id.b1);

        buttons[1] = (Button) findViewById(R.id.b2);

        buttons[2] = (Button) findViewById(R.id.b3);

        buttons[3] = (Button) findViewById(R.id.b4);

        buttons[4] = (Button) findViewById(R.id.b5);

        buttons[5] = (Button) findViewById(R.id.b6);

        buttons[6] = (Button) findViewById(R.id.b7);

        buttons[7] = (Button) findViewById(R.id.b8);

        buttons[8] = (Button) findViewById(R.id.b9);

        buttons[9] = (Button) findViewById(R.id.b10);

        buttons[10] = (Button) findViewById(R.id.h1);

        buttons[11] = (Button) findViewById(R.id.h2);

        buttons[12] = (Button) findViewById(R.id.h3);

        buttons[13] = (Button) findViewById(R.id.h4);

        buttons[14] = (Button) findViewById(R.id.h5);

        buttons[15] = (Button) findViewById(R.id.h6);

        buttons[16] = (Button) findViewById(R.id.h7);

        buttons[17] = (Button) findViewById(R.id.h8);

        buttons[18] = (Button) findViewById(R.id.h9);

        buttons[19] = (Button) findViewById(R.id.h10);

        buttons[20] = (Button) findViewById(R.id.h11);

        buttons[21] = (Button) findViewById(R.id.h12);

        buttons[22] = (Button) findViewById(R.id.h13);

        buttons[23] = (Button) findViewById(R.id.h14);

        buttons[24] = (Button) findViewById(R.id.alpha);

        buttons[25] = (Button) findViewById(R.id.delete);

        for (int i = 0; i < buttons.length; i++)
            buttons[i].setEnabled(false);
        set();


        button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (yes) {
                            coins_i.start();
                        }
                        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.won);
                        gem1.startAnimation(animation1);
                        gemText.setVisibility(View.GONE);
                    }
                }, 0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.won);
                        gem2.startAnimation(animation2);
                    }
                }, 175);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.won);
                        gem3.startAnimation(animation3);
                        animation3.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                gem1.setVisibility(View.INVISIBLE);
                                gem2.setVisibility(View.INVISIBLE);
                                gem3.setVisibility(View.INVISIBLE);
                                textCoins.setText(String.valueOf(coins));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }
                }, 350);
                set();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        next.setVisibility(View.GONE);
                        now.setVisibility(View.VISIBLE);
                    }
                }, 600);
            }
        });

        for (int i = 0; i < 26; i++) {
            buttons[i].setOnClickListener(this);
        }
        runnable.run();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable1.run();
            }
        }, 500);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            buttons[24].startAnimation(bouncy);
            new Handler().postDelayed(runnable, 7000);
        }
    };

    final Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            buttons[25].startAnimation(bouncy);
            new Handler().postDelayed(runnable1, 7000);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b1:
                remove(0);
                break;
            case R.id.b2:
                remove(1);
                break;
            case R.id.b3:
                remove(2);
                break;
            case R.id.b4:
                remove(3);
                break;
            case R.id.b5:
                remove(4);
                break;
            case R.id.b6:
                remove(5);
                break;
            case R.id.b7:
                remove(6);
                break;
            case R.id.b8:
                remove(7);
                break;
            case R.id.b9:
                remove(8);
                break;
            case R.id.b10:
                remove(9);
                break;
            case R.id.h1:
                fill(10);
                break;
            case R.id.h2:
                fill(11);
                break;
            case R.id.h3:
                fill(12);
                break;
            case R.id.h4:
                fill(13);
                break;
            case R.id.h5:
                fill(14);
                break;
            case R.id.h6:
                fill(15);
                break;
            case R.id.h7:
                fill(16);
                break;
            case R.id.h8:
                fill(17);
                break;
            case R.id.h9:
                fill(18);
                break;
            case R.id.h10:
                fill(19);
                break;
            case R.id.h11:
                fill(20);
                break;
            case R.id.h12:
                fill(21);
                break;
            case R.id.h13:
                fill(22);
                break;
            case R.id.h14:
                fill(23);
                break;
            case R.id.alpha:
                correctDialog.show();
                break;
            case R.id.delete:
                if (!b)
                    deleteDialog.show();
                break;
        }
    }

    private void getHint() {
        int i = ans.indexOf('-');
        int h = check(i, random.nextInt(ans.length()));
        if (gotHint[h] == 0) {
            if (!buttons[h].getText().toString().equals("")) {
                buttons[filled.get(h)].setVisibility(View.VISIBLE);
            }
            buttons[h].setText(buttons[ansHash.get(h)].getText().toString());
            buttons[h].setTextColor(Color.CYAN);
            buttons[ansHash.get(h)].setVisibility(View.GONE);
            filled.put(h, ansHash.get(h));
            gotHint[h] = 1;
            hints.put(h,1);
            saveHashMap("hints",hints);
        } else {
            conflict();
        }
        isComplete();
    }

    private int check(int i, int hi) {
        int h = 0;
        if (i == hi) {
            check(i, random.nextInt(ans.length()));
        } else {
            h = hi;
        }
        return h;
    }

    private void conflict() {
        for (int i = 0; i < ans.length(); i++) {
            if (gotHint[i] == 0 && !String.valueOf(ans.charAt(i)).equals("-")) {
                buttons[i].setText(buttons[ansHash.get(i)].getText().toString());
                buttons[i].setTextColor(Color.CYAN);
                buttons[ansHash.get(i)].setVisibility(View.GONE);
                filled.put(i, ansHash.get(i));
                gotHint[i] = 1;
                hints.put(i,1);
                saveHashMap("hints",hints);
                break;
            }
        }
    }

    private void remove(int button) {
        if (!buttons[button].getText().equals("") && gotHint[button] == 0 && !String.valueOf(ans.charAt(button)).equals("-")) {
            int num = filled.get(button);
            filled.remove(button);
            buttons[button].setText("");
            buttons[num].setVisibility(View.VISIBLE);
        }
    }

    private void fill(int button) {
        for (int i = 0; i < ans.length(); i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText(String.valueOf(buttons[button].getText()));
                buttons[button].setVisibility(View.GONE);
                filled.put(i, button);
                isComplete();
                break;
            }
        }
    }

    private void isComplete() {

        for (int j = 0; j < bbuttons.length; j++) {
            bbuttons[j].setVisibility(View.VISIBLE);
        }
        char[] completed = new char[ans.length()];
        for (int i = 0; i < ans.length(); i++) {
            if (!buttons[i].getText().toString().equals("")) {
                completed[i] = buttons[i].getText().toString().charAt(0);
            }
        }

        String myAns = new String(completed);
        if (myAns.equals(ans)) {
            sharedPreferences.edit().putBoolean("deleted",false).apply();
            sharedPreferences.edit().putString("question","").apply();
            sharedPreferences.edit().putString("answer",null).apply();
            sharedPreferences.edit().putString("vanish",null).apply();
            sharedPreferences.edit().putString("hints",null).apply();
            question="";
            answer=new HashMap<>();
            vanish=new HashMap<>();
            hints=new HashMap<>();
            b = false;
            for (int i = 0; i < buttons.length; i++)
                buttons[i].setEnabled(false);
            button.setEnabled(true);

            for (int j = 0; j < ans.length(); j++) {
                bbuttons[j].setText(buttons[j].getText());
            }
            for (int j = ans.length(); j < bbuttons.length; j++) {
                bbuttons[j].setVisibility(View.GONE);
            }

            if (yes)
                ans_correct.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    next.setVisibility(View.VISIBLE);
                    now.setVisibility(View.GONE);

                    gem1.setVisibility(View.VISIBLE);
                    gem2.setVisibility(View.VISIBLE);
                    gem3.setVisibility(View.VISIBLE);
                    gemText.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 10; i++) {
                        occupy[i] = 0;
                    }
                    for (int i = 0; i < 10; i++) {
                        buttons[i].setText("");
                        buttons[i].setTextColor(Color.WHITE);
                    }

                    for (int i = 0; i < 10; i++) {
                        buttons[i].setText("");
                    }

                    for (int i = 0; i < 24; i++)
                        buttons[i].setVisibility(View.VISIBLE);
                    level = level + 1;
                    coins = coins + 10;
                    sharedPreferences.edit().putInt("level", level).apply();
                    sharedPreferences.edit().putInt("coins", coins).apply();

                }
            }, 500);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (yes)
                        level_i.start();
                    increaseLevel.setText(String.valueOf(level + 1));
                }
            }, 1300);


            for (int i = 0; i < 10; i++) {

                buttons[i].setTextColor(Color.GREEN);
            }

        }
    }

    public void occupied() {
        int n = 10 + random.nextInt(10);
        if (occupy[n - 10] == 1) {
            occupied();
        } else
            number = n;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveHashMap("answer",ansHash);
        saveHashMap("vanish",filled);
    }

    public void set() {

        ans = ansList[level];
        gotHint = new int[ans.length()];

        textLevel.setText(String.valueOf(level + 1));
        for (int i = 0; i < buttons.length; i++)
            buttons[i].setEnabled(true);
        textCoins.setText(String.valueOf(coins));

        ques.setText(String.valueOf(quesList[level]));
        for (int i = 0; i < ans.length(); i++) {
            gotHint[i] = 0;
        }

        for (int i = ans.length(); i < 10; i++) {
            buttons[i].setVisibility(View.GONE);
        }


        if(!question.equals("")){
            for(int j=0;j<question.length();j++){
                buttons[j+10].setText(question.charAt(j)+"");
            }
            for(int i:hints.keySet()){
                gotHint[i]=hints.get(i);
                buttons[i].setTextColor(Color.CYAN);
                buttons[i].setText(buttons[ansHash.get(i)].getText()+"");

            }
        }
        else {
            StringBuilder st=new StringBuilder();
            ansHash=new HashMap<>();
            for (int i = 0; i < ans.length(); i++) {
                occupied();
                buttons[number].setText(String.valueOf(ans.charAt(i)));
                ansHash.put(i, number);
                occupy[number - 10] = 1;
            }
            saveHashMap("answer",ansHash);
            for (int i = 10; i < 24; i++) {
                if (occupy[i - 10] == 0) {
                    buttons[i].setText(String.valueOf((char) (65 + random.nextInt(24))));
                    occupy[i - 10] = 1;
                }
            }
            for(int i=10;i<24;i++){
                st.append(buttons[i].getText()+"");
            }
            sharedPreferences.edit().putString("question",st.toString()).apply();
        }
        if(sharedPreferences.getBoolean("deleted",false)){
            deleteExtra();
        }
    }

    public void watchVideo() {
        load.setVisibility(View.GONE);
        dialog.show();
        AdColony.requestInterstitial(ZONE_ID, listener, options);
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                playVideo.setEnabled(true);
                ad=adColonyInterstitial;
            }
        };
    }

    public void saveHashMap(String key, Object obj) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public HashMap<Integer, Integer> getHashMap(String key) {
        SharedPreferences prefs = sharedPreferences;
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        java.lang.reflect.Type type = new TypeToken<HashMap<Integer, Integer>>() {}.getType();
        HashMap<Integer, Integer> obj = gson.fromJson(json, type);
        return obj;
    }

    public void deleteExtra(){
        for (int i = 10; i < 24; i++) {
            buttons[i].setVisibility(View.GONE);
        }

        for (int i = 0; i < ans.length(); i++) {
            if (gotHint[i] == 0 && !String.valueOf(ans.charAt(i)).equals("-")) {
                filled.remove(i);
                buttons[i].setText("");
                buttons[ansHash.get(i)].setVisibility(View.VISIBLE);
            }
        }

        b = true;
        sharedPreferences.edit().putBoolean("deleted",true).apply();
    }

}
