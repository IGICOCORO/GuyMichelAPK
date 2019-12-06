package bi.udev.guymichel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by KonstrIctor on 01/11/2019.
 */
class AdaptateurArchive extends RecyclerView.Adapter<AdaptateurArchive.ViewHolder> {

    private Context context;
    private ArrayList<Parole> paroles;

    public void setData(ArrayList<Parole> paroles){
        this.paroles=paroles;
    }

    public AdaptateurArchive(Context context, ArrayList<Parole> paroles) {
        this.context = context;
        this.paroles = paroles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_archive, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lbl_title_card_archive.setText(paroles.get(position).titre);
        holder.lbl_author_card_archive.setText(paroles.get(position).author);
        holder.lbl_date_card_archive.setText(paroles.get(position).date);
        Glide.with(context).load("http://"+Host.url+paroles.get(position).photo).into(holder.img_card_archive);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("titre", paroles.get(position).titre);
                intent.putExtra("author", paroles.get(position).author);
                intent.putExtra("date", paroles.get(position).date);
                intent.putExtra("photo", paroles.get(position).photo);
                intent.putExtra("audio", paroles.get(position).audio);
                intent.putExtra("id", paroles.get(position).id);
                intent.putExtra("online", "false");
                context.startActivity(intent);

            }
        });
        holder.btn_card_archive_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.error_icon)
                        .setTitle(R.string.suppression)
                        .setMessage(context.getString(R.string.confirmation_suppr)+paroles.get(position).titre)
                        .setPositiveButton(R.string.supprimer, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ArchiveModel(context).delete(paroles.get(position).id);
                                paroles.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.annuler, null)
                        .show();
            }
        });

        holder.btn_card_archive_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = new Host(context).getDirPath() + File.separator + paroles.get(position).audio;
                ApplicationInfo appinfo = context.getApplicationContext().getApplicationInfo();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("audio/mp3");
                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.envoyer_avec)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return paroles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lbl_title_card_archive, lbl_author_card_archive, lbl_date_card_archive;
        ImageView img_card_archive;
        Button btn_card_archive_share, btn_card_archive_delete;
        int downloadId;
        public View view;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_author_card_archive = (TextView) itemView.findViewById(R.id.lbl_author_card_archive);
            lbl_title_card_archive = (TextView) itemView.findViewById(R.id.lbl_title_card_archive);
            lbl_date_card_archive = (TextView) itemView.findViewById(R.id.lbl_date_card_archive);
            btn_card_archive_share = (Button) itemView.findViewById(R.id.btn_card_archive_share);
            btn_card_archive_delete = (Button) itemView.findViewById(R.id.btn_card_archive_delete);
            img_card_archive = (ImageView) itemView.findViewById(R.id.img_card_archive);
        }
    }
}