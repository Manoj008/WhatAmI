package com.manoj.whatami;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;

public class Main2Activity extends AppCompatActivity {

    Button playButton;
    SharedPreferences sharedPreferences;
    boolean yes = true;
    int level = 0;
    TextView levelText;
    ImageView sound;
    int coins=300;
    TextView textCoins;
    ImageView imageView;

    Button play,rate,share;
    Dialog dialog;
    AdColonyInterstitialListener listener;
    AdColonyAdOptions options;
    AdColonyInterstitial ad;

    ProgressBar load;
    Button playVideo;

    final private String APP_ID = "app94ee12ff9db6430bac";
    final private String ZONE_ID = "vze9a6e1f933794646b9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        AdColony.configure(this, APP_ID, ZONE_ID);

        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward adColonyReward) {
                load.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "20 coins are added in your account", Toast.LENGTH_LONG).show();
                coins = coins + 20;
                textCoins.setText(String.valueOf(coins));
                sharedPreferences.edit().putInt("coins", coins).apply();
            }
        });


        playButton = findViewById(R.id.playButton);
        rate=findViewById(R.id.rate);
        share=findViewById(R.id.share);
        levelText = findViewById(R.id.levelText);

        textCoins = findViewById(R.id.coins);
        imageView = findViewById(R.id.imageView);
        sound = findViewById(R.id.sound);
        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        yes = sharedPreferences.getBoolean("sound", true);
        level = sharedPreferences.getInt("level", 0);
        levelText.setText(String.valueOf(level + 1));

        coins = sharedPreferences.getInt("coins", 300);
        textCoins.setText(String.valueOf(coins));
        if (yes)
            sound.setBackgroundResource(R.drawable.sound_on);
        else
            sound.setBackgroundResource(R.drawable.sound_off);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.manoj.whatami"));
                Intent chooser=Intent.createChooser(intent,"Rate What Am I? on PlayStore");
                startActivity(chooser);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.manoj.whatami");
                intent.putExtra(Intent.EXTRA_SUBJECT,"What Am I?");
                Intent chooser=Intent.createChooser(intent,"share What Am I? With Friends");
                startActivity(chooser);
            }
        });

        dialog = new Dialog(Main2Activity.this);
        dialog.setContentView(R.layout.watch);
        playVideo = dialog.findViewById(R.id.watchV);
        load=dialog.findViewById(R.id.load);
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load.setVisibility(View.VISIBLE);
                if (ad != null) {
                    dialog.dismiss();
                    ad.show();
                }
                else{
                    AdColony.requestInterstitial(ZONE_ID,listener,options);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchVideo();
            }
        });

        textCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchVideo();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes=sharedPreferences.getBoolean("sound",false);
                if (yes) {
                    sharedPreferences.edit().putBoolean("sound", false).commit();
                    sound.setBackgroundResource(R.drawable.sound_off);
                }
                else{
                    sharedPreferences.edit().putBoolean("sound", true).commit();
                    sound.setBackgroundResource(R.drawable.sound_on);

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public static String[] quesList = new String[]{
            "I eat, i live. I breathe, i live. I drink, i die.\n What am I ?? ",
            "I am seed with three letters in my name. Take away the last two and i still sound the same.\n What am I ?? ",
            "I am black when you buy me, red when you use me and gray when you throw me away.\n What am I ?? ",
            "I am heavy forward,but backward i am not.\n What am I ?? ",
            "I disappear everytime you say my name.\n What am I ?? ",
            "I have a face but no eyes, hands but no arms.\n What am I ?? ",
            "The more you take away from me, the bigger i get.\n What am I ?? ",
            "If you have me, you want to share me. Once you share me, you won't have me.\n What am I ?? ",
            "The faster you run, the harder it is to catch me.\n What am I ??",
            "I can fall off a building and live, but put me in water I will die. \nWhat am I ??",
            "I cannot be burned by fire or drowned in water.\n What am I ??",
            "I must be broken before you can use me.\n What am I ??",
            "I belong to you but others use me more often than you do.\n What am I ??",
            "Tear me off and scratch my head, what once red is now black.\n What am I ??",
            "I am full of holes but still holds water.\n What am I ??",
            "I start with an E, end with an E, and have a letter in me.\n What am I ??",
            "I get wet when drying. I get dirty when wiping.\n What am I ??",
            "I can be cracked, made, told, and played.\n What am I ??",
            "I like to twirl my body but keep my head up high. After I go in, everything becomes tight.\n What am I ??",
            "I can fly but I have no wings. I can cry but I have no eyes. Wherever I go, darkness follows me. \nWhat am I ??",
            "I am lighter than air but a hundred people cannot lift me. Careful, I am fragile. \nWhat am I ??",
            "Although I may have eyes, I cannot see. I have a round brown face with lots of acne.\n What am I ??",
            "I am gentle enough to soothe your skin, light enough to fly in the sky, strong enough to crack rocks.\n What am I ??",
            "Although glory but not at my best. Power will fall to me finally, when the man made me is dead.\n What am I ??",
            "I am the type of room you can not enter or leave. Raise from the ground below. I could be poisonous or a delicious treat.\n What am I ??",
            "The more you take of me, the more you leave behind.\n What am I ??",
            "I go up and never come down no matter how hard you wish. As I get higher, more wrinkles crawl on to the face.\n What am I ??",
            "I can be long or short. I can be grown or bought. I can be painted or left bare. My tip can be round or square.\n What am I ??",
            "I have a neck and no head, two arms but no hands. I’m with you to school, I’m with you to work.\n What am I ??",
            "I shrink smaller every time I take a bath.\n What am I ??",
            "I wear a green jacket on the outside, white jacket as a second layer, and red jacket inside. I am pregnant with a lot of babies.\n What am I ??",
            "I am good at concealing what’s real and hide what’s true. Sometime, I bring out the courage in you!\n What am I ??",
            "I grown from darkness but shine with a pale light. Very round I am, and always a lady’s delight.\n What am I ??",
            "I’m white; perfect for cutting & grinding. For most animals I am a useful tool.\n What am I ??",
            "I am always around you but often forgotten. I am pure and clean most time, but occasionally rotten.\n What am I ??",
            "I am the only thing that place today before yesterday.\n What am I ??",
            "I may only be asked but never bought. Sinners seek me but saints do not.\n What am I ??",
            "I jump when I walk and sit when I stand.\n What am I ??",
            "You can catch me but cannot throw me.\n What am I ??",
            "Taken from a mine and then locked up in a wooden case. Never released but used by students everyday.\n What am I ??",
            "I can never be stolen from you. I am owned by everyone. Some have more, some have less.\n What am I ??",
            "I am a mother from a family of eight. Spins around all day despite my weight. Had a ninth sibling before founding out its fake.\n What am I ??",
            "The more you have me the less you see. Shine a light on me and I flea.\n What am I ??",
            "I turn everything around, but I cannot move. When you see me you see you!\n What am I ??",
            "I dance on one feet and knows only one shape. Someone with same name as me is very good with directions.\n What am I ??",
            "Whoever made me don’t want me; Whoever bought me don’t need me. Whoever use me don’t know me.\n What am I ??",
            "When you take away the whole from me, there is always some left.\n What am I ??",
            "I am a word. I become longer when the third letter is removed.\n What am I ??",
            "Born in the ocean and white as snow. When I fall back to water I disappear without a trace.\n What am I ??",
            "As a state in America. I am round on both sides and high in the middle.\n What am I ??",
            "I am enjoyed by some, despised by others. Some take me for granted, some treasure me like a gift. I last forever, unless you break me first.\n What am I ??",
            "I am a king who’s good at measuring stuff.\n What am I ??",
            "I can wave my hands at you, but I never say goodbye. You are always cool when with me, even more so when I am high!\n What am I ??",
            "I start with “T”, ends with “T”, and within me is “T”.\n What am I ??",
            "My first half means container. Not a lot of people understand my language.\n What am I ??",
            "I walk on 4 legs in the start, 2 legs at middle, and 3 legs at end.\n What am I ??",
            "If a man carries my burden, they will get crushed to death. Though not rich, I leave silver in my track.\n What am I ??",
            "I have feathers that help me fly; with head and body but I’m not alive. Very skinny and a fixed length, how far I go depends on.\n What am I ??",
            "You heard me before, and then again. Afterward I die, until you call me again.\n What am I ??",
            "I am free the first time and second time, but the third time is going to cost you money.\n What am I ??",
            "I always run but never walk, often murmur but never talk, have a bed but never sleep, has a mouth but never eat.\n What am I ??",
            "Lighter than feather and softer than silk, yet the strongest man in the world cannot hold me for more than a few minutes.\n What am I ??",
            "Soft, hairy, from door to door. I am the pet that always stays on the floor.\n What am I ??",
            "I am quick when I’m thin. I am slow when I’m fat. Wind is my worst nightmare.\n What am I ??",
            "I have a head, tail, but no arms and legs.\n What am I ??",
            "People need me yet they give me away every day.\n What am I ??",
            "I am owned by the poor, the rich does not need me. If you eat me, you will die!\n What am I ??",
            "With a halo of water and a tongue of wood, stone as skin long I stood.\n What am I ??",
            "I have a ring but no fingers. I used to stay still all the time, but nowadays I follow you around.\n What am I ??",
            "I climb higher as I get hotter. I can never escape from my crystal cage.\n What am I ??",
            "What force and strength cannot get through, I with my unique teeth can do.\n What am I ??",
            "I am pronounced as one letter, written with three. I come in blue, black, brown, or grey. Reverse me and I read the same either way.\n What am I ??",
            "Kings and queens may cling to power, and the jesters may have their call. I am the most common but I can rule them all.\n What am I ??",
            "I may be made of metal, bone, or wood and have many teeth. My bite hurts no one and the ladies love me.\n What am I ??",
            "I can’t go left, I can’t go right. I am forever stuck in a building over three stories high.\n What am I ??",
            "I am an instrument capable of making numerous sound but cannot be touched or seen.\n What am I ??",
            "I run around the streets all day. Under the bed or by the door I sit at night, never alone. My tongue hangs out, waiting to be fed during the day.\n What am I ??",
            "I have no voice but I can teach you all there is to know. I have spines and hinges but I am not a door. Once I’ve told you all, I cannot tell you more.\n What am I ??",
            "I never was but always will be. No one ever saw me but everyone knows I exist. I give people the motivation to better themselves everyday.\n What am I ??",
            "I am a sharp looking horse with a flaxen tail. The longer I run the shorter my tail becomes.\n What am I ??",
            "I am rarely touched but often held, and if you are smart you’ll use me well.\n What am I ??",
            "I am an insect, half of my name is another insect. I am similar to the name of a famous band.\n What am I ??",
            "I wiggle and can not see,sometime underground and sometime on a tree. I really dont want to be on a hook,and i become a person when combined with book.\nWhat am I ??",
            "A necessity to some, a treasure to many, I’m best enjoyed among pleasant company. Some like me hot, some like me cold. Some prefer mild, some like me bold.\n What am I ??",
            "I go from house to house, a messenger small and tight. Weather it rains or snows. I sleep outside at night.\n What am I ??",
            "I am a time for gathering crops. Remove the first three letters I become an object you can wear.\n What am I ??",
            "I have cities with no people, forests with no trees, and oceans with no water.\n What am I ??",
            "I can the same written do forward, backward, and upside down.\n What am I ??Noon",
            "I sleep when you are awake, I am awake when you fall asleep. I can fly but no feathers to aid my flight.\n What am I ??",
            "I can be red, blue, purple, and green. No one can reach me, not even the queen.\n What am I ??",
            "I have six faces but not even one body connected, 21 eyes in total but cannot see.\n What am I ??",
            "I have a head and a tail but my head can never turn to my tail.\n What am I ??",
            "I come without being fetched at night, hides away as soon as daylight strikes. Although I may look small, I am much mightier than what you can imagine.\n What am I ??",
            "I do not contain flesh, feather, or scales; yet I have fingers and thumbs.\n What am I ??",
            "I am all around you, but you cannot see me. I have no throat, but you can hear me. Valued during summer but despised in the winter.\n What am I ??",
            "I contain six letters, minus one and you got twelve.\n What am I ??",
            "Sometimes I fly as fast as the speed of light. Sometime I crawl as slow as a snail. Unknown until I am measured but you will certainly miss me when I’m gone.\n What am I ??",
            "I have roots nobody sees. I am taller than trees. Up, up I go but I never grow.\n What am I ??",
            "I do not have eyes but I once could see. I used to have thoughts but now I’m empty.\n What am I ??",
            "I was not born, but I am here. I have no name, but I am given many. I was made by science and life.\n What am I ??",
            "I hold two meanings. With one I may be broken, with the other I hold on.\n What am I ??",
            "I go in hard, come out soft. Blow me hard and I’ll make a pop.\n What am I ??",
            "I am a food with 5 letters. If you remove the first letter I am a form of energy. Remove two and I’m needed to live. Scramble the last 3 and you can drink me down.\n What am I ??",
            "I have a hundred legs, but cannot stand. I have a long neck, but no head. I cannot see, and I help keep your house neat and tidy.\n What am I ??",
            "You use a knife to slice my head and weep beside me when I am dead.\n What am I ??",
            "I am a fruit with seed on the outside.\n What am I ??",
            "You throw me out from the ship when you use me and take me in when you are done.\n What am I ??",
            "I have 24 keys but cannot open any locks. Sometimes loud, sometimes soft.\n What am I ??",
            "I am clean when I’m black, dirty when I’m white.\n What am I ??",
            "My first two letters say my name. My last letter asks a question. What I embrace I destroy.\n What am I ??",
            "I have three heads. Cut off one, I become strong animal. Cut off two, I become ten.\n What am I ??",
            "I contain five little items of an everyday sort. You can find all five in 'a tennis court'.\n What am I ??",
            "I can sizzle like bacon. I am made with an egg, I have plenty of backbone, but lack a good leg. I peel layers like onions, but still remain whole. I can be long, like a flagpole, yet fit in a hole.\n What am I ??",
            "I can swim but never get wet. I can run but never get tired. I follow you everywhere but never say a word.\n What am I ??",
            "I am the beginning of the end, and the end of time and space. I am essential to creation, and I surround every place.\n What am I ??",
            "You can throw me away, but I will always be coming back.\n What am I ??",
            "I am easy to waste and unstoppable.\n What am I ??",
            "I am one small little piece of paper, yet sometimes hold lots of value. I am all you need to get in to big events, but will cost you. I am an important part of travel. And if lost, you’re not coming.\n What am I ??",
            "A hole in a pole. Though I fill a hole in white, I’m used more by the day and less by the night.\n What am I ??",
            "I have been the beginning of ideas for all time, yet I am just one simple small object, the things that you can use me for can be frustrating and also I can be pretty. I have some of the most valuable thing in the world on me, yet almost everyone owns me. With me you can make anything.\n What am I ??",
            "The thunder comes before the lightning, and the lightning comes before the cloud, The rain dries all the land it touches, wrapping the earth in a blood red shroud.\n What am I ??",
            "I may seem real but it always turns out I was never there in the first place… you only see me during a certain resting stage.\n What am I ??",
            "The 8 of us go forth and can never come back, to protect our king from a foes attack.\n What am I ??",
            "I can speak with my hard metal tongue. But I cannot breathe, for I have no lung.\n What am I ??",
            "I shoot but never kills.\n What am I ??",
            "Take away my first letter, take away my second letter, take away all my letters and I would remain the same.\n What am I ??",
            "I am essential to life on earth! I am split into thirds. Two thirds are the same. One of the thirds is 8. The other two are 1 each.\n What am I ??",
            "I can be winding and I can be straight. I can be smooth and I can be rough. Sometimes both. I start out black but fade to brown the more I am used. My favorite colors are yellow and white, and I love stripes and dashed lines.\n What am I ??",
            "I am gold and can be black and white, I’m a symbol for a nation, when freedom took flight.\n What am I ??",
            "I am a portal to another world which you cannot enter. Only you can see me but I can’t see you.\n What am I ??",
            "Sometimes dark and sometimes bright, I make my way among twinkling lights. Seas and oceans obey my call, yet mountains I cannot move at all. My face is marred and gray, but I’m majestic anyway.\n What am I ??",
            "I have married many woman but has never been married.\n What am I ??",
            "Whoever makes it, tells it not. Whoever takes it, knows it not. Whoever knows it, wants it not.\n What am I ??",
            "I move without wings, between silken strings. I leave as you find, my substance behind.\n What am I ??",
            "Against the sun, I protect your eyes and i am often worn on the face of spies.\n What am I ??",
            "I can be found in water but never wet.\n What am I ??",
            "My name is an object which is used in baseball, during the day I can’t be seen at all.\n What am I ??",
            "I’m as simple as a circle, worthless as a leader; but when I follow a group, their strength increases tenfold. By myself I’m practically nothing.\n What am I ??",
            "My first part compliments people. My second part makes things known. My third part hurts feelings. My fourth part may hold a previous treasure. My fifth part is used when sharing fancy beverages.\n What am I ??",
            "At the end I’m sweet,but i give people a huge fright. I normally get celebrate at night.\n What am I ??",
            "In my life I must bare, my bloodline I must share.\n What am I ??",
            "I am always in front of you, but you will never see me.\n What am I ??",
            "To cross the water I’m the way. For water I’m above I do not touch it ,i neither swim nor move is a truth to say.\n What am I ??",
            "I can be repeated but rarely in the same way. I can’t be changed but can be rewritten. I can be passed down, but should not be forgotten.\n What am I ??",
            "I am a shimmering field that reaches far. Yet I have no tracks, and am crossed without paths.\n What am I ??",
            "My teeth are sharp, my back is straight, to cut things up it is my fate.\n What am I ??",
            "My tail is long, my coat is brown, I like the country, I like the town. I can live in a house or live in a shed, And I come out to play when you are in bed.\n What am I ??",
            "I march before armies, a thousand salute me. My fall can bring victory, but no one would shoot me. The wind is my lover, one-legged am I. Name me and see me at home in the sky.\n What am I ??",
            "Turns me on my backs, and open up my stomach. You will be the wisest of men though at start a lummox.\n What am I ??",
            "I am all around, yet to me you are half blind. Sunlight makes me invisible, and difficult to find.\n What am I ??",
            "My first is in window but not in pane. My second’s in road but not in lane. My third is in oval but not in round. My fourth is in hearing but not in sound. My whole is known as a sign of peace. And from noah’s ark won quick release.\n What am I ??",
            "The more of me there is, the less you see.\n What am I ??",
            "By Moon or by Sun, I shall be found. Yet I am undone, if there’s no light around.\n What am I ??",
            "Some try to hide, some try to cheat. But time will show, we always will meet. Try as you might, to guess my name. I promise you’ll know, when I do claim.\n What am I ??",
            "Brown I am and much admired; many horses have I tried; tire a horse and worry a man; tell me this riddle if you can.\n What am I ??",
            "I row quickly with four oars, but never comes out from under my own roof.\n What am I ??",
            "I look at you, you look at me, I raise my right, you raise your left.\n What am I ??",
            "I am a path between high natural masses; remove the first letter to get a path between man-made masses.\n What am I ??",
            "I have hands but cannot clap.\n What am I ??",
            "Though desert men once called me God, today men call me mad. For I wag my tail when I am angry. And growl when I am glad.\n What am I ??",
            "I am made by nature, soft as silk; A puffy cloud, white as milk; Snow tops this tropical crop; The dirtiest part of a mop.\n What am I ??",
            "I can go through glass without breaking it.\n What am I ??",
            "In many hall ways you would stand, if not with me in hand.\n What am I ??",
            "If you’re looking for something sweet I know what to do. But if you don’t like heat I’m not for you.\n What am I ??",
            "I am the word that has three syllables and twenty six letters.\n What am I ??",
            "I am two-faced but bear only one. I have no legs but travel widely. Men spill much blood over me. Kings leave their imprint on me. I have greatest power when given away, yet lust for me keeps me locked away.\n What am I ??",
            "I fly forever, Rest never.\n What am I ??",
            "I am the building with the most number of stories.\n What am I ??",
            "Part carbon, part water, I am poison to the fishes. Many falsely claim my name, I am the pause that refreshes.\n What am I ??",
            "I’m tall, I’m round and hollow, Seems to get chewed a bit, But you’ll rarely see me unless the other end is lit.\n What am I ??",
            "My days are numbered.\n What am I ??",
            "I’m many people’s favorite place, even though many don’t remember their stay. You’ll love to come but hate to leave, if you get cold use my sleeves.\n What am I ??",
            "I’m very tempting, so its said, I have a shiny coat of red, and my flesh is white beneath. I smell so sweet, taste good to eat, and help to guard your teeth.\n What am I ??",
            "I hatch without food.\n What am I ??",
            "When I get closer my tail grows longer, but when I go away my tail leads the way.\n What am I ??",
            "When young, I am sweet in the sun. When middle-aged, I make you gay. When old, I am valued more than ever.\n What am I ??",
            "I go through a door but never go in, and never come out.\n What am I ??",
            "I cut through evil like a double edged sword, and chaos flees at my approach. Balance I single-handedly upraise, through battles fought with heart and mind, instead of with my gaze.\n What am I ??",
            "I’m not a bird, but I can fly through the sky. I’m not a river, but I’m full of water.\n What am I ??",
            "I work when I play and play when I work.\n What am I ??",
            "An open ended barrel, I am shaped like a hive. I am filled with the flesh, and the flesh is alive.\n What am I ??",
            "First I may be your servant’s name; then your desires I may proclaim; And, when your mortal life is over hold all your wealth within my power.\n What am I ??",
            "I am perfect with a head, perfect without a head; Perfect with a tail, perfect without a tail; Perfect with either, neither, or both.\n What am I ??",
            "I am a precious stone, as clear as diamond. Seek me out while the sun’s near the horizon. Though you can walk on water with my power, try to keep me, and I’ll vanish in an hour.\n What am I ??",
            "Lovely and round, I shine with pale light, grown in the darkness, a lady’s delight.\n What am I ??",
            "I come with a train, and go with a train, and the train doesn’t need me, but can’t go without me.\n What am I ??",
            "You seek me out, when your hunger’s ripe. I sit on four legs, and smoke a pipe.\n What am I ??",
            "I am a ball that can be rolled, but never bounced or thrown.\n What am I ??",
            "I can be hot, I can be cold, I can run and I can be still, I can be hard and I can be soft.\n What am I ??",
            "I can be any shape, I can a be surprise to your loved one, just fill me up and make sure to hold me real tight so I won’t fly away.\n What am I ??",
            "Fill me up with hot or cold, put anything in me I’ll make sure I’ll hold.\n What is it ??",
            "White and sparkly I can be, fluffy and soft, kids make angels out of me.\n What am I ??",
            "I have some branches, but I am without a trunk, leaves or fruit. What am I?\n",
            "They provide for you, they keep you safe, we celebrate them every June.\n What are they ??",
            "I have keys but no doors, I have space but no rooms, I allow you to enter but you are never able to leave.\n What am I ??",
            "I have wings, I am able to fly, I‘m not a bird yet I soar high in the sky.\n What am I ??",
            "I go up and I go down, I am blazing and hot. If you look right at me, your eyes will wish you had not.\n What am I ??",
            "I am the third child of the eight, I am also a mother, and it takes me 365.256 days to make a full circle.\n What am I ??",
            "I have wings and I have a tail, across the sky is where I sail. Yet I have no eyes, ears or mouth, and I bob randomly from north to south.\n What am I ??",
            "I come as a gas and I get in line with neon. I am also known as the home of superman.\n What am I ??",
            "I can be used to build castles,but I crumble in your hands.I can help a man see and am found all around the lands. What am I ??",
            "I go up but at the same time go down. Up towards the blue sky, and down towards the ground. I'm present tense and past tense too, so why don't you come for a ride, just me and you! What am I ??",
            "Pronounced as one letter,But written with three,Two letters there are,And only two in me.I'm double, I'm single,I'm black, blue, and gray,I'm read from both ends,And the same either way. What am I ??",
            "I am greater than God and more evil than the devil himself. The poor have me, the rich need me and if you ever eat me you'll die. What am I ??",
            "Until I am measured, I am not known,Yet you miss me so much When I have flown. What am I ??",
            "You hear me once, you hear me again,then i die until you call again. What am I ??",
            "I am 5 letter word, If all the 5 letters are available I am a talent in you, If you remove my first letter I will die, if you remove my first 2 letters I will be sick.\n What am I ??",
    };


    public static String[] ansList = new String[]{
            "FIRE",
            "PEA",
            "CHARCOAL",
            "TON",
            "SILENCE",
            "CLOCK",
            "HOLE",
            "SECRET",
            "BREATH",
            "PAPER",
            "ICE",
            "EGG",
            "NAME",
            "MATCH",
            "SPONGE",
            "ENVELOPE",
            "TOWEL",
            "JOKE",
            "SCREW",
            "CLOUD",
            "BUBBLE",
            "POTATO",
            "WATER",
            "PRINCE",
            "MUSHROOM",
            "FOOTSTEP",
            "AGE",
            "FINGERNAIL",
            "SHIRT",
            "SOAP",
            "WATERMELON",
            "MAKEUP",
            "PEARL",
            "TEETH",
            "AIR",
            "DICTIONARY",
            "APOLOGY",
            "KANGAROO",
            "COLD",
            "LEAD",
            "KNOWLEDGE",
            "EARTH",
            "DARKNESS",
            "MIRROR",
            "COMPASS",
            "COFFIN",
            "WHOLESOME",
            "LOUNGER",
            "SALT",
            "OHIO",
            "MARRIAGE",
            "RULER",
            "FAN",
            "TEAPOT",
            "BINARY",
            "HUMAN",
            "SNAIL",
            "ARROW",
            "ECHO",
            "TEETH",
            "RIVER",
            "BREATH",
            "CARPET",
            "CANDLE",
            "COIN",
            "MONEY",
            "NOTHING",
            "CASTLE",
            "TELEPHONE",
            "MERCURY",
            "KEY",
            "EYE",
            "ACE",
            "COMB",
            "ELEVATOR",
            "VOICE",
            "SHOE",
            "BOOK",
            "TOMORROW",
            "NEEDLE",
            "TONGUE",
            "BEETLE",
            "WORM",
            "COFFEE",
            "ROAD",
            "HARVEST",
            "MAP",
            "NOON",
            "BAT",
            "RAINBOW",
            "DICE",
            "COIN",
            "STAR",
            "GLOVE",
            "WIND",
            "DOZENS",
            "TIME",
            "MOUNTAIN",
            "SKULL",
            "CLONE",
            "TIE",
            "GUM",
            "WHEAT",
            "BROOM",
            "ONION",
            "STRAWBERRY",
            "ANCHOR",
            "MUSIC",
            "CHALKBOARD",
            "IVY",
            "FOX",
            "VOWELS",
            "SNAKE",
            "SHADOW",
            "E",
            "BOOMERANG",
            "TIME",
            "TICKET",
            "EYE",
            "PAPER",
            "VOLCANO",
            "DREAM",
            "PAWNS",
            "BELL",
            "CAMERA",
            "POSTMAN",
            "WATER",
            "ROAD",
            "EAGLE",
            "TELEVISION",
            "MOON",
            "PRIEST",
            "POISON",
            "SPIDER",
            "SUNGLASS",
            "REFLECTION",
            "BAT",
            "ZERO",
            "PALM",
            "HALLOWEEN",
            "SIBLING",
            "FUTURE",
            "BRIDGE",
            "HISTORY",
            "OCEAN",
            "SAW",
            "MOUSE",
            "FLAG",
            "BOOK",
            "STAR",
            "DOVE",
            "DARKNESS",
            "SHADOW",
            "DEATH",
            "SADDLE",
            "TURTLE",
            "MIRROR",
            "VALLEY",
            "CLOCK",
            "CAT",
            "COTTON",
            "LIGHT",
            "KEY",
            "KITCHEN",
            "ALPHABETS",
            "COIN",
            "WIND",
            "LIBRARY",
            "SODA",
            "PIPE",
            "CALENDER",
            "BED",
            "APPLE",
            "HUNGER",
            "COMET",
            "WINE",
            "KEYHOLE",
            "JUSTICE",
            "CLOUD",
            "MUSICIAN",
            "THIMBLE",
            "WILL",
            "WIG",
            "ICE",
            "PEAR",
            "NOISE",
            "STOVE",
            "EYEBALL",
            "WATER",
            "BALLOON",
            "CUP",
            "SNOW",
            "BANK",
            "DADS",
            "KEYBOARD",
            "AIRPLANE",
            "SUN",
            "EARTH",
            "KITE",
            "KRYPTON",
            "SAND",
            "SEESAW",
            "EYE",
            "NOTHING",
            "TIME",
            "ECHO",
            "SKILL",


    };

    public void watchVideo() {
        load.setVisibility(View.GONE);
        AdColony.requestInterstitial(ZONE_ID, listener, options);
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
               playVideo.setEnabled(true);
               ad=adColonyInterstitial;
            }

        };
        dialog.show();
    }
}
