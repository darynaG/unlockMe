package com.example.unlockme;

import android.content.Context;

import android.graphics.drawable.PictureDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageUrls;
    private String image_type;

    ViewPagerAdapter(Context context, ArrayList<String> imageUrls, String image_type) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.image_type = image_type;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        RequestBuilder<PictureDrawable> requestBuilder;

//        Picasso.get()
//                .load(imageUrls.get(position))
//                .fit()
//                .centerCrop()
//                .into(imageView);
            if (image_type.equals("svg")) {
                requestBuilder =
                        Glide.with(context)
                                .as(PictureDrawable.class)
//                        .placeholder(R.drawable.image_loading)
//                        .error(R.drawable.image_error)
                                .transition(withCrossFade())
                                .listener(new SvgSoftwareLayerSetter());

                requestBuilder.load(imageUrls.get(position)).into(imageView);
            } else {
                Picasso.get()
                .load(imageUrls.get(position))
                .fit()
                .centerCrop()
                .into(imageView);
            }
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}