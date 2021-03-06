package adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.exam_afterfirstproject.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.List;

import bean.NetAudioBean;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import utils.Utils;

/**
 * Created by xinpengfei on 2016/10/13.
 * 微信:18091383534
 * Function :网络音乐页面的适配器(分类型)
 */

public class NetAudioFragmentAdapter extends BaseAdapter {

    private Utils utils;
    //上下文
    private Context context;
    /**
     * 集合数据
     */
    private List<NetAudioBean.ListEntity> datas;

    /**
     * 视频类型:注意类型要从0开始
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;

    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;


    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;

    public NetAudioFragmentAdapter(Context context, List<NetAudioBean.ListEntity> datas) {
        this.context = context;
        this.datas = datas;
        utils = new Utils();
        Log.e("TAG", "datasize==" + datas.size());
    }

    /**
     * @return : 返回类型总数(共5种)
     */
    @Override
    public int getViewTypeCount() {
        return 5;
    }

    /**
     * 获取当前位置的Item是什么类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        int itemViewType = -1;
        //根据位置，从列表中得到一个数据对象
        NetAudioBean.ListEntity listEntity = datas.get(position);
        String type = listEntity.getType();//得到类型
        Log.e("TAG", "type===" + type);

        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;

        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;

        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;

        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;

        } else {
            itemViewType = TYPE_AD;//广播
        }
        return itemViewType;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //初始化item布局
        int itemViewType = getItemViewType(position);
        Log.e("TAG", "getView_position==" + position);
        ViewHolder viewHolder;

        if (convertView == null) {
            //创建viewHolder
            viewHolder = new ViewHolder();
            //根据不同的类型加载不同的布局
            convertView = initView(convertView, itemViewType, viewHolder);
            //初始化公共部分
            initCommonView(convertView, itemViewType, viewHolder);
            //设置tag
            convertView.setTag(viewHolder);

        } else {
            //getTag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.e("TAG", "bindData_position==" + position);
        //绑定数据
        bindData(position, itemViewType, viewHolder);

        return convertView;
    }

    /**
     * 将数据(特有)绑定到对应的布局中()
     *
     * @param position
     * @param itemViewType
     * @param viewHolder
     */
    private void bindData(int position, int itemViewType, ViewHolder viewHolder) {

        //根据位置得到数据，绑定数据
        NetAudioBean.ListEntity mediaItem = datas.get(position);

        switch (itemViewType) {
            case TYPE_VIDEO:
                bindData(viewHolder, mediaItem);
                //第一个参数是视频播放地址，第二个参数是显示封面的地址
                boolean setUp = viewHolder.jcv_videoplayer.setUp(
                        mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST, ""
                );
                //加载图片
                if (setUp) {
                    ImageLoader.getInstance().displayImage(mediaItem.getVideo().getThumbnail().get(0), viewHolder.jcv_videoplayer.thumbImageView);
                }
                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
                break;

            case TYPE_IMAGE://图片
                bindData(viewHolder, mediaItem);
                viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
                if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
                    Glide.with(context).load(mediaItem.getImage().getDownload_url().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
                }
                break;

            case TYPE_TEXT:
                bindData(viewHolder, mediaItem);
                break;
            case TYPE_GIF:
                bindData(viewHolder, mediaItem);
                if (mediaItem.getGif() != null && mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
                    Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);
                }
                break;
            case TYPE_AD://广告

                break;
        }
        //设置文本
        viewHolder.tv_context.setText(mediaItem.getText() + "_" + mediaItem.getType());
    }

    /**
     * 将公共数据绑定到布局中
     *
     * @param viewHolder
     * @param mediaItem
     */
    private void bindData(ViewHolder viewHolder, NetAudioBean.ListEntity mediaItem) {

        if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
            x.image().bind(viewHolder.iv_headpic, mediaItem.getU().getHeader().get(0));
        }
        if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
            viewHolder.tv_name.setText(mediaItem.getU().getName() + "");
        }

        viewHolder.tv_time_refresh.setText(mediaItem.getPasstime());

        //设置标签
        List<NetAudioBean.ListEntity.TagsEntity> tagsEntities = mediaItem.getTags();
        if (tagsEntities != null && tagsEntities.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < tagsEntities.size(); i++) {
                buffer.append(tagsEntities.get(i).getName() + " ");
            }
            viewHolder.tv_video_kind_text.setText(buffer.toString());
        }

        //设置点赞，踩,转发
        viewHolder.tv_shenhe_ding_number.setText(mediaItem.getUp());
        viewHolder.tv_shenhe_cai_number.setText(mediaItem.getDown() + "");
        viewHolder.tv_posts_number.setText(mediaItem.getForward() + "");
    }

    /**
     * 初始化公共部分的控件
     *
     * @param convertView
     * @param itemViewType
     * @param viewHolder
     */
    private void initCommonView(View convertView, int itemViewType, ViewHolder viewHolder) {
        switch (itemViewType) {
            case TYPE_VIDEO://视频
            case TYPE_IMAGE://图片
            case TYPE_TEXT://文字
            case TYPE_GIF://gif
                //加载除开广告部分的公共部分视图
                //user info
                viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
                viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);
                //bottom
                viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
                viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
                viewHolder.tv_shenhe_ding_number = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
                viewHolder.tv_shenhe_cai_number = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
                viewHolder.tv_posts_number = (TextView) convertView.findViewById(R.id.tv_posts_number);
                viewHolder.ll_download = (LinearLayout) convertView.findViewById(R.id.ll_download);

                break;
        }
        //中间公共部分 -所有的都有
        viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setData(List<NetAudioBean.ListEntity> datas) {
        this.datas = datas;
    }

    /**
     * 根据不同的类型初始化不同的view布局
     *
     * @param convertView
     * @param itemViewType
     * @param viewHolder
     * @return
     */
    private View initView(View convertView, int itemViewType, ViewHolder viewHolder) {

        switch (itemViewType) {

            case TYPE_VIDEO://视频
                convertView = View.inflate(context, R.layout.all_video_item, null);
                //在这里实例化特有的控件
                viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id.tv_play_nums);
                viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
                viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id.tv_commant_context);
                viewHolder.jcv_videoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
                break;

            case TYPE_IMAGE://图片
                convertView = View.inflate(context, R.layout.all_image_item, null);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;

            case TYPE_TEXT:
                convertView = View.inflate(context, R.layout.all_text_item, null);
                break;

            case TYPE_GIF:
                convertView = View.inflate(context, R.layout.all_gif_item, null);
                viewHolder.iv_image_gif = (ImageView) convertView.findViewById(R.id.iv_image_gif);
                break;

            case TYPE_AD:
                convertView = View.inflate(context, R.layout.all_ad_item, null);
                viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        //user_info
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;

        //bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_ding_number;
        TextView tv_shenhe_cai_number;
        TextView tv_posts_number;
        LinearLayout ll_download;

        //中间公共部分 -所有的都有
        TextView tv_context;

        //Video
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayerStandard jcv_videoplayer;

        //Image
        ImageView iv_image_icon;

        ImageView iv_image_gif;

        //软件推广
        Button btn_install;
    }
}
