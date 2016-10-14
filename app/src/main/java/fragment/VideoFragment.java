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

public class VideoFragment extends BaseFragment {

    private TextView tv_local_video;

    @Override
    public View initView() {

        LogUtil.e("本地视频UI创建了");
        View view = View.inflate(context, R.layout.fragment_video, null);
        tv_local_video = (TextView) view.findViewById(R.id.tv_local_video);

        return view;
    }
}
