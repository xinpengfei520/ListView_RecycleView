package me.yokeyword.sample.demo_flow.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import adapter.RecycleViewFragmentAdapter;
import bean.NetAudioBean;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_flow.utils.CacheUtils;
import me.yokeyword.sample.demo_flow.utils.Constants;
import me.yokeyword.sample.demo_flow.utils.LogUtil;

/**
 * Created by xinpengfei on 2016/10/14.
 * 微信:18091383534
 * Function :RecycleView
 */

public class RecycleViewFragment extends BaseFragment {

    private RecyclerView recycleview;
    private ProgressBar progressbar;
    private TextView tv_nomedia;
    private MaterialRefreshLayout refresh;

    private List<NetAudioBean.ListEntity> datas;

    private RecycleViewFragmentAdapter myAdapter;

    @Override
    public View initView() {

        Log.e("TAG", "RecycleView视图（页面）初始化了...");
        View view = View.inflate(context, R.layout.fragment_recyclerview, null);

        recycleview = (RecyclerView) view.findViewById(R.id.recycleview);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);

        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {

            /*
             * 下拉刷新
             */
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                getDataFromNet();
            }

            /**
             * 加载更多
             */
            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                getMoreDataFromNet();
            }
        });

        return view;
    }

    /**
     * 下拉刷新
     */
    private void getDataFromNet() {
        RequestParams reques = new RequestParams(Constants.NET_AUDIO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                CacheUtils.putString(context, Constants.NET_AUDIO_URL, result);
                LogUtil.e("onSuccess==" + result);
                processData(result);
                refresh.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取更多
     */
    private void getMoreDataFromNet() {

        RequestParams reques = new RequestParams(Constants.NET_AUDIO_URL);
        x.http().get(reques, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("onSuccess==" + result);
                processMoreData(result);
                refresh.finishRefreshLoadMore();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 解析和绑定数据
     *
     * @param json
     */
    private void processMoreData(String json) {
        datas.addAll(parsedJson(json));
        myAdapter.setData(datas);
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void initData() {
        super.initData();

        Log.e("TAG", "网络视频数据初始化了...");

        String saveJson = CacheUtils.getString(context, Constants.NET_AUDIO_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        getDataFromNet();
    }

    /**
     * 解析和绑定数据
     *
     * @param json
     */
    private void processData(String json) {

        datas = parsedJson(json);

        if (datas != null && datas.size() > 0) {
            //有视频
            tv_nomedia.setVisibility(View.GONE);
            //设置适配器
            myAdapter = new RecycleViewFragmentAdapter(context, datas);
            recycleview.setAdapter(myAdapter);
            recycleview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(View view) {

                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {
                    if (JCVideoPlayerManager.popListener() != null) {
                        JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.popListener();
                        if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                            JCVideoPlayer.releaseAllVideos();
                        }
                    }
                }
            });

            recycleview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        } else {
            //没有视频
            tv_nomedia.setVisibility(View.VISIBLE);
        }

        progressbar.setVisibility(View.GONE);
    }

    /**
     * 手动解析json数据
     *
     * @param json
     * @return
     */
    private List<NetAudioBean.ListEntity> parsedJson(String json) {
        NetAudioBean netAudioBean = new Gson().fromJson(json, NetAudioBean.class);
        return netAudioBean.getList();
    }
}
