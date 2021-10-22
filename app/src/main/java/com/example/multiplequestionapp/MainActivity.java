package com.example.multiplequestionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnStart, logOut;
    TextView txtWlc, txtHighScore;
    Spinner spinnerDifficulty;
    Spinner spinnerCategory;
    //to later identify from what acticity we get our result back but here we have only 1 activity
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    //save our high score in shared pref
    public static final String SHARED_PREF = "sharedpref" ;
    public static final String KEY_HIGHSCORE = "keyHighScore";

    private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.buttonStart);
        logOut= findViewById(R.id.logOut);
        txtWlc = findViewById(R.id.textViewWelcome);
        txtHighScore = findViewById(R.id.textViewHighScore);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCategory = findViewById(R.id.spiner_category);

        String wlcTxt = txtWlc.getText().toString();

        SharedPreferences save = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(save.getBoolean("save", false))
        {
            txtWlc.setText(wlcTxt + "" + save.getString("name",""));
        }

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            txtWlc.setText(wlcTxt + b.getString("username"));
        }


        loadCategories();
        loadDifficultyLevels();
        loadHighScore();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences saveLog = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveLog.edit();
                editor.putBoolean("save",false);
                editor.apply();

                Intent logout= new Intent(getApplicationContext(), Welcome_To_Fragment.class);
                startActivity(logout);
            }
        });


    }

    private void startQuiz() {
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        String difficulty = spinnerDifficulty.getSelectedItem().toString(); //get whatever item the users selects

        Intent intent = new Intent(MainActivity.this , Quiz.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if we have multiple activities well have many request code
        if(requestCode == REQUEST_CODE_QUIZ)
            if(resultCode == RESULT_OK)
            {
                int score = data.getIntExtra(Quiz.NEW_SCORE, 0);
                if(score > highscore)
                {
                    updateHighScore(score);
                }
            }
    }

    private void loadCategories()
    {
        SQLDBHelper dbHelper = SQLDBHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<Category>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);//this will put our categories in spinner
    }

    private void loadDifficultyLevels()
    {
        //we want to fill our spinner with 3 difficulty levels so thats why we created static method getAllDifficultylevels
        //pass string array to our spinner
        String [] difficultyLevels = Questions.getAllDifficultyLevels();//static method


        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//make dropdown menu better looking
        spinnerDifficulty.setAdapter((adapterDifficulty));

    }
    private void loadHighScore()
    {
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        highscore = pref.getInt(KEY_HIGHSCORE, 0);
        txtHighScore.setText("HighScore: " + highscore);
    }

    private void updateHighScore(int highScoreNew) {
        highscore = highScoreNew;
        txtHighScore.setText("HighScore: " + highscore);

        SharedPreferences pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}