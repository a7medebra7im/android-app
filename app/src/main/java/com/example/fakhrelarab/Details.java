package com.example.fakhrelarab;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;

public class Details extends AppCompatActivity {

    private ImageView image;
    private TextView titel;
    private TextView news;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        image = findViewById(R.id.imageView_id);
        titel = findViewById(R.id.titel_id);
        news = findViewById(R.id.news_id);

        Intent i = getIntent();
        String img = i.getExtras().get("i").toString();
        String t = i.getExtras().get("t").toString();
        String n = i.getExtras().get("n").toString();

        titel.setText(t);
        news.setText(n);
        Glide.with(Details.this).load(img).into(image);

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog();

                return true;

            }
        });


    }


    public void dialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("هل تريد حفظ الصورة ؟")
                .setNegativeButton("لا", null)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (saveImage())
                            Toast.makeText(getApplicationContext(), " تم الحفظ ", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), " فشل ", Toast.LENGTH_LONG).show();
                    }
                }).create();

        dialog.show();

    }

    public boolean saveImage() {
        try {

            BitmapDrawable draw = (BitmapDrawable) image.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            FileOutputStream outputStream = null;

            //انشاء مسار واسم االملف المراد تخزين الصور به
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/" + "فخر العرب");
            dir.mkdirs();
            //توليد اسم الصورة التى سيتم تخزينها
            //System.currentTimeMillis()دالة ترجع الفرق بين الوقت الحالى ومنتصف الليل بالمللى ثانية
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            return true;

        } catch (Exception e) {
            return false;
        }
    }


}
