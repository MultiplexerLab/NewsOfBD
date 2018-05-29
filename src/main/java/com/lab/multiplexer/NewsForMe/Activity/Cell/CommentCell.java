package com.lab.multiplexer.NewsForMe.Activity.Cell;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.lab.multiplexer.NewsForMe.Activity.Model.Comment;
import com.lab.multiplexer.NewsForMe.Activity.Webview_news;
import com.lab.multiplexer.NewsForMe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentCell extends SimpleCell<Comment, CommentCell.ViewHolder>
        implements Updatable<Comment> {

    private static final String KEY_TITLE = "KEY_TITLE";
    private boolean showHandle;
    public CommentCell(Comment item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.comment_item;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, final int position, final Context context, Object payload) {

        if (payload != null) {
            // payload from updateCell()
            if (payload instanceof Comment) {
                holder.name.setText(((Comment) payload).getName());
            }

            // payloads from updateCells()
            if (payload instanceof ArrayList) {
                List<Comment> payloads = ((ArrayList<Comment>) payload);
                holder.name.setText(payloads.get(position).getName());
            }
            // payload from addOrUpdate()
            if (payload instanceof Bundle) {
                Bundle bundle = ((Bundle) payload);
                for (String key : bundle.keySet()) {
                    if (KEY_TITLE.equals(key)) {
                        holder.name.setText(bundle.getString(key));
                    }
                }
            }
            return;
        }

        CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(getDpValue(context,8),getDpValue(context,2),getDpValue(context,8),0);
        holder.main_card.setLayoutParams(layoutParams);
        holder.name.setText(getItem().getName());
        holder.comment.setText(getItem().getComment());
        holder.time.setText(getItem().getTime());
        if(!getItem().getImage().equals("")){
            Picasso.with(context).load(getItem().getImage()).into(holder.dp);
        }
        holder.fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Webview_news.class);
                i.putExtra("link", "https://www.facebook.com/"+getItem().getFb_id());
                context.startActivity(i);
            }
        });

    }



    public int getDpValue(Context mContext,int yourdpmeasure){
        Resources r = mContext.getResources();
        final float scale = mContext.getResources().getDisplayMetrics().density;
        // convert the DP into pixel
        int pixel =  (int)(yourdpmeasure * scale + 0.5f);

        return pixel;
    }

    // Optional
    @Override
    protected void onUnbindViewHolder(ViewHolder holder) {
        // do your cleaning jobs here when the item view is recycled.
    }

    @Override
    protected long getItemId() {
        return getItem().getId();
    }

    public void setShowHandle(boolean showHandle) {
        this.showHandle = showHandle;
    }

    /**
     * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
     */
    @Override
    public boolean areContentsTheSame(Comment newItem) {
        return getItem().getName().equals(newItem.getName());
    }

    /**
     * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
     * and areContentsTheSame()  return false, then the cell need to be updated,
     * onBindViewHolder() will be called with this payload object.
     */
    @Override
    public Object getChangePayload(Comment newItem) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, newItem.getName());
        return bundle;
    }

    public static class ViewHolder extends SimpleViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.dp)
        ImageView dp;
        @BindView(R.id.comment)
        TextView comment;
        @BindView(R.id.main_card)
        CardView main_card;
        @BindView(R.id.fb_button)
        ImageButton fb_btn;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
