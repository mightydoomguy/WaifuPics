package ru.rmdm.waifupics.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.rmdm.waifupics.R;
import ru.rmdm.waifupics.listener.OnImageItemLongClickListener;
import ru.rmdm.waifupics.model.WaifuImage;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<WaifuImage> listItems;
    private OnImageItemLongClickListener longClickListener;

    public void setLongClickListener(OnImageItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setListItems(List<WaifuImage> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecyclerViewHolder.create(parent);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        WaifuImage item = listItems.get(position);
        Glide.with(holder.image)
                .load(listItems.get(position).getUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(13)))
                .placeholder(R.drawable.update)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .into(holder.image);

        holder.itemView.setOnLongClickListener( v-> {
            if (longClickListener != null){
                longClickListener.onImageLongClick(item);
                return true;
            } else
                Log.e("RecyclerViewAdapter", "LongClickListener is null!");
                return false;
        });
    }

    @Override
    public int getItemCount() {
        if (listItems == null){
            return 0;
        }
        else
          return  listItems.size();
    }

}
