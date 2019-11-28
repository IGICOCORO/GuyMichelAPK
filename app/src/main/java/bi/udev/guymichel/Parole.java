package bi.udev.guymichel;

/**
 * Created by KonstrIctor on 11/11/2019.
 */

public class Parole {
    public String id, author, categorie, titre, photo, audio, date, slug;

    public Parole(String id, String author, String categorie, String titre, String photo, String audio, String date, String slug) {
        this.id = id;
        this.author = author;
        this.categorie = categorie;
        this.titre = titre;
        this.photo = photo;
        this.audio = audio;
        this.date = date;
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "Parole{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", categorie='" + categorie + '\'' +
                ", titre='" + titre + '\'' +
                ", photo='" + photo + '\'' +
                ", audio='" + audio + '\'' +
                ", date='" + date + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
