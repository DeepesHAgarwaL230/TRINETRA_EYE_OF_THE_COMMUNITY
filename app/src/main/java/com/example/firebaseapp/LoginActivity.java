package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText user_name, pass_word;
    private Button btn_login,btn_sign;
    ImageView logoIV;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user_name=findViewById(R.id.email);
        pass_word=findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_sign = findViewById(R.id.btn_signup);
        logoIV = findViewById(R.id.imageView2);


//      Firebase authentication (getting the instance)
        mAuth=FirebaseAuth.getInstance();

//        When user clicks on the logo then page will be refreshed.
        logoIV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


//      Adding functionality when the user clicks on the Login button.
        btn_login.setOnClickListener(v -> {

            String email= user_name.getText().toString().trim();
            String password=pass_word.getText().toString().trim();

//          Email and password validation
            if(email.isEmpty())
            {
                user_name.setError("Email is empty");
                user_name.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                user_name.setError("Enter the valid email");
                user_name.requestFocus();
                return;
            }
            if(password.isEmpty())
            {
                pass_word.setError("Password is empty");
                pass_word.requestFocus();
                return;
            }
            if(password.length()<6)
            {
                pass_word.setError("Length of password is more than 6");
                pass_word.requestFocus();
                return;
            }

//            Functionality of signing in with email and password
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(LoginActivity.this, after_login.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,
                            "Please Check Your login Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            });
        });

//        When user clicks on the signup button then he/she will be redirected to registration page.
        btn_sign.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class )));
    }

}