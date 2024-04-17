package org.enissay.osu;

public class Beatmap {

    private String path, title, diffName, artist, mapper;

    public Beatmap(String path, String title, String diffName, String artist, String mapper) {
        this.path = path;
        this.title = title;
        this.diffName = diffName;
        this.artist = artist;
        this.mapper = mapper;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDiffName() {
        return diffName;
    }

    public String getArtist() {
        return artist;
    }

    public String getMapper() {
        return mapper;
    }

    @Override
    public String toString() {
        return "Beatmap{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", diffName='" + diffName + '\'' +
                ", artist='" + artist + '\'' +
                ", mapper='" + mapper + '\'' +
                '}';
    }
}
