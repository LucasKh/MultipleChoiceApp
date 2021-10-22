package com.example.multiplequestionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Quiz extends AppCompatActivity {
    //access this in our main
    public static final String NEW_SCORE = "NewScore";
    private  static final long COUNTDOWN_IN_MILLIS = 30000;//30 seconds for each question

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    TextView textViewDifficulty;
    TextView textViewCategory;

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private Button buttonConfirmNext;

    //change the color of rb after we answer and change it back to its default color
    private ColorStateList textColorDefaultRb;//stands for color of rb save the default color of rb; soon we will change it
    //change text color of timer if we get below 10second and then change back to its default color
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDowntimer;
    private long timeLeftInMillis;//save the remaining time in millis for our timer

    private ArrayList<Questions> questionList;

    private int questionCounter;// count how many questions we have shown
    private int questionCountTotal;//total questions in our array list
    private Questions currentQuestion;// save the current question that we want to show in a private question

    private int score;
    private boolean answered;//lock the answer if the question wasn't answered yet or show the question if its already answered

    int backPress = 0;
    private long backPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.textViewQusetion);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestionCount = findViewById(R.id.textViewQuestionCount);
        textViewCountDown = findViewById(R.id.textViewTimer);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewCategory = findViewById(R.id.textViewCategory);

        rbGroup = findViewById(R.id.radioGrp);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);

        buttonConfirmNext = findViewById(R.id.buttonConfirm);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();//save our default color of timer

        Intent i = getIntent();
        int categoryID = i.getIntExtra(MainActivity.EXTRA_CATEGORY_ID,0 );
        String categoryName = i.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String difficulty = i.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewDifficulty.setText("Difficulty: " + difficulty);
        textViewCategory.setText("Category: " + categoryName);

        SQLDBHelper dbHelper = SQLDBHelper.getInstance(this);

        questionList = dbHelper.getQuestions(categoryID, difficulty);
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!answered)
                {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked())
                    {
                        checkAnswer();
                    }
                    else {
                        Toast.makeText(Quiz.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    showNextQuestion();
                }
            }
        });
    }
    private void showNextQuestion()
    {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());

            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;//30 =30
            startCountDown();
        } else
            {
            finishQuiz();
        }
    }
    private void startCountDown()
    {
        countDowntimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            //OnTick method will be called every 1000ms or every sec
            public void onTick(long millisUntilFinished)
            {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish()
            {
                //it display 1 sec even after its finsihed so we set timeleft to 0 explicitly
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();//automatically start this count down timer after creating it
    }
    private void updateCountDownText()
    {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        //we want the seconds that are left after subtracting our minutes
        int seconds = (int) (timeLeftInMillis / 1000) % 60;//get what is left after dividing 60 calculating our minutes
        //we want to turn these values into as tring and pass it to our textviews
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);//so this method will be called every time our OnTick method is called which is once per second

        if(timeLeftInMillis < 10000)
        {
            textViewCountDown.setTextColor(Color.RED);
        }else
        {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }

    }
    private void checkAnswer()
    {
        answered = true;

        countDowntimer.cancel();//stop our timer if we choose an answer

        //return the id of whichever radio button was checked
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;//return index of rb that was selected in our rgrp
        if (answerNr == currentQuestion.getAnswerNr())
        {
            score++;
            textViewScore.setText("Score: " + score);
        }
        showSolution();
    }
    private void showSolution()
    {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }
    private void finishQuiz() {
        //send result to our main
        Intent result = new Intent();
        result.putExtra(NEW_SCORE, score);
        //compare this result ct if we succesfully finished our quiz
        setResult(RESULT_OK, result);
        finish();

    }
    @Override
    public void onBackPressed()
    {
        //so the first time we click our backpress button this part will not be called bcz backpress time is 0 and system is much bigger
        if(backPressed + 2000 > System.currentTimeMillis())
        {
            finishQuiz();
        }else //if more than 2 seconds passed since the last time we pessed our back button
        {
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

//        backPress = (backPress + 1);
//        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
//
//        if (backPress>1)
//        {
//            this.finish();
//        }
    }
    //we need to cancel our countdown timer after we finish our activity otherwise it will stay running in the background
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDowntimer != null)
        {
            countDowntimer.cancel();//so when our activity closes our downtimer cancels
        }
    }
}