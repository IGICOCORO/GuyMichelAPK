package bi.udev.guymichel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KonstrIctor on 22/11/2019.
 */

class AdaptateurTemoignage extends RecyclerView.Adapter<AdaptateurTemoignage.ViewHolder> {

    private Context context;
    private ArrayList<Temoignage> temoignages;

    public void setData(ArrayList<Temoignage> temoignages){
        this.temoignages=temoignages;
    }

    public AdaptateurTemoignage(Context context, ArrayList<Temoignage> temoignages) {
        this.context = context;
        this.temoignages = temoignages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_temoignages, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lbl_title_card_testimony.setText(temoignages.get(position).titre);
        holder.lbl_author_card_testimony.setText(temoignages.get(position).author);
        holder.lbl_date_card_testimony.setText(temoignages.get(position).date);
        holder.lbl_categorie_testimony.setText(temoignages.get(position).categorie);
        Glide.with(context).load("http://"+Host.url+temoignages.get(position).photo).into(holder.img_card_testimony);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TemoignageActivity.class);
                intent.putExtra("titre", temoignages.get(position).titre);
                intent.putExtra("author", temoignages.get(position).author);
                intent.putExtra("date", temoignages.get(position).date);
                intent.putExtra("photo", temoignages.get(position).photo);
                intent.putExtra("slug", temoignages.get(position).slug);
                context.startActivity(intent);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return temoignages.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_title_card_testimony, lbl_author_card_testimony, lbl_date_card_testimony, lbl_categorie_testimony;
        ImageView img_card_testimony;
        public View view;
    
        public ViewHolder(final View itemView) {
            super(itemView);
            this.view = itemView;
            lbl_author_card_testimony = (TextView) itemView.findViewById(R.id.lbl_author_card_testimony);
            lbl_title_card_testimony = (TextView) itemView.findViewById(R.id.lbl_title_card_testimony);
            lbl_date_card_testimony = (TextView) itemView.findViewById(R.id.lbl_date_card_testimony);
            lbl_categorie_testimony = (TextView) itemView.findViewById(R.id.lbl_categorie_testimony);
            img_card_testimony = (ImageView) itemView.findViewById(R.id.img_card_testimony);
        }
    }
}