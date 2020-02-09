package com.sanathbhardwaj.servicevalleydhanbad.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanathbhardwaj.servicevalleydhanbad.R;
import com.squareup.picasso.Picasso;

public class CartAdapter extends RecyclerView.ViewHolder {

    View myView;
    public CartAdapter(@NonNull View itemView) {
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

    public void setDetails(Context context, String Product_name, String Product_price, String Product_quantity){

        TextView name = myView.findViewById(R.id.name);
        TextView priceperunit = myView.findViewById(R.id.priceperunit);
        TextView quantity = myView.findViewById(R.id.quantity);
        TextView price = myView.findViewById(R.id.price);

        name.setText(Product_name);
        priceperunit.setText(Product_price);
        quantity.setText(Product_quantity);
        int price_of_product = Integer.valueOf(Product_price)*Integer.valueOf(Product_quantity);
        price.setText(String.valueOf(price_of_product));
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