package me.yokeyword.sample.demo_flow.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import bean.NetAudioBean;
import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_flow.activity.ShowImageAndGifActivity;
import me.yokeyword.sample.demo_flow.adapter.NetAudioFragmentAdapter;
import me.yokeyword.sample.demo_flow.utils.CacheUtils;
import me.yokeyword.sample.demo_flow.utils.Constants;
import me.yokeyword.sample.demo_flow.utils.LogUtil;

/**
 * Created by xinpengfei on 2016/10/13.
 * 微信:18091383534
 * Function :
 */

public class NetAudioFragment extends BaseFragment {

    private TextView no_media;
    private ProgressBar progressbar;
    private MaterialRefreshLayout refresh;
    private ListView lv_audio;

    private List<NetAudioBean.ListEntity> datas;

    private NetAudioFragmentAdapter adapter;

    @Override
    public View initView() {
        LogUtil.e("网络音乐UI创建了");
        View view = View.inflate(context, R.layout.fragment_net_audio, null);

        no_media = (TextView) view.findViewById(R.id.no_media);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        lv_audio = (ListView) view.findViewById(R.id.lv_audio);

        //设置点击事件
        lv_audio.setOnItemClickListener(new MyOnItemClickListener());

        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {

            /**
             * 下拉刷新
             * @param materialRefreshLayout
             */
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                getDataFromNet();
            }

            /**
             * 加载更多
             * @param materialRefreshLayout
             */
            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                getMoreDataFromNet();
            }
        });

        return view;
    }

    /**
     * 加载更多
     */
    private void getMoreDataFromNet() {

        RequestParams request = new RequestParams(Constants.NET_AUDIO_URL);
        x.http().get(request, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                LogUtil.e("onSuccess==" + result);

                processMoreData(result);
                refresh.finishRefreshLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG", "网络音乐数据初始化了...");

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
            //有数据
            no_media.setVisibility(View.GONE);
            //设置适配器
            adapter = new NetAudioFragmentAdapter(context, datas);
            lv_audio.setAdapter(adapter);
        } else {
            //没有视频
            no_media.setVisibility(View.VISIBLE);
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

    /**
     * 解析和绑定数据
     *
     * @param result
     */
    private void processMoreData(String result) {

        datas.addAll(parsedJson(result));//添加到原来的集合中
        adapter.setData(datas);//把数据设置到适配器中
        adapter.notifyDataSetChanged();//刷新
    }

    private void getDataFromNet() {

        RequestParams request = new RequestParams(Constants.NET_AUDIO_URL);
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context, Constants.NET_AUDIO_URL, result);
                LogUtil.e("onSuccess==" + result);
                processData(result);
                refresh.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            NetAudioBean.ListEntity listBean = datas.get(position);
            if (listBean != null) {
                //3.传递视频列表
                Intent intent = new Intent(context, ShowImageAndGifActivity.class);
                if (listBean.getType().equals("gif")) {
                    String url = listBean.getGif().getImages().get(0);
                    intent.putExtra("url", url);
                    context.startActivity(intent);

                } else if (listBean.getType().equals("image")) {
                    String url = listBean.getImage().getBig().get(0);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            }
        }
    }


}
