package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.x;

import java.util.List;

import bean.NetAudioBean;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_flow.activity.ShowImageAndGifActivity;
import utils.Utils;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by xinpengfei on 2016/10/14.
 * 微信:18091383534
 * Function :RecycleViewFragmentAdapter(RecycleView的适配器)
 */

public class RecycleViewFragmentAdapter extends RecyclerView.Adapter<RecycleViewFragmentAdapter.ViewHolder> {

    /**
     * 上下文
     */
    private Context context;

    /**
     * 集合数据
     */
    private List<NetAudioBean.ListEntity> datas;

    private Utils utils;//工具

    /**
     * 视频类型
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

    /**
     * 构造器(用于初始化适配器,同时将集合数据接收并保存)
     *
     * @param context
     * @param datas
     */
    public RecycleViewFragmentAdapter(Context context, List<NetAudioBean.ListEntity> datas) {
        this.context = context;
        this.datas = datas;
        utils = new Utils();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = initView(viewType);
        return new ViewHolder(itemView, viewType);
    }

    /**
     * 初始化view:根据不同的类型加载不同的布局
     *
     * @param viewType
     * @return
     */
    private View initView(int viewType) {

        View convertView = null;

        switch (viewType) {

            case TYPE_VIDEO://视频
                convertView = View.inflate(context, R.layout.all_video_item, null);
                break;

            case TYPE_IMAGE://图片
                convertView = View.inflate(context, R.layout.all_image_item, null);
                break;

            case TYPE_TEXT://文字
                convertView = View.inflate(context, R.layout.all_text_item, null);
                break;

            case TYPE_GIF://gif图片
                convertView = View.inflate(context, R.layout.all_gif_item, null);
                break;

            case TYPE_AD://软件广告
                convertView = View.inflate(context, R.layout.all_ad_item, null);
                break;
        }

        return convertView;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /**
         * 参数一：item的位置; 参数二：获取对应位置的类型; 参数三：holder
         */
        bindData(position, getItemViewType(position), holder);
    }

    /**
     * 绑定数据:根据不同的类型绑定不同的数据
     *
     * @param position
     * @param itemViewType
     * @param holder
     */
    private void bindData(int position, int itemViewType, ViewHolder holder) {

        //根据位置得到对应数据,绑定数据
        NetAudioBean.ListEntity mediaItem = datas.get(position);

        switch (itemViewType) {

            case TYPE_VIDEO://视频
                bindData(holder, mediaItem);
                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三个参数是标题
                boolean setUp = holder.jcv_videoplayer.setUp(
                        mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "");
                //加载图片：将得到图片使用imageload加载图片，加载到viewholder的thumbImageView
                if (setUp) {
                    ImageLoader.getInstance().displayImage(mediaItem.getVideo().getThumbnail().get(0),
                            holder.jcv_videoplayer.thumbImageView);
                }

                //获取播放次数，并设置到viewholder中
                holder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                //设置视频的总时长
                holder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
                break;

            case TYPE_IMAGE://图片
                bindData(holder, mediaItem);
                holder.iv_image_icon.setImageResource(R.drawable.bg_item);

                if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
                    //使用Glide解析图片，并设置进入(要考虑加载失败时的默认图片)
                    Glide.with(context).load(mediaItem.getImage().getDownload_url().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_image_icon);
                }
                break;

            case TYPE_TEXT://文字
                bindData(holder, mediaItem);
                break;

            case TYPE_GIF://gif图片
                bindData(holder, mediaItem);

                if (mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
                    Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_image_gif);
                }
                break;

