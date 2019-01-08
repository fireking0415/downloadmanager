package org.fireking.uugame.uis.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.fireking.uudownload.DownloadRequest;
import org.fireking.uudownload.SimpleDownloadListener;
import org.fireking.uudownload.UUDownloadManager;
import org.fireking.uugame.R;
import org.fireking.uugame.uis.activitys.SpeedDetailsActivity;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SpeedAdapter extends RecyclerView.Adapter<SpeedAdapter.MainViewHolder> {

    private List<DownloadRequest> downloadRequests;
    private Context mContext;

    public SpeedAdapter(List<DownloadRequest> requests, Context context) {
        downloadRequests = requests;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.download_item, viewGroup, false);
        return new MainViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder viewHolder, final int position) {

        final DownloadRequest request = downloadRequests.get(position);

        viewHolder.itemView.setTag(request.getDownloadUrl());

        viewHolder.title.setText(request.getStoreFileName());
        viewHolder.speed.setText("当前进度: " + request.getProgress() + "%");
        viewHolder.progress.setProgress(request.getProgress());
        viewHolder.tvSpeed.setText("");

        switch (request.getState()) {
            case DownloadRequest.STATE_NETWORK_ERROR:
            case DownloadRequest.STATE_FILE_ERROR:
            case DownloadRequest.STATE_FILE_LENGTH_ERROR:
                viewHolder.operator.setText("重试");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#F14400"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_PAUSE:
                viewHolder.operator.setText("继续");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#ef6c57"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_DOWNLOADING:
            case DownloadRequest.STATE_DOWNLOAD_PRE:
                viewHolder.operator.setText(request.getProgress() + "%");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#bfd8d5"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_DOWNLOAD_CONNECTING:
                viewHolder.operator.setText("连接中");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#bfd8d5"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_COMPLETED:
                viewHolder.operator.setText("加速");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#e61c5d"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_DOWNLOAD_INIT:
                viewHolder.operator.setText("等待");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#bfd8d5"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
            default:
                viewHolder.operator.setText("开始");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#f16821"));
                viewHolder.operator.setTextColor(Color.WHITE);
                break;
        }

        request.setDownloadListener(new SimpleDownloadListener() {

            @Override
            public void onNetworkError() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("重试");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#F14400"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onGetNetFileError() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("重试");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#F14400"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void updateProgress(int progress, String speed) {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText(progress + "%");
                    viewHolder.progress.setProgress(progress);
                    viewHolder.tvSpeed.setText("当前速度: " + speed);
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#9bbfab"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onBufferListener() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("连接中");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#bfd8d5"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onDownloadStart() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("下载中");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#9bbfab"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onPauseListener() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("继续");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#ef6c57"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onCompleteListener() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("加速");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#e61c5d"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onWaitingListener() {
                if (request.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(request);
                    viewHolder.operator.setText("等待");
                    viewHolder.operator.setBackgroundColor(Color.parseColor("#bfd8d5"));
                    viewHolder.operator.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNormalListener() {
                EventBus.getDefault().post(request);
                viewHolder.operator.setText("开始");
                viewHolder.operator.setBackgroundColor(Color.parseColor("#f16821"));
                viewHolder.operator.setTextColor(Color.WHITE);
            }
        });

        viewHolder.operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (request.getState()) {
                    case DownloadRequest.STATE_NETWORK_ERROR:
                    case DownloadRequest.STATE_FILE_ERROR:
                    case DownloadRequest.STATE_FILE_LENGTH_ERROR:
                        UUDownloadManager.getInstance().retryTask(request);
                        break;
                    case DownloadRequest.STATE_PAUSE:
                        UUDownloadManager.getInstance().resumeTask(request);
                        break;
                    case DownloadRequest.STATE_DOWNLOAD_PRE:
                    case DownloadRequest.STATE_DOWNLOAD_CONNECTING:
                    case DownloadRequest.STATE_DOWNLOADING:
                        UUDownloadManager.getInstance().pauseTask(request);
                        break;
                    case DownloadRequest.STATE_COMPLETED:
                        SpeedDetailsActivity.start(mContext);
                        break;
                    case DownloadRequest.STATE_DOWNLOAD_INIT:
                        UUDownloadManager.getInstance().removeTask(request);
                        break;
                    default:
                        UUDownloadManager.getInstance().enqueue(request);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadRequests == null ? 0 : downloadRequests.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        Button operator;
        TextView title;
        TextView speed;
        ProgressBar progress;
        TextView tvSpeed;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            operator = itemView.findViewById(R.id.operator);
            speed = itemView.findViewById(R.id.speed);
            progress = itemView.findViewById(R.id.progress);
            tvSpeed = itemView.findViewById(R.id.tvSpeed);
        }
    }
}
