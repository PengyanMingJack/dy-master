package com.movtalent.app.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.movtalent.app.R;
import com.movtalent.app.adapter.BannerEntity;
import com.movtalent.app.adapter.BannerEntityViewBinder;
import com.movtalent.app.adapter.SubjectEntity;
import com.movtalent.app.adapter.SubjectEntityViewBinder;
import com.movtalent.app.model.dto.TopicRDto;
import com.movtalent.app.model.vo.CommonVideoVo;
import com.movtalent.app.presenter.TopicPresenter;
import com.movtalent.app.presenter.iview.ITopicView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author huangyong
 * createTime 2019-09-11
 */
public class TopicTabFragment extends Fragment implements ITopicView {


    @BindView(R.id.backup)
    ImageView backup;
    @BindView(R.id.center_tv)
    TextView centerTv;
    @BindView(R.id.right_view)
    FrameLayout rightView;
    @BindView(R.id.home_rv)
    RecyclerView homeRv;
    Unbinder unbinder;
    private TopicPresenter topicPresenter;
    private int index;
    private MultiTypeAdapter multiTypeAdapter;
    @VisibleForTesting
    List<Object> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_topic_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        backup.setVisibility(View.GONE);
        centerTv.setText("????????????");

        topicPresenter = new TopicPresenter(this);
        index = 1;
        topicPresenter.getTopicRList(index, 18);
        items = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        homeRv.setLayoutManager(linearLayoutManager);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(SubjectEntity.class, new SubjectEntityViewBinder());

        homeRv.setAdapter(multiTypeAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDone(TopicRDto dto) {
        for (TopicRDto.DataBean dataInfo : dto.getData()) {
            SubjectEntity entity = new SubjectEntity(dataInfo.getTopic_name(), dataInfo.getTopic_pic_slide(), Integer.parseInt(dataInfo.getTopic_id()),dataInfo.getTopic_sub());
            items.add(entity);
        }
        multiTypeAdapter.setItems(items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(TopicRDto data) {
        for (TopicRDto.DataBean dataInfo : data.getData()) {
            SubjectEntity entity = new SubjectEntity(dataInfo.getTopic_name(), dataInfo.getTopic_pic_slide(), Integer.parseInt(dataInfo.getTopic_id()),dataInfo.getTopic_sub());
            items.add(entity);
        }
        multiTypeAdapter.setItems(items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(ArrayList<CommonVideoVo> from) {

    }

    @Override
    public void loadDone(ArrayList<CommonVideoVo> from) {

    }

    @Override
    public void loadError() {

    }

    @Override
    public void loadEmpty() {

    }
}
