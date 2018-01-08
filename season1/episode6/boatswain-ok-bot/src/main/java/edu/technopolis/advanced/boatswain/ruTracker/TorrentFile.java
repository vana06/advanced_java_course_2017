package edu.technopolis.advanced.boatswain.ruTracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TorrentFile implements Comparable<TorrentFile>{
    private String category;
    private String name;
    private String size;
    private Integer seed;
    private Integer peers;
    private Integer downloadedCount;
    private Date time;
    private String id;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");

    static int key = 0;
    static String downloadLink, viewLink;

    TorrentFile(String category, String name, String size, String seed, String peers, String downloadedCount, Date time, String id) {
        this.category = category;
        this.name = name;
        this.size = size;
        this.seed = seed == null ? 0 : Integer.parseInt(seed);
        this.peers = Integer.parseInt(peers);
        this.downloadedCount = Integer.parseInt(downloadedCount);
        this.time = time;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(TorrentFile o) {
        switch (key){
            case 1:
                return peers.compareTo(o.peers);
            case 2:
                return downloadedCount.compareTo(o.downloadedCount);
            case 3:
                return size.compareTo(o.size);
            case 4:
                return time.compareTo(o.time);
        }
        return seed.compareTo(o.seed);
    }

    @Override
    public String toString() {
        return  name + '\n' +
                viewLink + id + '\n' +
                "S " + seed + " | " +
                " L " + peers + " | " +
                " D " + downloadedCount + " | " +
                " Reg " + simpleDateFormat.format(time) + " | " +
                " Size " + size + "\n" +
                downloadLink + id + "\n\n";
    }
}
