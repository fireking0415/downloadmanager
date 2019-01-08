package org.fireking.uugame.uis.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.fireking.uudownload.DownloadRequest;
import org.fireking.uudownload.SimpleDownloadListener;
import org.fireking.uudownload.UUDownloadManager;
import org.fireking.uugame.R;
import org.fireking.uugame.uis.activitys.SpeedDetailsActivity;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AllGameAdapter extends RecyclerView.Adapter<AllGameAdapter.AllGameViewHolder> {

    private List<DownloadRequest> requests;
    private Context mContext;

    public AllGameAdapter(Context context, List<DownloadRequest> downloadRequests) {
        this.requests = downloadRequests;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AllGameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_game_grid_item, viewGroup, false);
        return new AllGameViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllGameViewHolder viewHolder, int i) {
        final DownloadRequest downloadRequest = requests.get(i);

        viewHolder.itemView.setTag(downloadRequest.getDownloadUrl());

        viewHolder.tvName.setText(downloadRequest.getStoreFileName());

        switch (downloadRequest.getState()) {
            case DownloadRequest.STATE_NETWORK_ERROR:
            case DownloadRequest.STATE_FILE_ERROR:
            case DownloadRequest.STATE_FILE_LENGTH_ERROR:
                viewHolder.btnDownload.setText("重试");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#F14400"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_PAUSE:
                viewHolder.btnDownload.setText("继续");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#ef6c57"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_DOWNLOADING:
            case DownloadRequest.STATE_DOWNLOAD_PRE:
                viewHolder.btnDownload.setText(downloadRequest.getProgress() + "%");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#bfd8d5"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_DOWNLOAD_CONNECTING:
                viewHolder.btnDownload.setText("连接中");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#bfd8d5"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_COMPLETED:
                viewHolder.btnDownload.setText("加速");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#e61c5d"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
            case DownloadRequest.STATE_DOWNLOAD_INIT:
                viewHolder.btnDownload.setText("等待");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#bfd8d5"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
            default:
                viewHolder.btnDownload.setText("开始");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#f16821"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
                break;
        }

        downloadRequest.setDownloadListener(new SimpleDownloadListener() {

            @Override
            public void onNetworkError() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("重试");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#F14400"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onGetNetFileError() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("重试");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#F14400"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void updateProgress(int progress, String speed) {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText(progress + "%");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#9bbfab"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onBufferListener() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("连接中");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#bfd8d5"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onDownloadStart() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("下载中");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#9bbfab"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onPauseListener() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("继续");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#ef6c57"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onCompleteListener() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("加速");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#e61c5d"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onWaitingListener() {
                if (downloadRequest.getDownloadUrl().equals(viewHolder.itemView.getTag())) {
                    EventBus.getDefault().post(downloadRequest);
                    viewHolder.btnDownload.setText("等待");
                    viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#bfd8d5"));
                    viewHolder.btnDownload.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNormalListener() {
                EventBus.getDefault().post(downloadRequest);
                viewHolder.btnDownload.setText("开始");
                viewHolder.btnDownload.setBackgroundColor(Color.parseColor("#f16821"));
                viewHolder.btnDownload.setTextColor(Color.WHITE);
            }
        });


        viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (downloadRequest.getState()) {
                    case DownloadRequest.STATE_NETWORK_ERROR:
                    case DownloadRequest.STATE_FILE_ERROR:
                    case DownloadRequest.STATE_FILE_LENGTH_ERROR:
                        UUDownloadManager.getInstance().retryTask(downloadRequest);
                        break;
                    case DownloadRequest.STATE_PAUSE:
                        UUDownloadManager.getInstance().resumeTask(downloadRequest);
                        break;
                    case DownloadRequest.STATE_DOWNLOAD_PRE:
                    case DownloadRequest.STATE_DOWNLOAD_CONNECTING:
                    case DownloadRequest.STATE_DOWNLOADING:
                        UUDownloadManager.getInstance().pauseTask(downloadRequest);
                        break;
                    case DownloadRequest.STATE_COMPLETED:
                        SpeedDetailsActivity.start(mContext);
                        break;
                    case DownloadRequest.STATE_DOWNLOAD_INIT:
                        UUDownloadManager.getInstance().removeTask(downloadRequest);
                        break;
                    default:
                        UUDownloadManager.getInstance().enqueue(downloadRequest);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    public static class AllGameViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLogo;
        TextView tvName;
        Button btnDownload;

        public AllGameViewHolder(@NonNull View itemView) {
            super(itemView);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvName = itemView.findViewById(R.id.tvName);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }
    }
}
