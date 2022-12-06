package jp.hcs.ac.s3a321.r4_project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResult {
    private String videoId;
    private String title;
    private String thumbnail;

    public SearchResult() {

    }
}
