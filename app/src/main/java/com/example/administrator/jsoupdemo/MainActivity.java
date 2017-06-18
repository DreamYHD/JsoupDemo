package com.example.administrator.jsoupdemo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<String> titleList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private List<String> bitmapList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private  MyAdapter mMyAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Handler mHandler = new Handler(){
        public void handleMessage(Message mMessage){
            switch (mMessage.what){
                case 1: mMyAdapter.notifyDataSetChanged();

                    break;
                default:
                    break;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swip);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mMyAdapter = new MyAdapter(titleList,bitmapList,urlList,this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mMyAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMyAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } );
        mMyAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void click(View mView, int position) {
                Intent mIntent = new Intent(MainActivity.this,ShowNews.class);
                mIntent.putExtra("url",urlList.get(position));
                startActivity(mIntent);
            }
         }

        );


        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("http://www.woshipm.com/").get();
                } catch (IOException mE) {
                    mE.printStackTrace();
                }
                Elements topnews = doc.getElementsByClass("stream-list-title");
                Elements links = topnews.select("a[href]");

                for (Element link : links) {
                    if (!link.text().equals("")) {
                        titleList.add(link.html());
                        urlList.add(link.attr("href"));
                        Log.d(TAG, "onCreate: " + link.html() + link.attr("href"));
                    }

                }
                Elements image = doc.getElementsByClass("stream-list-image");
                Elements imageLists = image.select("a[href]");

                for (Element link : imageLists) {
                    if (link.text().equals("")) {

                        String []mStrings = link.toString().split("><img src=");
                        String []mStrings1 = mStrings[1].split("\"");
                        bitmapList.add(mStrings1[1]);
                        Log.d(TAG, "onCreate: " +mStrings1[1]);
                    }

                }
                Message m = new Message();
                m.what = 1;
                mHandler.sendMessage(m);
            }
        }).start();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
