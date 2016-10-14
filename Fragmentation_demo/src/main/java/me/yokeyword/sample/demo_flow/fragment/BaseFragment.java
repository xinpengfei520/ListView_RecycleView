package me.yokeyword.sample.demo_flow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xinpengfei on 2016/10/13.
 * 微信:18091383534
 * Function :基类，公共类的Fragment,让子类去继承
 */

public abstract class BaseFragment extends Fragment {

    protected Context context;//protected子类可访问

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();//获取上下文
    }

    /**
     * 初始化view视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 要孩子一定要实现此方法，并且返回一个view，意思就是让孩子自己去实现
     *
     * @return
     */
    public abstract View initView();

    /**
     * 当Activity创建成功的时候，得到Fragment的视图，对视图进行数据的设置，联网请求
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 给视图绑定数据，或者联网请求数据并且绑定数据的时候就重写该方法
     */
    public void initData() {

    }
}
