package com.movtalent.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lib.common.util.DataInter;
import com.lib.common.util.tool.DateTools;
import com.lib.common.util.tool.StringUtil;
import com.movtalent.app.R;
import com.movtalent.app.db.HistoryDBhelper;
import com.movtalent.app.model.vo.CommonVideoVo;
import com.movtalent.app.view.OnlineDetailPageActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * Created by huangyong on 2018/2/11.
 */

public class OnlineHistoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<CommonVideoVo> datas;
    private int isMV;
    private final String time;
    private final Gson gson;
    private String seriIndex;


    public OnlineHistoryAdapter(Context context, ArrayList<CommonVideoVo> datas) {
        this.context = context;
        this.datas = datas;
        gson = new Gson();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
        Date ss = new Date();
        time = format0.format(ss.getTime());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_video, parent, false);
        return new HistoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String imgUrl = datas.get(position).getMovPoster();

        ((HistoryItemHolder) holder).movRemark.setText(datas.get(position).getMovRemark());

        if (TextUtils.isEmpty(datas.get(position).getMovRemark())) {
            ((HistoryItemHolder) holder).movRemark.setVisibility(View.INVISIBLE);
        } else {
            ((HistoryItemHolder) holder).movRemark.setVisibility(View.VISIBLE);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("?????????" + (TextUtils.isEmpty(datas.get(position).getMovArea()) ? "" : datas.get(position).getMovArea()) + "\n");
        builder.append("?????????" + datas.get(position).getMovDesc());

        ((HistoryItemHolder) holder).vodDesc.setText(builder.toString());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.tupian);
        Glide.with(context).load(imgUrl).transition(DrawableTransitionOptions.withCrossFade(300)).apply(requestOptions).into(((HistoryItemHolder) holder).itemPoster);

        ((HistoryItemHolder) holder).movName.setText(datas.get(position).getMovName());
        if (!TextUtils.isEmpty(datas.get(position).getMovUpdateTime())) {
            ((HistoryItemHolder) holder).vodYear.setText(StringUtil.getDateStringFromTimestamp(Long.parseLong(datas.get(position).getMovUpdateTime())));
            ((HistoryItemHolder) holder).vodYear.setVisibility(View.VISIBLE);
        }
        int playPosition = datas.get(position).getPlayPosition();

        int currentIndex = datas.get(position).getIndex();
        if (currentIndex > 0) {
            seriIndex = "???" + (currentIndex + 1) + "???";
        } else {
            seriIndex = "";
        }
        ((HistoryItemHolder) holder).vodPos.setText("???????????? " + seriIndex + DateTools.formatDuring(playPosition));
        ((HistoryItemHolder) holder).itemView.setOnClickListener(view -> {
            try {
                toDetailPage(datas.get(position).getMovId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        holder.itemView.setOnLongClickListener(v -> {

            new XPopup.Builder(v.getContext()).asConfirm("??????", "???????????????????????????", new OnConfirmListener() {
                @Override
                public void onConfirm() {

                    HistoryDBhelper.delete(datas.get(position).getMovId());
                    datas.remove(datas.get(position));
                    notifyItemRemoved(position);
                    holder.itemView.postDelayed(() -> notifyDataSetChanged(), 1000);

                }
            }).show();
            return true;
        });
    }

    private void toDetailPage(String vodId) {
        Intent intent = new Intent(context, OnlineDetailPageActivity.class);
        intent.putExtra(DataInter.KEY.VOD_ID, Integer.parseInt(vodId));
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }
}
