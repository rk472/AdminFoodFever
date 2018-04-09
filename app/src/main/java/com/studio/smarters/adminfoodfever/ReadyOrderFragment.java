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

public class ReadyOrderFragment extends Fragment {
    private View root;
    private RecyclerView list;
    private DatabaseReference pendingRef;
    private ProgressDialog p;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_ready_order, container, false);
        list=root.findViewById(R.id.ready_list);
        p=new ProgressDialog(getActivity());
        p.setTitle("Please Wait");
        p.setMessage("Please Wait While The Ready Orders are being loaded..");
        p.setCanceledOnTouchOutside(false);
        p.show();
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(true);
        pendingRef= FirebaseDatabase.getInstance().getReference().child("ready_orders");
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
                        viewHolder.setName(name);
                        viewHolder.setOrderNo(orderId);
                        viewHolder.setTotal(totalPrice);
                        viewHolder.setItemList(d.child("items"),getActivity().getApplicationContext());
                        viewHolder.submit.setText("Delivered");
                        viewHolder.status.setVisibility(View.GONE);
                        viewHolder.submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseDatabase.getInstance().getReference().child("delivered_orders").push().child("order_id").setValue(orderId);
                                d.child("status").setValue("delivered");
                                pendingRef.child(key).removeValue();
                            }
                        });
                        viewHolder.unSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseDatabase.getInstance().getReference().child("delivered_orders").push().child("order_id").setValue(orderId);
                                d.child("status").setValue("undelivered");
                                pendingRef.child(key).removeValue();
                            }
                        });
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
