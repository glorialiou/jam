package hu.ait.jam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Profile implements Serializable {

    private ArrayList<String> swipedOnMe;
    private HashMap<String, String> matches;
    private String email;
    private String name;
    private String phone;
    private String instrument;
    private String years;
    private String genre;
    private String bio;
    private String goals;
    private String imageUrl;

    public Profile() {};

    public ArrayList<String> getSwipedOnMe() {
        return swipedOnMe;
    }

    public void setSwipedOnMe(ArrayList<String> swipedOnMe) {
        this.swipedOnMe = swipedOnMe;
    }

    public HashMap<String, String> getMatches() {
        return matches;
    }

    public void setMatches(HashMap<String, String> matches) {
        this.matches = matches;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Profile(ArrayList<String> swipedOnMe, HashMap<String, String> matches,
                   String email, String name, String phone, String instrument,
                   String years, String genre, String bio, String goals) {
        this.swipedOnMe = swipedOnMe;
        this.matches = matches;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.instrument = instrument;
        this.years = years;
        this.genre = genre;
        this.bio = bio;
        this.goals = goals;
    }

}
