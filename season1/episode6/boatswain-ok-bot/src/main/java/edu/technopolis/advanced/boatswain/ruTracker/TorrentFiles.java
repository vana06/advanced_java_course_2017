package edu.technopolis.advanced.boatswain.ruTracker;

import java.util.ArrayList;
import java.util.Comparator;

public final class TorrentFiles extends ArrayList<TorrentFile>{

    public static int itemOnPage = 3;
    private int currentItem = 0;

    public boolean hasNext(){
        return currentItem < size();
    }
    public boolean hasPrevious(){
        return currentItem > itemOnPage;
    }
    public TorrentFile next(){
        return get(currentItem++);
    }
    public void prepareForPrev(){
        if(currentItem >= itemOnPage*2) {
            currentItem -= itemOnPage * 2;
        } else {
            currentItem -= itemOnPage + currentItem%itemOnPage;
        }
    }

    public void sortBySeeds(char sortingDirection){
        TorrentFile.key = -1;
        sort(sortingDirection);
    }
    public void sortByPeers(char sortingDirection){
        TorrentFile.key = 1;
        sort(sortingDirection);
    }
    public void sortByDownloadCount(char sortingDirection){
        TorrentFile.key = 2;
        sort(sortingDirection);
    }
    public void sortBySize(char sortingDirection){
        TorrentFile.key = 3;
        sort(sortingDirection);
    }
    public void sortByTime(char sortingDirection){
        TorrentFile.key = 4;
        sort(sortingDirection);
    }
    private void sort(char sortingDirection){
        if(sortingDirection == '-') {
            this.sort(Comparator.reverseOrder());
        }
        else {
            this.sort(Comparator.naturalOrder());
        }
    }
}
