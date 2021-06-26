package garlicquasar.babel.matt.dishesneedpacking.Objects;

public class GlobalHighscore implements Comparable<GlobalHighscore>{
    private String name;
    private int score;

    public GlobalHighscore(String name, int score)  {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Global Highscore{" +
                "name='" + name + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

    @Override
    public int compareTo(GlobalHighscore o) {
        return ((Integer)o.score).compareTo(score);
    }
}
