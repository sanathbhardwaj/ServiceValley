package com.sanathbhardwaj.servicevalleydhanbad.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanathbhardwaj.servicevalleydhanbad.R;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends RecyclerView.ViewHolder{

    View myView;

    public ProductAdapter(@NonNull View itemView) {
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

    public void setDetails(Context context, String product_name, String product_price, String product_image){

        TextView name = myView.findViewById(R.id.name);
        TextView price = myView.findViewById(R.id.price);
        ImageView image = myView.findViewById(R.id.product_img);

        name.setText(product_name);
        price.setText(product_price);
        Picasso.get().load(product_image).into(image);
    }

    private ProductAdapter.ClickListener mClickListener;
    public interface ClickListener{
        void OnItemClick(View view, int position);
        void OnItemLongClick(View view, int position);
    }
    public void setOnClickListener(ProductAdapter.ClickListener clickListener){
        mClickListener = clickListener;
    }
}