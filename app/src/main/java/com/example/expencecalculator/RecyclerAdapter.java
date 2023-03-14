//package com.example.expencecalculator;
//
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//
//
//public class RecyclerAdapter extends FirebaseRecyclerAdapter {
//    private DashBoardFragment.IncomeViewHolder incomeViewHolder;
//    private DashBoardFragment.ExpenseViewHolder expenseViewHolder;
//
//    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {
//       // expenseViewHolder.setIncomeType(model.getType());
//        //holder.setIncomeAmmount(model.getAmmount());
//        //holder.setIncomeDate(model.getDate());
//
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        //viewHolder.setExpenseType(model.getType());
//        //viewHolder.setExpenseAmmount(model.getAmmount());
//        //viewHolder.setExpenseDate(model.getDate());
//        return null;
//    }
//}
