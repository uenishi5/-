package jp.hcs.ac.s3a321.r4_project.service;

import jp.hcs.ac.s3a321.r4_project.entity.SearchResult;

import java.util.ArrayList;

public class a {
    public static void main(String[] args) {
        SearchService sample = new SearchService();
        ArrayList<SearchResult> results = new ArrayList<>();
        results = sample.getList("チェンソーマン");
        for(SearchResult result:results){
            System.out.println("永久機関が完成しちまったなぁ〜");
            System.out.println(result.getVideoId());
            System.out.println(result.getTitle());
            System.out.println(result.getThumbnail());
        }
    }
}
