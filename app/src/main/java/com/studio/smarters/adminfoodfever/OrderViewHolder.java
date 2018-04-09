package com.studio.smarters.adminfoodfever;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by daduc on 09-04-2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {
    TextView name,orderNo,total,status;
    RecyclerView itemList;
    Button submit,unSubmit;
    public OrderViewHolder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.pending_user_name);
        orderNo=itemView.findViewById(R.id.pending_order_no);
        total=itemView.findViewById(R.id.pending_order_total);
        itemList=itemView.findViewById(R.id.pending_items_list);
        submit=itemView.findViewById(R.id.ready_button);
        unSubmit=itemView.findViewById(R.id.not_ready_button);
        status=itemView.findViewById(R.id.order_status);
    }
    public void setName(String s){
        name.setText(s);
    }

    public void setOrderNo(String s) {
        orderNo.setText(s);
    }
    public void setTotal(String s){
        total.setText(s);
    }
    public void setStatus(String s){
        status.setText(s);
    }
    public void setItemList(DatabaseReference d, final Context c){
        LinearLayoutManager ll = new LinearLayoutManager(c);
        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemList.setLayoutManager(ll);
        itemList.setHasFixedSize(true);
        FirebaseRecyclerAdapter<Item,ItemsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Item, ItemsViewHolder>(
                Item.class,
                R.layout.items_row,
                ItemsViewHolder.class,
                d
        ) {
            @Override
            protected void populateViewHolder(ItemsViewHolder viewHolder, Item model, int position) {
                //Toast.makeText(c, model.getItem_name(), Toast.LENGTH_SHORT).show();
                viewHolder.setAllData(model.getItem_name(),model.getItem_quantity(),model.getItem_total_price());
            }
        };
        itemList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class ItemsViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemQuantity,itemPrice;
        public ItemsViewHolder(View mView) {
            super(mView);
            itemName=mView.findViewById(R.id.item_name);
            itemQuantity=mView.findViewById(R.id.item_quantity);
            itemPrice=mView.findViewById(R.id.item_price);
        }
        void setAllData(String n,String q,String p){
            itemName.setText(n);
            itemQuantity.setText(q);
            itemPrice.setText(p);
        }
    }
}
