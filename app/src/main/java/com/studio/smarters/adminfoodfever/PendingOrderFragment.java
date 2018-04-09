package com.studio.smarters.adminfoodfever;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

public class PendingOrderFragment extends Fragment {

    private RecyclerView list;
    private DatabaseReference pendingRef;
    private ProgressDialog p;
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_pending_order, container, false);
        list=root.findViewById(R.id.pending_list);
        p=new ProgressDialog(getActivity());
        p.setTitle("Please Wait");
        p.setMessage("Please Wait While The Pending Orders are being loaded..");
        p.setCanceledOnTouchOutside(false);
        p.show();
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(true);
        pendingRef= FirebaseDatabase.getInstance().getReference().child("pending_orders");
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
                        viewHolder.setItemList(d.child("items"),getActivity());
                        viewHolder.submit.setText("Ready");
                        viewHolder.unSubmit.setVisibility(View.GONE);
                        viewHolder.status.setVisibility(View.GONE);
                        viewHolder.submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseDatabase.getInstance().getReference().child("ready_orders").push().child("order_id").setValue(orderId);
                                d.child("status").setValue("ready");
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
