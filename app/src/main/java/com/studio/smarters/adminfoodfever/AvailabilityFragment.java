package com.studio.smarters.adminfoodfever;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;


public class AvailabilityFragment extends Fragment {
    private AppCompatActivity main;
    private View root;
    private Spinner menu,subMenu,name;
    private Button submit,notsubmit;
    private TextView av_text,nv_text;
    private DatabaseReference d;
    private ProgressDialog p;
    private LinearLayout linearLayout;
    private String [] subMenuItems;
    private Map mapAvailable;

    public AvailabilityFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main=(AppCompatActivity)getActivity();
        main.getSupportActionBar().setTitle("Change Availability");
        root=inflater.inflate(R.layout.fragment_availability, container, false);
        //Nav View
        NavigationView navigationView = (NavigationView) main.findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_change_availability);

        //Other Assignment
        menu=root.findViewById(R.id.menu_available);
        subMenu=root.findViewById(R.id.sub_menu_available);
        name=root.findViewById(R.id.item_available);
        linearLayout=root.findViewById(R.id.sub_menu_holder_available);
        p=new ProgressDialog(main);
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        p.setTitle("Please Wait");
        p.setMessage("Modifying the Item ...");
        final String [] menuItems= getResources().getStringArray(R.array.menu);
        submit=root.findViewById(R.id.button_available);
        notsubmit=root.findViewById(R.id.button_unavailable);
        av_text=root.findViewById(R.id.available_text);
        nv_text=root.findViewById(R.id.unavailable_text);
        d= FirebaseDatabase.getInstance().getReference().child("items");
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s=menuItems[i];
                if(s.equals("dessert")){
                    linearLayout.setVisibility(GONE);
                    setNamesDesert();
                }else{
                    subMenuItems=null;
                    linearLayout.setVisibility(View.VISIBLE);
                    if(s.equals("starter")){
                        subMenuItems=getResources().getStringArray(R.array.starter);
                    }else if(s.equals("main course")){
                        subMenuItems=getResources().getStringArray(R.array.main_course);
                    }else if(s.equals("others")){
                        subMenuItems=getResources().getStringArray(R.array.others);
                    }else if(s.equals("chinese")){
                        subMenuItems=getResources().getStringArray(R.array.chinese);
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter(main,android.R.layout.simple_spinner_item,subMenuItems);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subMenu.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String submenuName = subMenuItems[i];
                setNames(submenuName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String avail = mapAvailable.get(name.getSelectedItem().toString()).toString();
                if(avail.equalsIgnoreCase("available")) {
                    nv_text.setVisibility(View.GONE);
                    av_text.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    notsubmit.setVisibility(View.GONE);
                }else{
                    av_text.setVisibility(View.GONE);
                    nv_text.setVisibility(View.VISIBLE);
                    notsubmit.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(main)
                        .setTitle("Update Item")
                        .setMessage("If you make it UnAvailable then Users can't order it untill you make it Available.\n\nDo You Really want to continue ?")
                        .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                p.show();
                                if (menu.getSelectedItem().toString().equals("dessert")) {
                                    deleteDesert(name.getSelectedItem().toString(),"Not Available");
                                } else {
                                    deleteOthers(menu.getSelectedItem().toString(),subMenu.getSelectedItem().toString(),name.getSelectedItem().toString(),"Not Available");
                                }
                                p.dismiss();
                            }
                        }).setNegativeButton("No, Don't",null).show();
            }
        });
        notsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(main)
                        .setTitle("Update Item")
                        .setMessage("If you make it Available then Users can order it untill you make it UnAvailable.\n\nDo You Really want to continue ?")
                        .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                p.show();
                                if (menu.getSelectedItem().toString().equals("dessert")) {
                                    deleteDesert(name.getSelectedItem().toString(),"Available");
                                } else {
                                    deleteOthers(menu.getSelectedItem().toString(),subMenu.getSelectedItem().toString(),name.getSelectedItem().toString(),"Available");
                                }
                                p.dismiss();
                            }
                        }).setNegativeButton("No, Don't",null).show();
            }
        });
        return root;
    }

    private void deleteOthers(String menu, String subMenu, final String name1,final String avail) {
        d.child(menu).child(subMenu).child(name1).child("availability").setValue(avail).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(main, "Availability Changed Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteDesert(String name1,final String avail) {
        d.child("dessert").child(name1).child("availability").setValue(avail).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(main, "Availability Changed Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setNames(String subMenuName){
        final ProgressDialog pd = new ProgressDialog(main);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Please Wait");
        pd.setMessage("Fetching the Item Data...");
        pd.show();
        d.child(menu.getSelectedItem().toString()).child(subMenuName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapAvailable=new HashMap();
                final List<String> items = new ArrayList<String>();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemname = itemSnapshot.getKey().toString();
                    items.add(itemname);
                    String availability = itemSnapshot.child("availability").getValue().toString();
                    mapAvailable.put(itemname,availability);
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(main, android.R.layout.simple_spinner_item, items);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                name.setAdapter(areasAdapter);
                pd.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setNamesDesert(){
        final ProgressDialog pd = new ProgressDialog(main);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Please Wait");
        pd.setMessage("Fetching the Item Data...");
        pd.show();
        d.child("dessert").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<String>();
                mapAvailable=new HashMap();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemname = itemSnapshot.getKey().toString();
                    String availability = itemSnapshot.child("availability").getValue(String.class);
                    mapAvailable.put(itemname,availability);
                    items.add(itemname);
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(main, android.R.layout.simple_spinner_item, items);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                name.setAdapter(areasAdapter);
                pd.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}