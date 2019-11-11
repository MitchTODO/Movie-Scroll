package com.mitchelltucker.popularmovies.model;

import android.content.Context;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitchelltucker.popularmovies.R;
import com.mitchelltucker.popularmovies.utils.layout_res.screen_metrics;

class View_creators {
    /**
     * createButton
     * @param context: Context
     * @param tag: String
     * @param text: String
     * @param id: integer
     * @return Button
     */
    static Button createButton(Context context, String tag, String text, Integer id) {
        Button newButton = new Button(context);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        newButton.setText(text);
        newButton.setTag(tag);
        newButton.setId(id);
        newButton.setBackgroundResource(android.R.drawable.btn_default);
        return newButton;
    }

    /**
     * createTextView
     * @param context: Context
     * @param text: int
     * @param textSize: int
     * @return TextView
     */
    static TextView createTextView(Context context, int text,int textSize){
        int padding = screen_metrics.getPadding(context,10);
        TextView newTextView = new TextView(context);
        newTextView.setText(text);
        newTextView.setTextColor(context.getResources().getColor(R.color.colorBackScroll));
        newTextView.setTextSize(textSize);
        newTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        newTextView.setPadding(padding,padding,padding,padding);
        newTextView.setBackgroundColor(context.getResources().getColor(R.color.yearSearchWithoutTrans));
        newTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return newTextView;
    }

    /**
     * createImageView
     * @param context: Context
     * @param tag: String
     * @param id: int
     * @return ImageView
     */
    static ImageView createImageView(Context context, String tag, int id) {
        ImageView newImageView = new ImageView(context);
        newImageView.setId(id);
        newImageView.setTag(tag);
        //newImageView.setLayoutParams(new LinearLayout.LayoutParams(height, screen_metrics.getScreenWidth()));

        return newImageView;
    }
}
