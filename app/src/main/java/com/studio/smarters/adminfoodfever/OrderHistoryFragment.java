package com.studio.smarters.adminfoodfever;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderHistoryFragment extends Fragment {

    private View root;
    ProgressDialog p;
    private RecyclerView list;
    private DatabaseReference pendingRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_order_history, container, false);
        list=root.findViewById(R.id.order_history_list);
        p=new ProgressDialog(getActivity());
        p.setTitle("Please Wait");
        p.setMessage("Please Wait While The Order History is being loaded..");
        p.setCanceledOnTouchOutside(false);
        p.show();
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(true);
        pendingRef= FirebaseDatabase.getInstance().getReference().child("delivered_orders");
        FirebaseRecyclerAdapter<OrderId,OrderViewHolder> f=new FirebaseRecyclerAdapter<OrderId, OrderViewHolder>(
                OrderId.class,
                R.layout.pending_order_row,
                OrderViewHolder.class,
                pendingRef
        ) {
            @Override
            protected void populateViewHolder(final OrderViewHolder viewHolder, OrderId model, int position) {
                p.dismiss();
                final String orderId=model.getOrder_id();
                final String key=getRef(position).getKey();
                final DatabaseReference d=FirebaseDatabase.getInstance().getReference("orders").child(orderId);
                d.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("name").getValue().toString();
                        String totalPrice=dataSnapshot.child("total_price").getValue().toString();
                        String status=dataSnapshot.child("status").getValue().toString();
                        viewHolder.setName(name);
                        viewHolder.setOrderNo(orderId);
                        viewHolder.setTotal(totalPrice);
                        viewHolder.setItemList(d.child("items"),getActivity().getApplicationContext());
                        viewHolder.submit.setVisibility(View.GONE);
                        viewHolder.unSubmit.setVisibility(View.GONE);
                        viewHolder.setStatus(status);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        list.setAdapter(f);
        return root;
    }
}
