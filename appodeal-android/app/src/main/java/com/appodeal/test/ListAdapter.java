package com.appodeal.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> list;
    public ListAdapter(@NonNull Context context, ArrayList<String> li) {
        super(context, R.layout.list_item, li);
        list=li;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);


        //код прохода по списку

        if (list.get(position).contains("реклама")){
            Appodeal.setAutoCacheNativeIcons(true);
            Appodeal.setAutoCacheNativeMedia(false);
            Appodeal.initialize(MainActivity.self, "4a59433766a4b8e9b79871b5d7c163dcee357e5ac2029fc6", Appodeal.NATIVE);
            final View finalConvertView = convertView;
            Appodeal.setNativeCallbacks(new NativeCallbacks() {
                @Override
                public void onNativeLoaded() {
                    RelativeLayout nativeView = finalConvertView.findViewById(R.id.native_item);
                    NativeAd nativeAd = Appodeal.getNativeAds(1).get(0);

                    TextView nativeAdSign = finalConvertView.findViewById(R.id.native_ad_sign);
                    nativeAdSign.setText("Ad");

                    TextView nativeTitle = finalConvertView.findViewById(R.id.native_title);
                    nativeTitle.setText(nativeAd.getTitle());

                    TextView nativeDescription = finalConvertView.findViewById(R.id.native_description);
                    nativeDescription.setMaxLines(3);
                    nativeDescription.setEllipsize(TextUtils.TruncateAt.END);
                    nativeDescription.setText(nativeAd.getDescription());

                    RatingBar nativeRating = finalConvertView.findViewById(R.id.native_rating);
                    if (nativeAd.getRating() == 0) {
                        nativeRating.setVisibility(View.INVISIBLE);
                    } else {
                        nativeRating.setVisibility(View.VISIBLE);
                        nativeRating.setRating(nativeAd.getRating());
                        nativeRating.setIsIndicator(true);
                        nativeRating.setStepSize(0.1f);
                    }

                    Button nativeCta = finalConvertView.findViewById(R.id.native_cta);
                    nativeCta.setText(nativeAd.getCallToAction());

                    ((ImageView) finalConvertView.findViewById(R.id.native_icon)).setImageBitmap(nativeAd.getIcon());
                    ((ImageView) finalConvertView.findViewById(R.id.native_image)).setImageBitmap(nativeAd.getImage());

                    View providerView = nativeAd.getProviderView(MainActivity.self);
                    if (providerView != null) {
                        FrameLayout providerViewContainer = finalConvertView.findViewById(R.id.native_provider_view);
                        providerViewContainer.addView(providerView);
                    }
                    LinearLayout spoiler=finalConvertView.findViewById(R.id.spoiler);
                    nativeAd.registerViewForInteraction(nativeView);
                    nativeView.setVisibility(View.VISIBLE);
                    spoiler.setVisibility(View.VISIBLE);
                }
                @Override
                public void onNativeFailedToLoad() {
                }
                @Override
                public void onNativeShown(NativeAd nativeAd) {
                }
                @Override
                public void onNativeClicked(NativeAd nativeAd) {
                }
            });
        }else{
            TextView nativeDescription = convertView.findViewById(R.id.native_description);
            nativeDescription.setText(list.get(position));
        }





        return convertView;

    }
}