package fragment;

import android.view.View;
import android.widget.TextView;

import com.atguigu.exam_afterfirstproject.R;

import utils.LogUtil;

/**
 * Created by xinpengfei on 2016/10/13.
 * 微信:18091383534
 * Function :
 */

public class NetVideoFragment extends BaseFragment {

    private TextView tv_net_video;

    @Override
    public View initView() {

        LogUtil.e("本地音频UI创建了");
        View view = View.inflate(context, R.layout.fragment_net_video, null);
        tv_net_video = (TextView) view.findViewById(R.id.tv_net_video);

        return view;
    }
}
