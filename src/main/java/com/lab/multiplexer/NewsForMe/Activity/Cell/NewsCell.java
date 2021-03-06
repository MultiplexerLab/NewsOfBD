package com.lab.multiplexer.NewsForMe.Activity.Cell;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.lab.multiplexer.NewsForMe.Activity.Database.DatabaseHelper;
import com.lab.multiplexer.NewsForMe.Activity.Fullnews;
import com.lab.multiplexer.NewsForMe.Activity.Model.Category;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;
import com.lab.multiplexer.NewsForMe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class NewsCell extends SimpleCell<News, NewsCell.ViewHolder>
  implements Updatable<News> {

  private static final String KEY_TITLE = "KEY_TITLE";
  private boolean showHandle;
  SharedPreferences.Editor editor;
  SharedPreferences pref;
  ArrayList<News> list;
  DatabaseHelper db;
  public NewsCell(News item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.cell_news;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, final int position, final Context context, Object payload) {
    pref = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
    editor = pref.edit();
    db = new DatabaseHelper(context);
    if (payload != null) {
      // payload from updateCell()
      if (payload instanceof News) {
        holder.textView.setText(((News) payload).getTitle());
      }

      // payloads from updateCells()
      if (payload instanceof ArrayList) {
        List<News> payloads = ((ArrayList<News>) payload);
        holder.textView.setText(payloads.get(position).getTitle());
        list = new ArrayList<News>(payloads);
      }
      // payload from addOrUpdate()
      if (payload instanceof Bundle) {
        Bundle bundle = ((Bundle) payload);
        for (String key : bundle.keySet()) {
          if (KEY_TITLE.equals(key)) {
            holder.textView.setText(bundle.getString(key));
          }
        }
      }
      return;
    }
    Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/solaimanlipi.ttf");

    if(pref.getString("language","").equals("Bangla")){
      holder.textView.setTypeface(font);
      holder.full_textView.setTypeface(font);
    }
    if(position>0){
      CardView.LayoutParams layoutParams = new CardView.LayoutParams(
              CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
      layoutParams.setMargins(getDpValue(context,8),getDpValue(context,2),getDpValue(context,8),0);
      holder.main_card.setLayoutParams(layoutParams);
      holder.textView.setText(getItem().getTitle());
      holder.source.setText(getItem().getSource());
      holder.time.setText(getItem().getTime());
      holder.dragHandle.setVisibility(View.VISIBLE);
      if(!getItem().getImage().equals("")){
        Picasso.with(context).load(getItem().getImage()).error(R.drawable.no_image).into(holder.dragHandle);
      } else {
        Picasso.with(context).load(R.drawable.no_image).into(holder.dragHandle);
      }
      holder.parent_holder.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent i = new Intent(context, Fullnews.class);
          //i.putExtra("category", getItem().getCategoryName());
          i.putExtra("position",position);
          i.putExtra("from","Highlights");
          context.startActivity(i);
        }
      });
      holder.parent_holder.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          Category c = new Category(position, getItem().getCategoryName());
          News n = new News(getItem().getId(), getItem().getTitle(),getItem().getDescription(),getItem().getImage(),getItem().getSource(),
                  getItem().getPublish_time(),getItem().getLink(),c);
          saveNewsAlert(context,n);

          return true;
        }
      });

    } else {
      holder.parent_holder.setVisibility(View.GONE);
      holder.second_parent_holder.setVisibility(View.VISIBLE);
      holder.full_textView.setText(getItem().getTitle());
      holder.full_source.setText(getItem().getSource());
      holder.full_time.setText(getItem().getTime());
      holder.full_dragHandle.setVisibility(View.VISIBLE);
      if(!getItem().getImage().equals("")){
        Picasso.with(context).load(getItem().getImage()).error(R.drawable.no_image).into(holder.full_dragHandle);
      }else {
        Picasso.with(context).load(R.drawable.no_image).into(holder.dragHandle);
      }
      holder.second_parent_holder.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent i = new Intent(context, Fullnews.class);
          //i.putExtra("category", getItem().getCategoryName().toLowerCase());
          i.putExtra("position",position);
          i.putExtra("from","Highlights");
          context.startActivity(i);
        }
      });

      holder.second_parent_holder.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          Category c = new Category(position, getItem().getCategoryName());
          News n = new News(getItem().getId(), getItem().getTitle(),getItem().getDescription(),getItem().getImage(),getItem().getSource(),
                  getItem().getPublish_time(),getItem().getLink(),c);
          saveNewsAlert(context,n);

          return true;
        }
      });
    }

  }


  public void saveNewsAlert(Context c, final News n) {
    AlertDialog alertDialog = new AlertDialog.Builder(c).create();
    alertDialog.setTitle(R.string.app_name);
    alertDialog.setMessage("Are you sure you want to save this news ?");

    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        db.saveNews(n,pref.getString("language",""));
      }
    });


    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    alertDialog.show();
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
  public boolean areContentsTheSame(News newItem) {
    return getItem().getTitle().equals(newItem.getTitle());
  }

  /**
   * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
   * and areContentsTheSame()  return false, then the cell need to be updated,
   * onBindViewHolder() will be called with this payload object.
   */
  @Override
  public Object getChangePayload(News newItem) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, newItem.getTitle());
    return bundle;
  }

  public static class ViewHolder extends SimpleViewHolder {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.description)
    TextView source;
    @BindView(R.id.dragHandle)
    ImageView dragHandle;
    @BindView(R.id.parent_holder)
    LinearLayout parent_holder;
    @BindView(R.id.main_card)
    CardView main_card;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.full_textView)
    TextView full_textView;
    @BindView(R.id.full_source)
    TextView full_source;
    @BindView(R.id.full_dragHandle)
    ImageView full_dragHandle;
    @BindView(R.id.second_parent_holder)
    LinearLayout second_parent_holder;
    @BindView(R.id.full_time)
    TextView full_time;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
