package com.example.fakhrelarab;


public class get_News {

    public   String title,news,image_uri;


    public  get_News()
    {
    }

    public get_News(String title, String news, String img_Url) {
        this.title = title;
        this.news = news;
        this.image_uri = img_Url;

    }


    public String getTitel() {
        return title;
    }

    public void setTitel(String titel) {
        this.title = titel;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getImg_Url() {
        return image_uri;
    }

    public void setImg_Url(String img_Url) {
        this.image_uri = img_Url;
    }




}
