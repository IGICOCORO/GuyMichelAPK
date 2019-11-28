package bi.udev.guymichel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by KonstrIctor on 15/11/2019.
 */

public class ArchiveModel extends SQLiteOpenHelper {

    private static final String DB_NAME = "Archives.db";
    private static final int DB_VERSION = 1;
    Context context;

    public ArchiveModel(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command = "CREATE TABLE archives(" +
                            "id TEXT NOT NULL, "+
                            "author TEXT NOT NULL, "+
                            "categorie TEXT NOT NULL, "+
                            "titre TEXT NOT NULL, "+
                            "photo TEXT NOT NULL, "+
                            "audio TEXT NOT NULL, "+
                            "date TEXT NOT NULL"+
                            ')';
        db.execSQL(command);
    }

    public void insert(Parole parole){
        String command = "INSERT INTO archives(id, author, categorie, titre, photo, audio, date) VALUES(" +
                            "'"+parole.id+"'," +
                            "'"+parole.author.replace("'", "''")+"',"+
                            "'"+parole.categorie.replace("'", "''")+"',"+
                            "'"+parole.titre.replace("'", "''")+"',"+
                            "'"+parole.photo.replace("'", "''")+"',"+
                            "'"+parole.audio.replace("'", "''")+"',"+
                            "'"+parole.date.replace("'", "''")+"'"+
                            ')';
        this.getWritableDatabase().execSQL(command);
    }
    public ArrayList<Parole> getAll(){
        ArrayList<Parole> paroles = new ArrayList<Parole>();
        String[] colonnes = {"id", "author", "categorie", "titre", "photo", "audio", "date"};
        Cursor cursor = this.getReadableDatabase().query("archives", colonnes, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Parole parole = new Parole(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                                        cursor.getString(6), "sans");
            paroles.add(parole);
            cursor.moveToNext();
        }
        cursor.close();
        return paroles;
    }
    public Parole get(String id){
        Parole parole;
        String command = "SELECT * FROM archives WHERE id = \""+id+"\"";
        Cursor cursor = this.getReadableDatabase().rawQuery(command, null);
        cursor.moveToFirst();
        if (cursor.getCount()!=0) {
            parole = new Parole(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), "sans");
            cursor.close();
            return parole;
        }
        return null;
    }

    public void delete(String id){
        String filename;
        String command = "SELECT * FROM archives WHERE id = \""+id+"\"";
        try {
            Cursor cursor = this.getReadableDatabase().rawQuery(command, null);
            cursor.moveToFirst();
            filename = cursor.getString(5);
            File file = new File(new Host(this.context).getDirPath() + File.separator + filename);
            file.delete();
        }catch (Exception e){

        }
        try {
            command = "DELETE FROM archives WHERE id = \"" + id + "\"";
            this.getWritableDatabase().execSQL(command);
        }catch (Exception e){

        }
    }
}
