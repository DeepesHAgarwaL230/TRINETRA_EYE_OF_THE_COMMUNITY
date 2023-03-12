package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    Button registerbtn;

    Button loginbtn;

    EditText emailfield, passwordfield;
    EditText age,gender,contact;
    ImageView logoIV;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        All types of textfields
        emailfield = findViewById(R.id.username);
        passwordfield = findViewById(R.id.password1);
        age = findViewById(R.id.userAge);
        gender = findViewById(R.id.Gender);
        contact = findViewById(R.id.contactnumber);

//        Login & Signup buttons
        registerbtn = findViewById(R.id.sign);
        loginbtn = findViewById(R.id.login);

//        ImageView for the logo
        logoIV = findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        // Set on Click Listener on Registration button
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        logoIV.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }
        private void registerNewUser(){
            // show the visibility of progress bar to show loading
//            progressbar.setVisibility(View.VISIBLE);

            // Take the value of two edit texts in Strings
            String email, password;
            String userage, contactno,usergender;
            email = emailfield.getText().toString();
            password = passwordfield.getText().toString();
            userage = age.getText().toString();
            usergender = gender.getText().toString();
            contactno = contact.getText().toString();

            // Validations for input email and password
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(RegistrationActivity.this,
                                "Please fill all the details!!", Toast.LENGTH_SHORT).show();
                return;
            }

            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()) {
                                String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                User user = new User(email,password,userage,usergender,contactno);

                                mDatabase.child(userId).setValue(user);

                                Toast.makeText(getApplicationContext(),
                                                "Registration successful!", Toast.LENGTH_LONG).show();

                                // if the user created intent to login activity
                                Intent intent
                                        = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                // Registration failed
                                Toast.makeText(
                                                getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    public class User {
        public String email;
        public String password;
        public String gender;
        public String age;
        public String contactNo;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String password,String age,String contactNo, String gender) {
            this.email = email;
            this.password = password;
            this.age = age;
            this.gender = gender;
            this.contactNo = contactNo;
        }
    }

}

