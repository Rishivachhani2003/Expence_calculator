package com.example.expencecalculator;

import static com.example.expencecalculator.R.id;
import static com.example.expencecalculator.R.layout;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import Model.Data;


public class DashBoardFragment extends Fragment {

    //floating button

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating button textview ..

    private TextView fab_income_txt;
    private TextView fab_expense_txt;


    //boolean
    private boolean isOpen = false;

    //object of animaion class
    private Animation FadOpen, FadClose;

    //dashboard income and expense..

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    // firebase...

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    //recycler view ....
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    //recycler adapter..
//    private FirebaseRecyclerAdapter recyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(layout.fragment_dash_board, container, false);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        // for syncing data to firebase...
        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);


        //connecting  floating with the layout ..
        fab_main_btn = myview.findViewById(id.fb_main_plus_btn);
        fab_income_btn = myview.findViewById(id.income_Ft_btn);
        fab_expense_btn = myview.findViewById(id.expense_Ft_btn);

        // conneting floating text

        fab_income_txt = myview.findViewById(id.income_Ft_text);
        fab_expense_txt = myview.findViewById(id.expense_ft_text);

        // total income and expense result set...

        totalIncomeResult = myview.findViewById(id.income_set_result);
        totalExpenseResult = myview.findViewById(id.expense_set_result);

        //Recycler view..
        mRecyclerIncome = myview.findViewById(id.recycler_income);
        mRecyclerExpense = myview.findViewById(id.recycler_expense);


        //animation connect..

        FadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addData();

                if (isOpen) {
                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen = false;

                } else {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen = true;

                }
            }
        });

        //calculate total income....
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalsum=0;

                for (DataSnapshot mysnap : dataSnapshot.getChildren()) {
//                    totalsum=0;
                    Data data = mysnap.getValue(Data.class);
                    totalsum = totalsum + data.getAmmount();
                    String stResult = String.valueOf(totalsum);

                    totalIncomeResult.setText(stResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //calculate total expense....
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalsum = 0;
                for (DataSnapshot mysnapshot : dataSnapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    totalsum += data.getAmmount();
                    String strTotalSum = String.valueOf(totalsum);
                    totalExpenseResult.setText(strTotalSum);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Recycler view....

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        return myview;

    }

    //floating button animation..

    private void ftAnimation() {
        if (isOpen) {
            fab_income_btn.startAnimation(FadClose);
            fab_expense_btn.startAnimation(FadClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadClose);
            fab_expense_txt.startAnimation(FadClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen = false;

        } else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen = true;

        }

    }


    private void addData() {
        // fab button income ...

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeDataInsert();

            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDataInsert();

            }
        });
    }


    public void incomeDataInsert() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myviewm = inflater.inflate(layout.custom_layout_for_insertdata, null);
        mydialog.setView(myviewm);
        AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        EditText edtAmmount = myviewm.findViewById(id.amount_edt);
        EditText edtType = myviewm.findViewById(id.type_edt);
        EditText edtNote = myviewm.findViewById(id.note_edt);

        Button btnSave = myviewm.findViewById(id.btnSave);
        Button btnCancel = myviewm.findViewById(id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = edtType.getText().toString().trim();
                String ammount = edtAmmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    edtType.setError("Required field...");
                    return;
                }
                if (TextUtils.isEmpty(ammount)) {
                    edtAmmount.setError("Required field...");
                    return;
                }

                int ourammountint = Integer.parseInt(ammount);

                if (TextUtils.isEmpty(note)) {
                    edtNote.setError("Required field...");
                    return;
                }

                String id = mIncomeDatabase.push().getKey();
                String mDate = java.text.DateFormat.getDateInstance().format(new Date());
                //String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(ourammountint, type, note, id, mDate);

                mIncomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "DATA ADDED", Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public void expenseDataInsert() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(layout.custom_layout_for_insertdata, null);

        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        EditText ammount = myview.findViewById(id.amount_edt);
        EditText type = myview.findViewById(id.type_edt);
        EditText note = myview.findViewById(id.note_edt);

        Button btnSave = myview.findViewById(id.btnSave);
        Button btnCancel = myview.findViewById(id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmAmount = ammount.getText().toString().trim();
                String tmtype = type.getText().toString().trim();
                String tmnote = note.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmount)) {
                    ammount.setError("Required field...");
                    return;
                }

                int inamount = Integer.parseInt(tmAmount);

                if (TextUtils.isEmpty(tmtype)) {
                    type.setError("Required field....");
                    return;
                }
                if (TextUtils.isEmpty(tmnote)) {
                    note.setError("Required field...");
                    return;
                }

                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(inamount, tmtype, tmnote, id, mDate);
                mExpenseDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();
        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder viewHolder, int position, @NonNull Data model) {
                viewHolder.setIncomeType(model.getType());
                viewHolder.setIncomeAmmount(model.getAmmount());
                viewHolder.setIncomeDate(model.getDate());
            }


            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
        mRecyclerIncome.setAdapter(incomeAdapter);

        FirebaseRecyclerOptions<Data> optionss =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();
        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(optionss) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                holder.setExpenseType(model.getType());
                holder.setExpenseAmmount(model.getAmmount());
                holder.setExpenseDate(model.getDate());

            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
        mRecyclerExpense.setAdapter(expenseAdapter);
    }

     /*   FirebaseRecyclerAdapter<Data,ExpenseViewHolder>expenseAdapter=new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(
                Data.class,
                R.layout.dashboard_expense,
                ExpenseViewHolder.class,
                mExpenseDatabase
        ) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder viewHolder, int position, @NonNull Data model) {
                viewHolder.setExpenseType(model.getType());
                viewHolder.setExpenseAmmount(model.getAmmount());
                viewHolder.setExpenseDate(model.getDate());

            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };*/

//        mRecyclerExpense.setAdapter(incomeAdapter);


//    }

//    // for income data

        class IncomeViewHolder extends RecyclerView.ViewHolder {

            View mIncomeView;

            public IncomeViewHolder(@NonNull View itemView) {
                super(itemView);
                mIncomeView = itemView;

            }

            public void setIncomeType(String type) {
                TextView mtype = mIncomeView.findViewById(id.type_Income_ds);
                mtype.setText(type);

            }

            public void  setIncomeAmmount(int ammount) {
                TextView mAmmount = mIncomeView.findViewById(id.ammount_income_ds);
                String strAmmount = String.valueOf(ammount);
                mAmmount.setText(strAmmount);

            }

            public void setIncomeDate(String date) {
                TextView mDate = mIncomeView.findViewById(id.date_income_ds);
                mDate.setText(date);

            }


        }

        //for expense data...

         class ExpenseViewHolder extends RecyclerView.ViewHolder {

            View mExpenseView;

            public ExpenseViewHolder(@NonNull View itemView) {
                super(itemView);
                mExpenseView = itemView;
            }

            public void setExpenseType(String type) {
                TextView mtype = mExpenseView.findViewById(id.type_Expense_ds);
                mtype.setText(type);
            }

            public void setExpenseAmmount(int ammount) {
                TextView mAmmount = mExpenseView.findViewById(id.ammount_expense_ds);
                String strAmmount = String.valueOf(ammount);
                mAmmount.setText(strAmmount);
            }

            public void setExpenseDate(String date) {
                TextView mDate = mExpenseView.findViewById(id.date_expense_ds);
                mDate.setText(date);
            }
        }
}

