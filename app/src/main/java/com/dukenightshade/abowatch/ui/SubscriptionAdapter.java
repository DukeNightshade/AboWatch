package com.dukenightshade.abowatch.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private List<Subscription> subscriptionList = new ArrayList<>();

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptionList = subscriptions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subscription, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        Subscription current = subscriptionList.get(position);
        holder.tvName.setText(current.getName());
        holder.tvCategory.setText(current.getCategory());
        holder.tvPrice.setText(String.format(Locale.GERMANY, "%.2f €", current.getPrice()));

        // Icon Logik (Beispiel)
        if ("Streaming".equalsIgnoreCase(current.getCategory())) {
            holder.ivIcon.setImageResource(R.drawable.ic_streaming);
        } else if ("Fitness".equalsIgnoreCase(current.getCategory())) {
            holder.ivIcon.setImageResource(R.drawable.ic_fitness);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_music);
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPrice;
        TextView tvCategory;
        ImageView ivIcon;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSubscriptionName);
            tvPrice = itemView.findViewById(R.id.tvSubscriptionPrice);
            tvCategory = itemView.findViewById(R.id.tvSubscriptionCategory);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
        }
    }
}