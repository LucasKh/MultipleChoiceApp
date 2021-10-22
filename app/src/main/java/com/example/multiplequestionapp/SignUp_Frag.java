package com.example.multiplequestionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SignUp_Frag extends Fragment {

    EditText emailTxt,passwordTxt,confPasswordTxt;
    Button btnRegister;
    ArrayList<Users> lst;;
    SQLDBHelper db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up_, container, false);

        emailTxt = v.findViewById(R.id.EmailAddressTxt);
        passwordTxt = v.findViewById(R.id.PasswordTxt);
        confPasswordTxt = v.findViewById(R.id.ConfPasswordTxt);
        btnRegister = v.findViewById(R.id.buttonRegister);

        db = SQLDBHelper.getInstance(getContext());


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usern = emailTxt.getText().toString();
                String pass = passwordTxt.getText().toString();
                String confPass = confPasswordTxt.getText().toString();

                Users user = new Users();
                user.setUsername(usern);
                user.setPassword(pass);
                user.setConfPassword(confPass);
                if(!user.equals("") && !pass.equals("") && !confPass.equals(""))
                {
                    if(pass.equals(confPass))
                    {
                        if(!checkUser(user))
                        {
                            db.insertUser(user);
                            SignIn_Frag signIn = new SignIn_Frag();
                            getFragmentManager().beginTransaction().replace(R.id.frameLayout3,signIn).commit();
                        }
                        else
                        {
                            Toast.makeText(getContext(),"User Already exits!",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Password should be confirmed correctly !",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getContext(),"All fields * are required",Toast.LENGTH_LONG).show();
                }

            }

        });


        return v;
    }
    private boolean checkUser(Users username)
    {
        lst = db.getAllUsers();
        boolean result = false;

        for (int i = 0; i < lst.size(); i++)
        {
            Users u = (Users) lst.get(i);

            if (username.equals(u.getUsername()))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    private void finish()
    {
        finish();
    }
}