            case TYPE_AD://广告
                break;
        }
        //设置文本 + 数据类型
        holder.tv_context.setText(mediaItem.getText() + "_" + mediaItem.getType());
    }

    /**
     * 绑定数据
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

        //设置tag标签
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
     * 获取对应位置item的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        int itemViewType = -1;//默认

        //根据位置从列表中得到一个数据对象
        NetAudioBean.ListEntity listEntity = datas.get(position);
        String type = listEntity.getType();//得到listEntity对象的类型
        Log.e(TAG, "type===" + type);

        //判断它是哪种类型并返回
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
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setData(List<NetAudioBean.ListEntity> datas) {
        this.datas = datas;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

        //中间公共部分的文本--> 所有数据类型的都有
        TextView tv_context;

        //Video
        TextView tv_play_nums;
        TextView tv_video_duration;//视频总时长
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayerStandard jcv_videoplayer;//节操播放器对象

        //Image
        ImageView iv_image_icon;

        //Gif
        ImageView iv_image_gif;

        //软件推广
        Button btn_install;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            //根据不同的类型实例化特有的控件对象
            switch (viewType) {

                case TYPE_VIDEO://视频
                    //在这里实例化特有的
                    tv_play_nums = (TextView) itemView.findViewById(R.id.tv_play_nums);
                    tv_video_duration = (TextView) itemView.findViewById(R.id.tv_video_duration);
                    iv_commant = (ImageView) itemView.findViewById(R.id.iv_commant);
                    tv_commant_context = (TextView) itemView.findViewById(R.id.tv_commant_context);
                    jcv_videoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.jcv_videoplayer);
                    break;

                case TYPE_IMAGE://图片
                    iv_image_icon = (ImageView) itemView.findViewById(R.id.iv_image_icon);
                    break;

                case TYPE_TEXT://文字
                    break;

                case TYPE_GIF://gif
                    iv_image_gif = (ImageView) itemView.findViewById(R.id.iv_image_gif);
                    break;

                case TYPE_AD://软件广告
                    btn_install = (Button) itemView.findViewById(R.id.btn_install);
                    iv_image_icon = (ImageView) itemView.findViewById(R.id.iv_image_icon);
                    break;
            }

            switch (viewType) {
                case TYPE_VIDEO://视频
                case TYPE_IMAGE://图片
                case TYPE_TEXT://文字
                case TYPE_GIF://gif
                    //加载除广告部分的公共部分视图
                    //user info
                    iv_headpic = (ImageView) itemView.findViewById(R.id.iv_headpic);
                    tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    tv_time_refresh = (TextView) itemView.findViewById(R.id.tv_time_refresh);
                    iv_right_more = (ImageView) itemView.findViewById(R.id.iv_right_more);
                    //bottom
                    iv_video_kind = (ImageView) itemView.findViewById(R.id.iv_video_kind);
                    tv_video_kind_text = (TextView) itemView.findViewById(R.id.tv_video_kind_text);
                    tv_shenhe_ding_number = (TextView) itemView.findViewById(R.id.tv_shenhe_ding_number);
                    tv_shenhe_cai_number = (TextView) itemView.findViewById(R.id.tv_shenhe_cai_number);
                    tv_posts_number = (TextView) itemView.findViewById(R.id.tv_posts_number);
                    ll_download = (LinearLayout) itemView.findViewById(R.id.ll_download);

                    break;
            }

            //中间公共部分--> 所有的类型都有
            tv_context = (TextView) itemView.findViewById(R.id.tv_context);

            //设置item的点击事件
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    NetAudioBean.ListEntity listEntity = datas.get(getLayoutPosition());

                    if (listEntity != null) {
                        //传递数据
                        Intent intent = new Intent(context, ShowImageAndGifActivity.class);

                        //如果是图片就在ShowImageAndGifActivity中显示(使用photoview)
                        if (listEntity.getType().equals("gif")) {
                            String url = listEntity.getGif().getImages().get(0);
                            intent.putExtra("url", url);
                            context.startActivity(intent);

                            //如果是gif图片就在ShowImageAndGifActivity中显示(使用photoview)
                        } else if (listEntity.getType().equals("image")) {
                            String url = listEntity.getImage().getBig().get(0);
                            intent.putExtra("url", url);
                            context.startActivity(intent);
                        }
                    }
                }
            });

        }
    }
}
