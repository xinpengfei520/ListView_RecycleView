package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.exam_afterfirstproject.R;

/**
 * Created by xinpengfei on 2016/10/13.
 * 微信:18091383534
 * Function :自定义一个title
 */

public class Titlebar extends LinearLayout implements View.OnClickListener {

    private TextView textView;
    private RelativeLayout relativeLayout;
    private ImageView imageView;

    /**
     * 上下文
     */
    private Context context;

    public Titlebar(Context context) {
        this(context, null);
    }

    /**
     * 布局文件中使用必须要实现此构造方法，不然程序会崩溃！
     *
     * @param context
     * @param attrs
     */
    public Titlebar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 当布局加载完成的时候会调用此方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        textView = (TextView) getChildAt(1);
        relativeLayout = (RelativeLayout) getChildAt(2);
        imageView = (ImageView) getChildAt(3);

        //设置点击事件
        textView.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
//                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, SearchActivity.class);
//                context.startActivity(intent);

                break;
            case R.id.rl_game:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_history:
                Toast.makeText(context, "历史", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
