package com.atguigu.exam_afterfirstproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import java.util.ArrayList;

import fragment.AudioFragment;
import fragment.BaseFragment;
import fragment.NetAudioFragment;
import fragment.NetVideoFragment;
import fragment.RecycleViewFragment;
import fragment.VideoFragment;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;
    /**
     * 用于存储所有的Fragment
     */
    private ArrayList<BaseFragment> fragments;

    /**
     * 用于指明对应的Fragment的位置
     */
    private int position;

    private Fragment content;//上一个页面的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        initFragment();

        /**
         * 设置RadioButton改变的监听
         */
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //进入主页默认选中本地视频
        rg_main.check(R.id.rb_local_video);
    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_net_video:
                    position = 1;
                    break;
                case R.id.rb_local_audio:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
                case R.id.rb_recycleview:
                    position = 4;
                    break;
            }

            Fragment toFragment = getFragment();
            switchFragment(content, toFragment);
        }
    }

    /**
     * 切换Fragment
     *
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {

        //当前显示的Fragment和要去显示Fragment不同的时候才去切换
        if (toFragment != content) {
            if (toFragment != null) {
                //得到Fragment的事务
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!toFragment.isAdded()) {
                    //隐藏之前显示content
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    //添加toFragment
                    transaction.add(R.id.fl_main_container, toFragment).commit();
                } else {
                    //隐藏之前显示的content
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    //显示toFragment
                    transaction.show(toFragment).commit();
                }

                content = toFragment;//切换完之后更新(重新记录)当前的Fragment
            }
        }
    }

    /**
     * 得到将要切换的Fragment
     *
     * @return
     */
    private Fragment getFragment() {
        return fragments.get(position);
    }

    /**
     * 初始化Fragment:将Fragment的实例添加到集合中
     */
    private void initFragment() {

        fragments = new ArrayList<>();
        fragments.add(new VideoFragment());
        fragments.add(new NetVideoFragment());
        fragments.add(new AudioFragment());
        fragments.add(new NetAudioFragment());
        fragments.add(new RecycleViewFragment());

    }
}
