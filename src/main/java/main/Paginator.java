package main;

import net.dv8tion.jda.api.EmbedBuilder;
import net.jodah.expiringmap.ExpiringMap;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Paginator {
    public static ExpiringMap<String, Paginator> paginators = ExpiringMap.builder()
            .maxSize(100000)
            .expiration(60, TimeUnit.MINUTES)
            .build();


    public ArrayList<EmbedBuilder> embeds;
    public EmbedBuilder currentPage;
    public String footer;
    public int color;
    public int pagePosition;
    public int pages;
    public boolean isSinglePage;

    public Paginator(String footer, int color){
        pagePosition = 1;
        pages = 1;
        embeds = new ArrayList<>();
        currentPage = new EmbedBuilder();
        isSinglePage = true;
        this.footer = footer;
        this.color = color;
    }

    public Paginator(EmbedBuilder embed){
        currentPage = embed;
        embeds = new ArrayList<>();
        embeds.add(embed);
        pagePosition = 1;
        pages = 1;
        isSinglePage = true;
    }

    public void paginate(){
        if(embeds.size() > 1) {
            ArrayList<EmbedBuilder> preEmbeds = new ArrayList<>();

            isSinglePage = false;

            for(EmbedBuilder embed : embeds) {
                embed.setColor(color);
                embed.setFooter(footer + "\t\t Page " + pagePosition + "/" +  (pages-1));
                preEmbeds.add(embed);
            }
            embeds = preEmbeds;
        }

        currentPage = embeds.get(0);
    }

    public void addPage(EmbedBuilder embedBuilder){
        embeds.add(embedBuilder);
        pages += 1;

    }
    
    public void previousPage(){
        if(pagePosition == 1) return;
        pagePosition -= 1;
        currentPage = embeds.get(pagePosition-1);
        currentPage.setFooter(footer + "\t\t Page " + pagePosition + "/" +  (pages-1));
    }

    public void nextPage(){
        if(pages-1 == pagePosition) return;
        pagePosition += 1;
        currentPage = embeds.get(pagePosition-1);
        currentPage.setFooter(footer + "\t\t Page " + pagePosition + "/" +  (pages-1));
    }
}
