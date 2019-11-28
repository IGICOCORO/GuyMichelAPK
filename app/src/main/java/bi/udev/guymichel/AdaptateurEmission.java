package bi.udev.guymichel;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KonstrIctor on 01/11/2019.
 */
class AdaptateurEmission extends RecyclerView.Adapter<AdaptateurEmission.ViewHolder> {

    private Context context;
    private ArrayList<Parole> paroles;

    public void setData(ArrayList<Parole> paroles){
        this.paroles=paroles;
    }

    public AdaptateurEmission(Context context, ArrayList<Parole> paroles) {
        this.context = context;
        this.paroles = paroles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_emission, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lbl_title_card_emission.setText(paroles.get(position).titre);
        holder.lbl_author_card_emmision.setText(paroles.get(position).author);
        holder.lbl_date_card_emmision.setText(paroles.get(position).date);
        Glide.with(context).load("http://"+Host.url+paroles.get(position).photo).into(holder.img_card_emission);

        final String audio_url = "http://"+Host.url+paroles.get(position).audio;
        final String fileName = audio_url.substring(audio_url.lastIndexOf('/')+1);

        if(new ArchiveModel(context).get(paroles.get(position).id) != null){
            holder.btn_card_emission_download.setVisibility(View.INVISIBLE);
        }else {
            if (Status.COMPLETED == PRDownloader.getStatus(holder.downloadId)) {
                holder.btn_card_emission_download.setVisibility(View.INVISIBLE);
            }

            holder.btn_card_emission_resume_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Status.RUNNING == PRDownloader.getStatus(holder.downloadId)) {
                        PRDownloader.pause(holder.downloadId);
                        return;
                    }

                    //                buttonOne.setEnabled(false);
                    //                progressBarOne.setIndeterminate(true);
                    //                progressBarOne.getIndeterminateDrawable().setColorFilter( Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

                    if (Status.PAUSED == PRDownloader.getStatus(holder.downloadId)) {
                        PRDownloader.resume(holder.downloadId);
                        return;
                    }
                }
            });

            holder.btn_card_emission_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.downloadId != 0) {
                        PRDownloader.cancel(holder.downloadId);
                        return;
                    }

                    holder.downloadId = PRDownloader.download(audio_url, new Host(context).getDirPath(), fileName)
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {
                                    holder.progressBar_download.setVisibility(View.VISIBLE);
                                    holder.btn_card_emission_resume_down.setVisibility(View.VISIBLE);
                                    holder.btn_card_emission_resume_down.setBackgroundResource(R.drawable.pause_icon);
                                    holder.btn_card_emission_download.setBackgroundResource(R.drawable.close_icon);
                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {
                                    holder.btn_card_emission_resume_down.setBackgroundResource(R.drawable.play_icon);
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    holder.downloadId = 0;
                                    holder.btn_card_emission_resume_down.setVisibility(View.INVISIBLE);
                                    holder.btn_card_emission_download.setBackgroundResource(R.drawable.download_icon);
                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                    long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                    holder.progressBar_download.setProgress((int) progressPercent);
                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    holder.progressBar_download.setVisibility(View.INVISIBLE);
                                    holder.btn_card_emission_resume_down.setVisibility(View.INVISIBLE);
                                    holder.btn_card_emission_download.setVisibility(View.INVISIBLE);
                                    Parole parole = paroles.get(position);
                                    parole.audio = fileName;
                                    new ArchiveModel(context).insert(parole);
                                }

                                @Override
                                public void onError(Error error) {
                                    holder.progressBar_download.setVisibility(View.INVISIBLE);
                                    Toast.makeText(context, error.getConnectionException().getMessage(), Toast.LENGTH_SHORT);
                                    holder.btn_card_emission_resume_down.setVisibility(View.INVISIBLE);
                                    holder.btn_card_emission_download.setBackgroundResource(R.drawable.download_icon);
                                    holder.downloadId = 0;

                                }
                            });
                }
            });
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("titre", paroles.get(position).titre);
                intent.putExtra("author", paroles.get(position).author);
                intent.putExtra("date", paroles.get(position).date);
                intent.putExtra("photo", paroles.get(position).photo);
                intent.putExtra("audio", paroles.get(position).audio);
                intent.putExtra("online", "true");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paroles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_title_card_emission, lbl_author_card_emmision, lbl_date_card_emmision;
        ImageView img_card_emission;
        Button btn_card_emission_download, btn_card_emission_resume_down;
        ProgressBar progressBar_download;
        int downloadId;
        public View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_author_card_emmision = (TextView) itemView.findViewById(R.id.lbl_author_card_emmision);
            lbl_title_card_emission = (TextView) itemView.findViewById(R.id.lbl_title_card_emission);
            lbl_date_card_emmision = (TextView) itemView.findViewById(R.id.lbl_date_card_emmision);
            btn_card_emission_download = (Button) itemView.findViewById(R.id.btn_card_emission_download);
            btn_card_emission_resume_down = (Button) itemView.findViewById(R.id.btn_card_emission_resume_down);
            img_card_emission = (ImageView) itemView.findViewById(R.id.img_card_emission);
            progressBar_download = (ProgressBar) itemView.findViewById(R.id.progressBar_download);
        }
    }
}