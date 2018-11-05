package rikkeisoft.nguyenducdung.com.music.model;

public class SongInfo {
    private String Songname;
    private String Artistname;
    private String SongUrl;

    public SongInfo() {
    }

    public SongInfo(String songname, String artistname, String songUrl) {
        Songname = songname;
        Artistname = artistname;
        SongUrl = songUrl;
    }

    public String getSongname() {
        return Songname;
    }

    public String getArtistname() {
        return Artistname;
    }

    public String getSongUrl() {
        return SongUrl;
    }

}
