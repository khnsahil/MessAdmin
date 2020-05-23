package com.example.messadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messadmin.models.MenuItem;

import java.util.List;

public class MyMenuRvAdapter extends RecyclerView.Adapter<MyMenuRvAdapter.ViewHolder> {

    private static final String TAG = "MyRvAdapter";

    private List<MenuItem> menuList;
    private Context mContext;
    private IAdapterClicked iAdapterClicked;

    public void setiAdapterClicked(IAdapterClicked iAdapterClicked) {
        this.iAdapterClicked = iAdapterClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Name,Price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.tv_menu_item_name);
            Price = itemView.findViewById(R.id.tv_menu_item_price);



            Log.d(TAG, "ViewHolder: Constructor Ok");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iAdapterClicked != null) {
                        iAdapterClicked.onPostionTap(getAdapterPosition());
                    }
                }
            });

        }
    }

    public MyMenuRvAdapter(List<MenuItem> menuItems, Context context) {
        this.menuList = menuItems;
        this.mContext = context;

        Log.d(TAG, "MyRvAdapter: Constructor ok");
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: aaa");
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final MenuItem rest = menuList.get(i);
        viewHolder.Name.setText(rest.getName());
        viewHolder.Price.setText(rest.getPrice().toString());

        Log.d(TAG, "onBindViewHolder: views set");

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    //8602 606 409

}