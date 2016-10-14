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

public class AudioFragment extends BaseFragment {

    private TextView tv_local_audio;

    @Override
    public View initView() {
        LogUtil.e("本地音频UI创建了");
        View view = View.inflate(context, R.layout.fragment_audio, null);
        tv_local_audio = (TextView) view.findViewById(R.id.tv_local_audio);

        return view;
    }
}
