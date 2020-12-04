package com.example.to_doapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ListHolder>{
    public interface OnItemClickListener{
        void onItemClick(int postion);
    }

    private ArrayList<String> mList;
    private OnItemClickListener mListener;
    private Context mContext;
    private View mActivity;


    private String mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ListAdapter(ArrayList<String> list, Context context, View view){
        this.mContext = context;
        this.mList = list;
        this.mActivity = view;
    }

    @NonNull
    @Override
    public ListAdapter.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listholder_layoout, parent, false);
        return new ListHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListHolder holder, int position) {
        String lTitle = mList.get(position);
        holder.mTextView.setText(lTitle);

        holder.mTextView.setOnClickListener(view -> mListener.onItemClick(position));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = mList.get(position);
        mRecentlyDeletedItemPosition = position;
        mList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    public Context getContext(){
        return mContext;
    }

    private void showUndoSnackbar() {
        View view = mActivity;
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        mList.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    // View holder
    class ListHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public ListHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            this.mTextView = itemView.findViewById(R.id.list_title);

            itemView.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
