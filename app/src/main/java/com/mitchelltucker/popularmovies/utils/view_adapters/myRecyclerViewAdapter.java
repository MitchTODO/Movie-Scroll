package com.mitchelltucker.popularmovies.utils.view_adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitchelltucker.popularmovies.utils.layout_res.screen_metrics;
import com.mitchelltucker.popularmovies.utils.network_res.NetworkUtils;
import com.mitchelltucker.popularmovies.utils.encode_objects.Movie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mitchelltucker.popularmovies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.util.List;



public class myRecyclerViewAdapter extends RecyclerView.Adapter<myRecyclerViewAdapter.ViewHolder>  {

    //private static final String TAG = "myRcyclerViewAdapter";
    private List<Movie>  mMovieData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Integer page_position;


    // data is passed into the constructor
    public myRecyclerViewAdapter(Context context, List<Movie> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mMovieData = data;

    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String urlImage = mMovieData.get(position).getPoster_path();
        String fullUrl = NetworkUtils.imageUrlBuilder(urlImage);

        //ImageView singleImageView = holder.myImageView;
        LinearLayout rowView = holder.myLinearLayout;


        final TextView errorForImage = holder.errorForImageView;
        errorForImage.setText(mMovieData.get(position).getTitle());

        // Fit to screen keep 2-3 aspect ratio
        rowView.getLayoutParams().height = screen_metrics.getScreenHeight();
        rowView.getLayoutParams().width = screen_metrics.getScreenWidth();

        page_position = position;

        errorForImage.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(fullUrl)
                //.into(holder.myImageView);
                .into(holder.myImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        errorForImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        // low bandwidth mode just show title
                        errorForImage.setVisibility(View.VISIBLE);
                    }
                });

    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TextView myTextView;
        ImageView myImageView;
        LinearLayout myLinearLayout;
        TextView errorForImageView;

        ViewHolder(View itemView) {
            super(itemView);
            //myLinearLayout = itemView.findViewById(R.id.parentRowImage);
            myImageView = itemView.findViewById(R.id.moviePic);
            errorForImageView = itemView.findViewById(R.id.hiddenTextView);
            myLinearLayout = itemView.findViewById(R.id.rowForImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    //public String getItem(int id) {
    //    return mMovieData.get(id).getOriginal_title();
    //}

    public Integer getPagePosition() {
        return page_position;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
