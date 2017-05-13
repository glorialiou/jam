package hu.ait.jam.data;

public class Profile {

    private String uid;
    private String name;
    private String instrument;
    private String years;
    private String genre;
    private String bio;
    private String search;
    private String imageUrl;

    public Profile(){
    }

    public Profile(String uid, String name, String instrument, String years,
                   String genre, String bio, String search) {
        this.uid = uid;
        this.name = name;
        this.instrument = instrument;
        this.years = years;
        this.genre = genre;
        this.bio = bio;
        this.search = search;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
