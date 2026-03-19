package com.dukenightshade.abowatch.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import com.dukenightshade.abowatch.ui.adapter.GroupingUtils;
import com.dukenightshade.abowatch.ui.adapter.HeaderItem;
import com.dukenightshade.abowatch.ui.adapter.ListItem;
import com.dukenightshade.abowatch.ui.adapter.OnSubscriptionActionListener;
import com.dukenightshade.abowatch.ui.adapter.SubscriptionDiffCallback;
import com.dukenightshade.abowatch.ui.adapter.SubscriptionItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter für die RecyclerView der Abo-Liste.
 * Unterstützt gruppierte Darstellung nach Kategorie sowie einen Edit-Modus.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class SubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ====================================
    // Instance Variables
    // ====================================

    private List<ListItem> items = new ArrayList<>();
    private boolean editModeActive = false;
    private OnSubscriptionActionListener listener;

    // ====================================
    // Getter Methods
    // ====================================

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    public void setSubscriptions(List<Subscription> subscriptions) {
        List<ListItem> newItems = GroupingUtils.groupByCategory(subscriptions);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new SubscriptionDiffCallback(this.items, newItems)
        );
        this.items = newItems;
        diffResult.dispatchUpdatesTo(this);
    }


    public void setEditMode(boolean active) {
        this.editModeActive = active;
        notifyItemRangeChanged(0, items.size());
    }

    public void setOnSubscriptionActionListener(OnSubscriptionActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ListItem.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_category_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view = inflater.inflate(R.layout.item_subscription, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderItem header = (HeaderItem) items.get(position);
            ((HeaderViewHolder) holder).bind(header);
        } else if (holder instanceof SubscriptionViewHolder) {
            SubscriptionItem subItem = (SubscriptionItem) items.get(position);
            ((SubscriptionViewHolder) holder).bind(subItem.getSubscription(), editModeActive, listener);
        }
    }

    // ====================================
    // Inner Classes: ViewHolders
    // ====================================

    /**
     * ViewHolder für Kategorie-Header.
     */
    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCategoryName;
        private final TextView tvCategoryTotal;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryTotal = itemView.findViewById(R.id.tvCategoryTotal);
        }

        void bind(HeaderItem header) {
            tvCategoryName.setText(header.getCategoryName());
            tvCategoryTotal.setText(String.format(
                    Locale.GERMANY, "%.2f € / Monat", header.getCategoryTotal()
            ));
        }
    }

    /**
     * ViewHolder für einzelne Abo-Einträge.
     */
    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvPrice;
        private final TextView tvCategory;
        private final ImageView ivIcon;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSubscriptionName);
            tvPrice = itemView.findViewById(R.id.tvSubscriptionPrice);
            tvCategory = itemView.findViewById(R.id.tvSubscriptionCategory);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(Subscription subscription, boolean editModeActive,
                  OnSubscriptionActionListener listener) {

            tvName.setText(subscription.getName());
            tvCategory.setText(subscription.getCategory());
            tvPrice.setText(String.format(
                    Locale.GERMANY, "%.2f €", subscription.getPrice()
            ));

            // Edit-Buttons nur im Edit-Modus sichtbar
            int visibility = editModeActive ? View.VISIBLE : View.GONE;
            btnEdit.setVisibility(visibility);
            btnDelete.setVisibility(visibility);

            // Icon-Logik
            if ("Streaming".equalsIgnoreCase(subscription.getCategory())) {
                ivIcon.setImageResource(R.drawable.ic_streaming);
            } else if ("Fitness".equalsIgnoreCase(subscription.getCategory())) {
                ivIcon.setImageResource(R.drawable.ic_fitness);
            } else {
                ivIcon.setImageResource(R.drawable.ic_music);
            }

            // Callbacks
            btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(subscription);
            });
            btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(subscription);
            });
        }
    }
}
