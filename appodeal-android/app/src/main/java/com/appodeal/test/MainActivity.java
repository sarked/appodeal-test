package com.appodeal.test;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    public static MainActivity self;
    Button btn;
    TimeTask task;
    boolean stopable, topBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        self = this;
        btn = findViewById(R.id.btn);
        stopable=true;
        topBanner=true;
        final ListView listView=findViewById(R.id.list_view);

        //инициализация рекламы
        final String appKey = "4a59433766a4b8e9b79871b5d7c163dcee357e5ac2029fc6";

        //для листвью
        final ArrayList <String> news = new ArrayList<String>();
        news.add("новость №1");
        news.add("новость №2");
        news.add("новость №3");
        news.add("реклама");
        news.add("новость №4");
        news.add("новость №5");
        news.add("новость №6");
        news.add("реклама");
        news.add("новость №7");
        news.add("новость №8");
        news.add("новость №9");

        //верхний баннер
        if (topBanner==true){
            Appodeal.initialize(this, appKey, Appodeal.BANNER);
            Appodeal.show(MainActivity.this, Appodeal.BANNER_TOP);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    boolean a=Appodeal.isLoaded(Appodeal.BANNER_TOP);
                    while (Appodeal.isLoaded(Appodeal.BANNER_TOP)==false){
                        Thread.sleep(250);
                        System.out.println("ждем");
                    }
                        System.out.println("показался!");
                        Thread.sleep(5000);
                        System.out.println("скрыли");
                        Appodeal.hide(MainActivity.self, Appodeal.BANNER);
                        topBanner=false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        //реклама-страница
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);

        //старт обратного отсчета
        task = new TimeTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //кнопка
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //​ ​ Если​ ​ кнопка​ ​ была​ ​ нажата​ ​ в​ ​ первые​ ​ 30​ ​ секунд,​ ​ то показа​ ​ не​ ​ должно​ ​ быть​ ​ вообще.
                if (stopable==true){
                    task.cancel(true);
                }
                //вызов нативки
                ListAdapter adapter = new ListAdapter(MainActivity.this,news);
                listView.setAdapter(adapter);
            }
        });
    }

    public void onFinish() {
        Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
        stopable=false;

    }

    @Override
    public void onBackPressed() {
        task.cancel(true);
        MainActivity.self.finish();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        btn.setText("30");
        task = new TimeTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public void showNativeAd() {
        RelativeLayout nativeView = findViewById(R.id.native_item);
        NativeAd nativeAd = Appodeal.getNativeAds(1).get(0);

        TextView nativeAdSign = nativeView.findViewById(R.id.native_ad_sign);
        nativeAdSign.setText("Ad");

        TextView nativeTitle = nativeView.findViewById(R.id.native_title);
        nativeTitle.setText(nativeAd.getTitle());

        TextView nativeDescription = nativeView.findViewById(R.id.native_description);
        nativeDescription.setMaxLines(3);
        nativeDescription.setEllipsize(TextUtils.TruncateAt.END);
        nativeDescription.setText(nativeAd.getDescription());

        RatingBar nativeRating = nativeView.findViewById(R.id.native_rating);
        if (nativeAd.getRating() == 0) {
            nativeRating.setVisibility(View.INVISIBLE);
        } else {
            nativeRating.setVisibility(View.VISIBLE);
            nativeRating.setRating(nativeAd.getRating());
            nativeRating.setIsIndicator(true);
            nativeRating.setStepSize(0.1f);
        }

        Button nativeCta = nativeView.findViewById(R.id.native_cta);
        nativeCta.setText(nativeAd.getCallToAction());

        ((ImageView) nativeView.findViewById(R.id.native_icon)).setImageBitmap(nativeAd.getIcon());
        ((ImageView) nativeView.findViewById(R.id.native_image)).setImageBitmap(nativeAd.getImage());

        View providerView = nativeAd.getProviderView(MainActivity.this);
        if (providerView != null) {
            FrameLayout providerViewContainer = nativeView.findViewById(R.id.native_provider_view);
            providerViewContainer.addView(providerView);
        }

        nativeAd.registerViewForInteraction(nativeView);
        nativeView.setVisibility(View.VISIBLE);
    }
}