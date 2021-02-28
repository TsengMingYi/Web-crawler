package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Handler handler;
    private Adapter adapter = new Adapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        isNetWork(MainActivity.this);
        newsArrayList = getNews();
        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                if(message.what == 1){
                    recyclerView.setAdapter(adapter);
                    adapter.updateProductData(newsArrayList);

                }
            }
        };
        adapter.updateProductData(newsArrayList);
        Log.e("size1",newsArrayList.size()+"");

    }

    private void findView() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public boolean isNetWork(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<News> getNews() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
//                    String NetUrl = "https://voice.hupu.com/nba";
//                    //Connection conn = Jsoup.connect(NetUrl);
//                    Connection coon = Jsoup.connect(NetUrl);
//                    //coon.header("User-Agent","Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/     20100101 Firefox/32.0");
//                    final Document docs = coon.get();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Elements elements = docs.select("div.list-hd");
//                            for(Element element : elements){
//                                String title = element.select("a").text();
//                                String url = element.select("a").attr("href");
//                            }
//                            Elements timements = docs.select("div.otherInfo");
//                            for(Element element : timements){
//                                String time = element.select("span.other-left").select("a").text();
//
//                            }
//                            hello.setText(docs.toString());
//                        }
//                    });

                    for (int i = 1; i <= 20; i++) {

                        Document doc = Jsoup.connect("https://voice.hupu.com/nba/" + Integer.toString(i)).get();
                        Elements titleLinks = doc.select("div.list-hd");    //解析来获取每条新闻的标题与链接地址
                        // Elements descLinks = doc.select("div.list-content");//解析来获取每条新闻的简介
                        Elements timeLinks = doc.select("div.otherInfo");   //解析来获取每条新闻的时间与来源
                        Log.e("title", Integer.toString(titleLinks.size()));
                        for (int j = 0; j < titleLinks.size(); j++) {
                            String title = titleLinks.get(j).select("a").text();
                            String uri = titleLinks.get(j).select("a").attr("href");
                            //   String desc = descLinks.get(j).select("span").text();
                            String time = timeLinks.get(j).select("span.other-left").select("a").text();
                            News news = new News(title, uri, time);
                            news.setNewsTitle(title);
                            news.setNewsUrl(uri);
                            news.setNewsTime(time);
                            Log.e("detail",news.getNewsTitle());
                            Log.e("URL",news.getNewsUrl());
                            newsArrayList.add(news);
                            Log.e("size",newsArrayList.size()+"");
                        }
                    }
                    //adapter.updateProductData(newsArrayList);

//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            recyclerView.setAdapter(adapter);
//                            adapter.updateProductData(newsArrayList);
//                        }
//                    });

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return newsArrayList;
    }

}