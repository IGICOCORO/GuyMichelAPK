package bi.udev.guymichel;

/**
 * Created by KonstrIctor on 22/11/2019.
 */

class Temoignage {
    public String id, titre, categorie, author, photo, date, slug;

    public Temoignage(String id, String titre, String categorie, String author, String photo, String date, String slug) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.author = author;
        this.photo = photo;
        this.date = date;
        this.slug = slug;
    }
}
