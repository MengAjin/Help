package com.example.firstprogram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;


public class TopBar extends RelativeLayout {
    private Button leftBtn, rightBtn;
    private TextView textTitle;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;

    private LayoutParams leftParams,rightParams,titleParams;

    public interface topbarClickListener{
        public void leftClick();
        public void rightClick() throws UnsupportedEncodingException;
    }

    private topbarClickListener listener;

    public void setOnTopbarClickListener(topbarClickListener listener){
        this.listener = listener;
    }

    public  void setLeftBtnIsVisiable(Boolean b){
        if(b){
            leftBtn.setVisibility(View.VISIBLE);
        }else {
            leftBtn.setVisibility(View.GONE);
        }
    }
    public  void setRightBtnIsVisiable(Boolean b){
        if(b){
            rightBtn.setVisibility(View.VISIBLE);
        }else {
            rightBtn.setVisibility(View.GONE);
        }
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, 0);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);

        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);

        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, 0);
        title = ta.getString(R.styleable.TopBar_titleText);

        ta.recycle();

        leftBtn = new Button(context);
        rightBtn = new Button(context);
        textTitle = new TextView(context);

        leftBtn.setTextColor(leftTextColor);
        leftBtn.setBackground(leftBackground);
        leftBtn.setText(leftText);

        rightBtn.setTextColor(rightTextColor);
        rightBtn.setBackground(rightBackground);
        rightBtn.setText(rightText);

        textTitle.setTextColor(titleTextColor);
        textTitle.setTextSize(titleTextSize);
        textTitle.setText(title);
        textTitle.setGravity(Gravity.CENTER);

        setBackgroundColor(0xff00CCCC);

        //leftButton
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);

        addView(leftBtn, leftParams);

        //title
        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

        addView(textTitle, titleParams);

        //rightButton
        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);

        addView(rightBtn,rightParams);


        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();

            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listener.rightClick();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void setTitleText(String s){
        textTitle.setText(s);
    }
}
