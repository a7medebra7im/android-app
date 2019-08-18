package com.example.fakhrelarab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class news_Recycler_Adapter extends RecyclerView.Adapter<news_Recycler_Adapter.mViewholder> {


    public List<get_News> news_list;
    public Context context;


    public news_Recycler_Adapter(List<get_News> news_list) {
        this.news_list = news_list;
    }

    @NonNull
    @Override
    public mViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_form,viewGroup,false);
        context = viewGroup.getContext();
        return new mViewholder(view) ;

    }

    @Override
    public void onBindViewHolder(@NonNull mViewholder viewHolder, int i) {

        get_News pos = news_list.get(i);

        String tiy = pos.getTitel();
        viewHolder.set_Titel(tiy);

        String ne = pos.getNews();

        String img = pos.getImg_Url();
        viewHolder.set_Image_News(img);




    }

    @Override
    public int getItemCount() {
        return news_list.size();
    }


    public class mViewholder extends RecyclerView.ViewHolder {

        private View mview;
        private ImageView news_Image;

        private TextView tietl ;


        public mViewholder(@NonNull View itemView) {
            super(itemView);

            mview = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    get_News position = news_list.get(getAdapterPosition());

                    String i = position.getImg_Url();
                    String t = position.getTitel();
                    String n = position.getNews();

                    Intent intent = new Intent(context, Details.class);
                    intent.putExtra("i",i);
                    intent.putExtra("t",t);
                    intent.putExtra("n",n);
                    context.startActivity(intent);


                }
            });
        }




        public void set_Titel(String s)
        {
            tietl = mview.findViewById(R.id.text_news_id);
            tietl.setText(s);
        }
        public void set_Image_News(String img_url)
        {
            news_Image = mview.findViewById(R.id.imageView2);

            Glide.with(context).load(img_url).into(news_Image);

        }


    }



}
