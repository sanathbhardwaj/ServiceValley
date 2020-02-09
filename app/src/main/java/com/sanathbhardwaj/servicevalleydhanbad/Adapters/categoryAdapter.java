package com.sanathbhardwaj.servicevalleydhanbad.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanathbhardwaj.servicevalleydhanbad.R;
import com.squareup.picasso.Picasso;

public class categoryAdapter extends RecyclerView.ViewHolder {

    View myView;
    public categoryAdapter(@NonNull View itemView) {
        super(itemView);

        myView = itemView;
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view, getAdapterPosition());
            }
        });
        myView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }
    public void setDetails(Context context, String name, String image){
        TextView category_tv = myView.findViewById(R.id.category_tv);
        ImageView category_img = myView.findViewById(R.id.category_img);
        category_tv.setText(name);
        Picasso.get().load(image).into(category_img);
    }
    private MyAdapter.ClickListener mClickListener;
    public interface ClickListener{
        void OnItemClick(View view, int position);
        void OnItemLongClick(View view, int position);
      }
    public void setOnClickListener(MyAdapter.ClickListener clickListener){
        mClickListener = clickListener;

    }
}