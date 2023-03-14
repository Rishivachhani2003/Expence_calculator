package com.example.expencecalculator;

//import static com.example.expencecalculator.R.layout.income_recycler_data;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Data;

public class IncomeFragment<obj_firebase,MyViewHolder extends RecyclerView.ViewHolder> extends Fragment {

    // Firebase database

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //recycler view ..
    private RecyclerView recyclerView;

    //textview result
    private TextView incomeTotalSum;

    //update edit text

    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;

    //button of update  and delete..
    private Button btnUpdate;
    private Button btnDelete;


    // data item..
    private String type;
    private String note;
    private int ammount;

    private String post_Key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myview = inflater.inflate(R.layout.fragment_income, container, false);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum = myview.findViewById(R.id.income_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalvalue = 0;

                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()) {

                    Data data = mysnapshot.getValue(Data.class);
                    totalvalue = totalvalue + data.getAmmount();

                    String stTotalvalue = String.valueOf(totalvalue);

                    incomeTotalSum.setText(stTotalvalue);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myview;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();
        FirebaseRecyclerAdapter<Data, DashBoardFragment.IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, DashBoardFragment.IncomeViewHolder>(
                options) {
            @Override
            protected void onBindViewHolder(@NonNull DashBoardFragment.IncomeViewHolder viewHolder, int position, @NonNull Data model) {
                viewHolder.setIncomeType(model.getType());
                viewHolder.setIncomeAmmount(model.getAmmount());
                viewHolder.setIncomeDate(model.getDate());

            }
            @NonNull
            @Override
            public DashBoardFragment.IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
        recyclerView.setAdapter(incomeAdapter);
    }
//        mRecyclerIncome.setAdapter(incomeAdapter);


//
//
//
//
//
//
//        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data,MyViewHolder>(
//                Data.class,
//                income_recycler_data,
//                MyViewHolder.class,
//                mIncomeDatabase)
//        {
//
//
//            @NonNull
//            @Override
//            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                return null;
//            }
//
//            @Override
//            protected void onBindViewHolder( MyViewHolder viewHolder, int position ,   Data model) {
//                viewHolder.setType(model.getType());
//                viewHolder.setNote(model.getNote());
//                viewHolder.setDate(model.getDate());
//                viewHolder.setAmmount(model.getAmmount());
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Overridetas
//                    public void onClick(View view) {
//
//                        post_Key=getRef(position).getKey();
//                        type=model.getType();
//                        note= model.getNote();
//                        ammount= model.getAmmount();
//
//                        updateDataItem();
//                    }
//                });
//            }
//        };
//
//        recyclerView.setAdapter(adapter);
//
//
//
//    }

        class MyViewHolder extends RecyclerView.ViewHolder {

            View mView;

            public MyViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }

            private void setType(String type) {
                TextView mType = mView.findViewById(R.id.type_txt_income);
                mType.setText(type);
            }

            private void setNote(String note) {
                TextView mNote = mView.findViewById(R.id.note_txt_income);
                mNote.setText(note);
            }

            private void setDate(String date) {
                TextView mDate = mView.findViewById(R.id.date_txt_income);
                mDate.setText(date);
            }

            private void setAmmount(int ammount) {
                TextView mAmmount = mView.findViewById(R.id.ammount_txt_income);
                String stammount = String.valueOf(ammount);
                mAmmount.setText(stammount);
            }
        }
        public void updateDataItem ()
        {
            AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View myview = inflater.inflate(R.layout.update_data_item, null);
            mydialog.setView(myview);
            edtAmmount = myview.findViewById(R.id.amount_edt);
            edtType = myview.findViewById(R.id.type_edt);
            edtNote = myview.findViewById(R.id.note_edt);

            //set data to edit text....
            edtType.setText(type);
            edtNote.setSelection(type.length());

            edtNote.setText(note);
            edtNote.setSelection(note.length());

            edtAmmount.setText(String.valueOf(ammount));
            edtAmmount.setSelection(String.valueOf(ammount).length());

            btnUpdate = myview.findViewById(R.id.btn_upd_Update);
            btnDelete = myview.findViewById(R.id.btnuPD_Delete);

            final AlertDialog dialog = mydialog.create();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    type = edtType.getText().toString().trim();
                    note = edtNote.getText().toString().trim();

                    String mdammount = String.valueOf(ammount);
                    mdammount = edtAmmount.getText().toString().trim();

                    int myAmmount = Integer.parseInt(mdammount);

                    String mDate = DateFormat.getDateInstance().format(new Date());
                    Data data = new Data(myAmmount, type, note, post_Key, mDate);

                    mIncomeDatabase.child(post_Key).setValue(data);
                    dialog.dismiss();

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mIncomeDatabase.child(post_Key).removeValue();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
}

