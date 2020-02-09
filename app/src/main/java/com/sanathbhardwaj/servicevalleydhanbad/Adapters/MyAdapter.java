package com.sanathbhardwaj.servicevalleydhanbad.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanathbhardwaj.servicevalleydhanbad.R;
import com.squareup.picasso.Picasso;

public class MyAdapter extends RecyclerView.ViewHolder {

    View myView;
    public MyAdapter(@NonNull View itemView) {
        super(itemView);

        myView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }

    public void findDist(String dist){

        TextView distant = myView.findViewById(R.id.distant);
        distant.setText(dist+" Km");

//        if (Integer.parseInt(dist)>11){
//            ViewGroup parent = (ViewGroup) itemView.getParent();
//                parent.removeView(itemView);
//                parent.notify();
//
//        }

    }

    public void setDetails(Context context, String title, String slogan, String image){

        TextView shop_name = myView.findViewById(R.id.shop_name);
        TextView shop_slogan = myView.findViewById(R.id.shop_slogan);
        ImageView banner = myView.findViewById(R.id.banner);

        shop_name.setText(title);
        shop_slogan.setText(slogan);
        Picasso.get().load(image).into(banner);
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