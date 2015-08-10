package com.example.firstprogram;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BottomBar extends LinearLayout {
    private ImageView imageView1, imageView2, imageView3, imageView4;

    private Drawable image1, image2, image3, image4;
    private float image1Height, image1Width,image2Height, image2Width,image3Height, image3Width,image4Height, image4Width;

    private  LayoutParams param1, param2, param3, param4;

    public interface bottombarClickListener{
        public void Click1();
        public void Click2();
        public void Click3();
        public void Click4();
    }
    private bottombarClickListener listener;

    public void setOnBottombarClickListener(bottombarClickListener listener) {
        this.listener = listener;
    }


    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
        image1 = ta.getDrawable(R.styleable.BottomBar_ImageView1);
        image2 = ta.getDrawable(R.styleable.BottomBar_ImageView2);
        image3 = ta.getDrawable(R.styleable.BottomBar_ImageView3);
        image4 = ta.getDrawable(R.styleable.BottomBar_ImageView4);
        image1Height = ta.getDimension(R.styleable.BottomBar_ImageView1Height, 50);
        image2Height = ta.getDimension(R.styleable.BottomBar_ImageView2Height, 0);
        image3Height = ta.getDimension(R.styleable.BottomBar_ImageView3Height, 0);
        image4Height = ta.getDimension(R.styleable.BottomBar_ImageView4Height, 0);

        image1Width = ta.getDimension(R.styleable.BottomBar_ImageView1Width, 50);
        image2Width = ta.getDimension(R.styleable.BottomBar_ImageView2Width, 0);
        image3Width = ta.getDimension(R.styleable.BottomBar_ImageView3Width, 0);
        image4Width = ta.getDimension(R.styleable.BottomBar_ImageView4Width, 0);

        ta.recycle();

        imageView1 = new ImageView(context);
        imageView2 = new ImageView(context);
        imageView3 = new ImageView(context);
        imageView4 = new ImageView(context);

        imageView1.setImageDrawable(image1);
        imageView1.setAdjustViewBounds(true);
        imageView1.setMaxHeight((int) image1Height);
        imageView1.setMaxWidth((int) image1Width);

        imageView2.setImageDrawable(image2);
        imageView2.setAdjustViewBounds(true);
        imageView2.setMaxHeight((int) image2Height);
        imageView2.setMaxWidth((int) image2Width);

        imageView3.setImageDrawable(image3);
        imageView3.setAdjustViewBounds(true);
        imageView3.setMaxHeight((int) image3Height);
        imageView3.setMaxWidth((int) image3Width);


        imageView4.setImageDrawable(image4);
        imageView4.setAdjustViewBounds(true);
        imageView4.setMaxHeight((int) image4Height);
        imageView4.setMaxWidth((int) image4Width);

        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(0x8800CCCC);

        param1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param1.gravity = Gravity.CENTER_VERTICAL;
        param1.weight = 1;
        addView(imageView1, param1);

        param2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param2.gravity = Gravity.CENTER_VERTICAL;
        param2.weight = 1;
        addView(imageView2, param2);

        param3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param3.gravity = Gravity.CENTER_VERTICAL;
        param3.weight = 1;
        addView(imageView3, param3);

        param4 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param4.gravity = Gravity.CENTER_VERTICAL;
        param4.weight = 1;
        addView(imageView4, param4);

        imageView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Click1();
            }
        });
        imageView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Click2();
            }
        });
        imageView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Click3();
            }
        });
        imageView4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.Click4();
            }
        });
    }
}
