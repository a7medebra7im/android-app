package com.example.fakhrelarab;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class login extends AppCompatActivity {


    private EditText emil, password;
    private Button login;
    private FirebaseAuth Athu;
    private ProgressBar log_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Athu = FirebaseAuth.getInstance();


        emil = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.pass_id);
        login = (Button) findViewById(R.id.login_id);
        log_progress = (ProgressBar) findViewById(R.id.login_progress_id);

    }

    public void login(View view) {



            String m = emil.getText().toString();
            String s = password.getText().toString();

            if (TextUtils.isEmpty(m) || TextUtils.isEmpty(s)) {

                Toast.makeText(this,"الحقل فارغ...",Toast.LENGTH_SHORT).show();

            }else
            {
                ConnectivityManager c = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = c.getActiveNetworkInfo();

                if(info!=null&&info.isConnected())
                {log_progress.setVisibility(View.VISIBLE);

                    Athu.signInWithEmailAndPassword(m, s).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                log_progress.setVisibility(View.INVISIBLE);

                                Intent i = new Intent(login.this, Control.class);
                                startActivity(i);

                            } else if (!task.isSuccessful()) {
                                log_progress.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "الايميل او الباسورد خاطئ ", Toast.LENGTH_LONG).show();
                            }

                        }
                    });} else
                    Toast.makeText(this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();


            }





    }









    }

























