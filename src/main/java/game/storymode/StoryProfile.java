package game.storymode;

import game.storymode.episodes.C1Episode1;

import java.util.ArrayList;

public class StoryProfile {
    public ArrayList<Episode> completedEpisodes;
    public Episode currentEpisode;
    public ArrayList<Item> collectedItems;

    public StoryProfile(){
        this.completedEpisodes = new ArrayList<>();
        this.currentEpisode = new C1Episode1();
        this.collectedItems = new ArrayList<>();
    }

}
