package com.example.multiplequestionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SignIn_Frag extends Fragment {


    EditText emailText,passwordText;
    Button btnLogin;
    ArrayList<Users> lst;;
    SQLDBHelper db;
    CheckBox saveLogin, showPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in_, container, false);

        emailText = v.findViewById(R.id.TextEmailAddress);
        passwordText = v.findViewById(R.id.TextPassword);
        btnLogin = v.findViewById(R.id.buttonLogin);
        saveLogin= v.findViewById(R.id.checkBox);
        showPass= v.findViewById(R.id.checkBox2);

        db = SQLDBHelper.getInstance(getContext());
        lst = db.getAllUsers();

        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b)
                {
                    passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else
                {
                    passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(!username.equals("") && !password.equals(""))
                {
                    if(lst.isEmpty())
                    {
                        Toast.makeText(getContext(),"User does not exit!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        boolean userFound = false;
                        for(int i=0;i<lst.size();++i)
                        {
                            Users u = (Users) lst.get(i);
                            if(u.getUsername().equals(username) && u.getPassword().equals(password))
                            {
                                Intent intent = new Intent(getContext(),MainActivity.class);
                                intent.putExtra("username",username);
                                startActivity(intent);
                                Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                                userFound = true;
                                break;
                            }
                        }

                        if(!userFound)
                        {
                            Toast.makeText(getContext(), "User Not Found!", Toast.LENGTH_SHORT).show();
                        }

                    }


                }else
                {
                    Toast.makeText(getContext(),"All fields * are required",Toast.LENGTH_LONG).show();
                }


                //Save Login
                SharedPreferences saveLog = v.getContext().getSharedPreferences("login", v.getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = saveLog.edit();
                editor.putBoolean("save",false);
                editor.putString("name", emailText.getText().toString());
                editor.apply();

                if (saveLogin.isChecked()){
                    editor.putBoolean("save",true);
                    editor.apply();
                }

            }
        });
        return v;
    }
//    public boolean checkUsers(Users users)
//    {
//        lst = db.getAllUsers();
//        Boolean result = false;
//
//        for(int i=0;i<lst.size();++i)
//        {
//            Users u = (Users)lst.get(i);
//            if(u.getUsername().equalsIgnoreCase(u.getUsername()))
//            {
//                result = true;
//                break;
//            }
//        }
//        return result;
//    }
}