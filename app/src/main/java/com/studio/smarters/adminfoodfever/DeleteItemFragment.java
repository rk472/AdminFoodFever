package com.studio.smarters.adminfoodfever;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;


public class DeleteItemFragment extends Fragment {
    private AppCompatActivity main;
    private View root;
    private Spinner menu,subMenu,name;
    private Button submit;
    private DatabaseReference d;
    private ProgressDialog p;
    private LinearLayout linearLayout;
    private String [] subMenuItems;

    public DeleteItemFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main=(AppCompatActivity)getActivity();
        main.getSupportActionBar().setTitle("Delete An Item");
        root= inflater.inflate(R.layout.fragment_delete_item, container, false);
        //Nav View
        NavigationView navigationView = (NavigationView) main.findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_delete_item);
        //Other Assignment
        menu=root.findViewById(R.id.menu_delete);
        subMenu=root.findViewById(R.id.sub_menu_delete);
        name=root.findViewById(R.id.item_delete);
        linearLayout=root.findViewById(R.id.sub_menu_holder_delete);
        p=new ProgressDialog(main);
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        p.setTitle("Please Wait");
        p.setMessage("Deleting the Item ...");
        final String [] menuItems= getResources().getStringArray(R.array.menu);
        submit=root.findViewById(R.id.button_delete);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(main)
                        .setTitle("Delte Item")
                        .setMessage("Deleting this item will permanently remove it from database and the action can't be reverted .\nDo You Really want to continue ?")
                        .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                p.show();
                                if (menu.getSelectedItem().toString().equals("dessert")) {
                                    deleteDesert(name.getSelectedItem().toString());
                                } else {
                                    deleteOthers(menu.getSelectedItem().toString(),subMenu.getSelectedItem().toString(),name.getSelectedItem().toString());
                                }
                                p.dismiss();
                            }
                        }).setNegativeButton("No, Don't",null).show();
            }
        });
        return root;
    }

    private void deleteOthers(String menu, String subMenu, final String name1) {
        d.child(menu).child(subMenu).child(name1).removeValue().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(main, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteDesert(String name1) {
        d.child("dessert").child(name1).removeValue().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(main, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setNames(String subMenuName){
        final ProgressDialog pd = new ProgressDialog(main);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Please Wait");
        pd.setMessage("Fetching the Item Names...");
        pd.show();
        d.child(menu.getSelectedItem().toString()).child(subMenuName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<String>();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemname = itemSnapshot.getKey().toString();;
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
    public void setNamesDesert(){
        final ProgressDialog pd = new ProgressDialog(main);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("Please Wait");
        pd.setMessage("Fetching the Item Names...");
        pd.show();
        d.child("dessert").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<String>();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    String itemname = itemSnapshot.getKey().toString();;
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
