package com.lab.multiplexer.NewsForMe.Activity.Cell;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.lab.multiplexer.NewsForMe.Activity.Model.Newspaper__model;
import com.lab.multiplexer.NewsForMe.Activity.NewsPaperContent;
import com.lab.multiplexer.NewsForMe.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewspaperCell extends SimpleCell<Newspaper__model, NewspaperCell.ViewHolder>
        implements Updatable<Newspaper__model> {

    private static final String KEY_TITLE = "KEY_TITLE";
    private boolean showHandle;
    public NewspaperCell(Newspaper__model item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.newspaper_item;
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
            if (payload instanceof Newspaper__model) {
                holder.txt_newspaper_name.setText(((Newspaper__model) payload).getNewspaper_name());
            }

            // payloads from updateCells()
            if (payload instanceof ArrayList) {
                List<Newspaper__model> payloads = ((ArrayList<Newspaper__model>) payload);
                holder.txt_newspaper_name.setText(payloads.get(position).getNewspaper_name());
            }
            // payload from addOrUpdate()
            if (payload instanceof Bundle) {
                Bundle bundle = ((Bundle) payload);
                for (String key : bundle.keySet()) {
                    if (KEY_TITLE.equals(key)) {
                        holder.txt_newspaper_name.setText(bundle.getString(key));
                    }
                }
            }
            return;
        }
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/solaimanlipi.ttf");
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(getDpValue(context,8),getDpValue(context,2),getDpValue(context,8),0);
        holder.main_card.setLayoutParams(layoutParams);
        holder.txt_newspaper_name.setText(getItem().getNewspaper_name());
        holder.txt_newspaper_name.setTypeface(font);
        holder.txt_newspaper_type.setText( "("+getItem().getNewspaper_type()+")");

        if(getItem().getNewspaper_type().equals("Bangla")){
            //Picasso.with(context).load(R.drawable.ic_bangla).into(holder.logo_tag);
            holder.txt_logo.setText("বাংলা");
            holder.txt_logo.setTypeface(font);
        } else {
            //Picasso.with(context).load(R.drawable.ic_english).into(holder.logo_tag);
            holder.txt_logo.setText("English");
        }

        holder.main_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NewsPaperContent.class);
                i.putExtra("language",getItem().getNewspaper_type());
                i.putExtra("newspaper",getItem().getNewspaper_name());
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
    public boolean areContentsTheSame(Newspaper__model newItem) {
        return getItem().getNewspaper_name().equals(newItem.getNewspaper_name());
    }

    /**
     * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
     * and areContentsTheSame()  return false, then the cell need to be updated,
     * onBindViewHolder() will be called with this payload object.
     */
    @Override
    public Object getChangePayload(Newspaper__model newItem) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, newItem.getNewspaper_name());
        return bundle;
    }

    public static class ViewHolder extends SimpleViewHolder {
        @BindView(R.id.txt_newspaper_name)
        TextView txt_newspaper_name;
        @BindView(R.id.txt_newspaper_type)
        TextView txt_newspaper_type;
        @BindView(R.id.logo_txt)
        TextView txt_logo;
        @BindView(R.id.main_card)
        CardView main_card;
        @BindView(R.id.logo_tag)
        ImageView logo_tag;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
