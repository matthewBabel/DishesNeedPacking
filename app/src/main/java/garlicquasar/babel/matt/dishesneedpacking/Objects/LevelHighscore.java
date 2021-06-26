package garlicquasar.babel.matt.dishesneedpacking.Objects;

public class LevelHighscore {
    private String name;
    private String date;
    private int score;
    private int rank;
    private String userID;

    public LevelHighscore(String name, String date, int score, int rank, String id) {
        this.name = name;
        this.date = date;
        this.score = score;
        this.rank = rank;
        this.userID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
