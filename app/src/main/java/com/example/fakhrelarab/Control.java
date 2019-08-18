package com.example.fakhrelarab;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;


public class Control extends AppCompatActivity {

    private EditText titel, news;
    private Button post_btn;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Uri Img_Uri = null;
    private String url;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        imageView = findViewById(R.id.imageView_id);
        progressBar = findViewById(R.id.progressBar_id);
        titel = findViewById(R.id.titel_id);
        news = findViewById(R.id.news_id);
        post_btn = findViewById(R.id.button);
        post_btn.setVisibility(View.VISIBLE);


    }


    public void img_click(View view) {

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Img_Uri = data.getData();
            imageView.setImageURI(Img_Uri);

        } else {
            Toast.makeText(this, "no image selected", Toast.LENGTH_LONG).show();
        }


    }

    public void post_btn(View view) {

        final String tit = titel.getText().toString();
        final String ne = news.getText().toString();

        if (TextUtils.isEmpty(tit) || TextUtils.isEmpty(ne) || Img_Uri == null) {
            Toast.makeText(this, "Field empty", Toast.LENGTH_SHORT).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            post_btn.setVisibility(View.INVISIBLE);

            String randomname = FieldValue.serverTimestamp().toString();
            final StorageReference fielpath = storageReference.child("post_images").child(randomname + ".jpg");

            fielpath.putFile(Img_Uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    fielpath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            url = uri.toString();

                            if (url == null) {
                                Toast.makeText(getApplicationContext(), "url null", Toast.LENGTH_SHORT).show();
                            } else {

                                Map<String, Object> newsmap = new HashMap<>();
                                newsmap.put("image_uri", url);
                                newsmap.put("title", tit);
                                newsmap.put("news", ne);
                                newsmap.put("timestamp", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("news").add(newsmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "تم نشر الخبر بنجاح", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Control.this,MainActivity.class));
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "فشل", Toast.LENGTH_LONG).show();
                                            post_btn.setVisibility(View.VISIBLE);

                                        }

                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                        }
                    });


                }
            });


        }

    }


    public void go(View view) {

        startActivity(new Intent(Control.this, MainActivity.class));
    }
}

