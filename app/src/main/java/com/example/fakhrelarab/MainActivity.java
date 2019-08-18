package com.example.fakhrelarab;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView news_View ;

    private List<get_News> news_List ;
    private FirebaseFirestore firebaseFirestore;
    private news_Recycler_Adapter adapter;
    private DocumentSnapshot lastnews;
    private ProgressBar progressBar;

    private int c=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

// كود الاشتراك فى اشعارات الفاير بيز كلود ميسج
        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

            progressBar = findViewById(R.id.progress_main);
            news_View = findViewById(R.id.news_viewer_id);
            news_List = new ArrayList<>();
            adapter = new news_Recycler_Adapter(news_List);
            news_View.setLayoutManager(new LinearLayoutManager(this));
            news_View.setAdapter(adapter);

        news_View.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    boolean key = !recyclerView.canScrollVertically(1);

                    if (key) {
                        progressBar.setVisibility(View.VISIBLE);
                        lode_More_News();
                    }
                }
            });

        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore = FirebaseFirestore.getInstance();
        //تحميل العناصر داخل الريسايكلر عن طريق  التايم ستامب  والترتيب يكون تنازليا من الاحدث للاقدم
        Query first_query = firebaseFirestore.collection("news")
                  .orderBy("timestamp",Query.Direction.DESCENDING)//من الاحدث للاقدم(اخر بيانات دخلت تكون اول بيانات تخرج)
                  .limit(8);//عدد العناصر المراد تحميلها كل استعلام

        first_query.addSnapshotListener(new EventListener<QuerySnapshot>() {


                   @Override
                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                       try {

                           progressBar.setVisibility(View.VISIBLE);
                           lastnews = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                       }catch (Exception s)
                       {
                           progressBar.setVisibility(View.INVISIBLE);
                           Toast.makeText(getApplicationContext(),"لا يوجداتصال بالانترنت",Toast.LENGTH_SHORT).show();
                       }


                       for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                           if (doc.getType() == DocumentChange.Type.ADDED) {
                               get_News get = doc.getDocument().toObject(get_News.class);
                               news_List.add(get);

                               adapter.notifyDataSetChanged();

                           }
                       }

                       progressBar.setVisibility(View.INVISIBLE);
                   }
               });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.salah_id) {
            startActivity(new Intent(MainActivity.this,Salah.class));

        } else if (id == R.id.mor_app) {

            String link = "https://play.google.com/store/apps/developer?id=Ahmed+Ebrahim";
            Intent mor = new Intent(Intent.ACTION_VIEW);
            mor.setData(Uri.parse(link));
            startActivity(mor);


        } else if (id == R.id.rate_app)
        {
            Intent rate = new Intent(Intent.ACTION_VIEW);
            rate.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.example.fakhrelarab"));
            startActivity(rate);

        } else if (id == R.id.exite_app)
        {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("الخروج من التطبيق ؟")
                    .setNegativeButton("لا", null)
                    .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           finish();
                        }
                    }).create();

            dialog.show();

        } else if (id == R.id.share_app)
        {
            String textshare = "تابع اخر اخبار النجم محمد صلاح";
            String sharelink = "https://play.google.com/store/apps/details?id=com.example.fakhrelarab";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT,textshare+"\n"+sharelink);
            startActivity(share);

        } else if (id == R.id.con_app)
        {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setData(Uri.parse("mailto:"));
            send.setType("message/rfc822");
            send.putExtra(Intent.EXTRA_EMAIL,"a7medebra7imali@gmail.com");
            send.putExtra(Intent.EXTRA_SUBJECT,"عنوان الرسالة");
            send.putExtra(Intent.EXTRA_TEXT,"موضوع الرسالة");
            try
            {
                startActivity(send);
            }catch (Exception e)
            {
                Toast.makeText(this,"عذرا لا يوجد تطبيق بريد",Toast.LENGTH_LONG).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void lode_More_News()
    {
        //تحميل العناصر داخل الريسايكل عن طريق قت انشاء تايم ستامب  والترتيب يكون تنازليا من الاحدث للاقدم
        Query nextqurey = firebaseFirestore.collection("news")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .startAfter(lastnews)
                .limit(5);

        nextqurey.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(!queryDocumentSnapshots.isEmpty())
                {

                    lastnews = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() -1 );

                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        if (doc.getType()==DocumentChange.Type.ADDED)
                        {
                            get_News get = doc.getDocument().toObject(get_News.class);
                            news_List.add(get);

                            adapter.notifyDataSetChanged();
                        }
                    }
                }
             progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void fakhr(View view)
    {
        c++;
        if (c==7)
        {
            c=0;
            startActivity(new Intent(MainActivity.this,login.class));
            finish();
        }

    }



}
