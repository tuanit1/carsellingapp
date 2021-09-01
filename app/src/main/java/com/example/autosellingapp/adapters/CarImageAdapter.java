package com.example.autosellingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.MainActivity;
import com.example.autosellingapp.fragments.FragmentAdsDetail;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CarImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> arrayList_image = new ArrayList<>();
    private String video_type;
    private Methods methods;

    public CarImageAdapter(Context context, ArrayList<String> arrayList_image, String car_video, String video_type) {
        this.context = context;
        this.arrayList_image.addAll(arrayList_image);
        this.arrayList_image.add(0, car_video);
        this.video_type = video_type;
        this.methods = new Methods(context);
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

        View view;

        if(position == 0){
            if(video_type.equals("youtube")){
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_car_video, container, false);
                YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = methods.getVideoId(arrayList_image.get(position));
                        youTubePlayer.cueVideo(videoId, 0);
                    }
                });
            }else {
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_car_video_exoplayer, container, false);
                PlayerView playerView = view.findViewById(R.id.exo_player);
                ImageView btn_FullScreen = playerView.findViewById(R.id.img_full);

                Uri uri = Uri.parse(Constant.SERVER_URL + "videos/car_video/" + arrayList_image.get(0));

                SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(context).build();

                playerView.setPlayer(simpleExoPlayer);

                MediaItem mediaItem = MediaItem.fromUri(uri);

                simpleExoPlayer.setMediaItem(mediaItem);

                simpleExoPlayer.prepare();

                simpleExoPlayer.play();
            }


        }else {
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_car_image, container, false);

            ImageView iv_car_image = view.findViewById(R.id.iv_car_image);

            Picasso.get()
                    .load(arrayList_image.get(position))
                    .placeholder(R.drawable.placeholder_rec)
                    .into(iv_car_image);
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(arrayList_image != null){
            return arrayList_image.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }


}
