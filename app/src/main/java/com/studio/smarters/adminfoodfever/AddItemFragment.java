package com.studio.smarters.adminfoodfever;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;


public class AddItemFragment extends Fragment {
    private AppCompatActivity main;
    private View root;
    private Spinner menu,subMenu;
    private EditText name,desc,price;
    private Button submit;
    private DatabaseReference d;
    private ProgressDialog p;
    private LinearLayout linearLayout;

    public AddItemFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main=(AppCompatActivity)getActivity();
        main.getSupportActionBar().setTitle("Add An Item");
        root=inflater.inflate(R.layout.fragment_add_item, container, false);
        //Nav View
        NavigationView navigationView = (NavigationView) main.findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_add_item);
        //Other Assignment
        menu=root.findViewById(R.id.menu);
        subMenu=root.findViewById(R.id.sub_menu);
        name=root.findViewById(R.id.name);
        desc=root.findViewById(R.id.desc);
        price=root.findViewById(R.id.price);
        linearLayout=root.findViewById(R.id.sub_menu_holder);
        p=new ProgressDialog(main);
        p.setCancelable(false);
        p.setCanceledOnTouchOutside(false);
        p.setTitle("Please Wait");
        p.setMessage("Adding the New Item ...");
        final String [] menuItems= getResources().getStringArray(R.array.menu);
        submit=root.findViewById(R.id.button);
        d= FirebaseDatabase.getInstance().getReference().child("items");
        menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s=menuItems[i];
                if(s.equals("dessert")){
                    linearLayout.setVisibility(GONE);
                }else{
                    String [] subMenuItems=null;
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.show();
                String a = name.getText().toString();
                String b = desc.getText().toString();
                String c = price.getText().toString();
                if (TextUtils.isEmpty(a)  || TextUtils.isEmpty(c)) {
                    Toast.makeText(main, "You must fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if(TextUtils.isEmpty(b)) b=" ";
                    if (menu.getSelectedItem().toString().equals("dessert")) {
                        insertDessert(a,b,Integer.parseInt(c));
                    } else {
                        insertOthers(menu.getSelectedItem().toString(),subMenu.getSelectedItem().toString(),a,b,Integer.parseInt(c));
                    }
                }
                p.dismiss();
            }
        });
        return root;
    }

    private void insertOthers(String menu, String subMenu, final String name1, String desc1, int price1) {
        Map m=new HashMap();
        m.put("desc",desc1);
        m.put("price",price1);
        m.put("availability","Available");
        d.child(menu).child(subMenu).child(name1).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    name.setText("");
                    price.setText("");
                    desc.setText("");
                    Toast.makeText(main, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertDessert(String name1,String desc1,int price1) {
        Map m=new HashMap();
        m.put("desc",desc1);
        m.put("price",price1);
        m.put("availability","Available");
        d.child("dessert").child(name1).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    name.setText("");
                    price.setText("");
                    desc.setText("");
                    Toast.makeText(main, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
