package com.example.multiplequestionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome_To_Fragment extends AppCompatActivity {

    Button signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_to_fragment);
        signUp = (Button) findViewById(R.id.button1);
        signIn = (Button) findViewById(R.id.button2);

        //save login is checked
        SharedPreferences saveLog = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (saveLog.contains("save")){
            if (saveLog.getBoolean("save", false)){
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment first = new SignIn_Frag();
                replaceFragment(first);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment second = new SignUp_Frag();
                replaceFragment(second);
            }
        });

    }
    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout3, destFragment);
        fragmentTransaction.commit();
    }
}