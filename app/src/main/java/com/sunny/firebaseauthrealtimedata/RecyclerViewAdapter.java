package com.sunny.firebaseauthrealtimedata;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    // for RecyclerView set variable for Listener add
    private OnItemClickListener listener;

    // agula likhte hbe
    private Context context;
    private List<Upload> uploadList; // Upload class e get-set kora theke data nea kaj

    // create kore nebo constructor
    public RecyclerViewAdapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    } // end likha

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_item_layout, parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // data find korar pore data set korbo
        Upload upload = uploadList.get(position); //Data gulo pete upload class ar help nebo
        holder.textView.setText(upload.getImageName());
        Picasso.with(context)
                .load(upload.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploadList.size(); // ata likhte hbe
    }

    // Method use for every view handle / view hold
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        // variable declare korte hbe
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // itemView ar sahajje data gula Find korte hbe
            imageView = itemView.findViewById(R.id.idCardimageView);
            textView = itemView.findViewById(R.id.idCardtextView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);


        }

        @Override
        public void onClick(View view) {
            if (listener!=null){
                int position = getAdapterPosition();

                if (position!=RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Choose an Option");
            MenuItem delete = contextMenu.add(Menu.NONE, 1 ,1 , "delete");
            MenuItem update = contextMenu.add(Menu.NONE, 2 ,2 , "Update");

            delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (listener!=null){
                        int position = getAdapterPosition();

                        if (position!=RecyclerView.NO_POSITION){
                            listener.onDelete(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
            update.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (listener!=null){
                        int position = getAdapterPosition();

                        if (position!=RecyclerView.NO_POSITION){
                            listener.onUpdate(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    //RecyclerView te onclickListener create start

    // Defining Method
    public interface OnItemClickListener {
       void onItemClick(int position);

       void onDelete(int position);
       void onUpdate(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){ //Parameter hisebe Interface pass korte hobe seta upore create kore nete hbe
        this.listener = listener;
    }
}
