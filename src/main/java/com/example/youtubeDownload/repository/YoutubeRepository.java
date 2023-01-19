package com.example.youtubeDownload.repository;

import com.example.youtubeDownload.entity.PlayList;
import com.example.youtubeDownload.entity.PlayListentity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface YoutubeRepository {

    @Insert("insert into playList(videoId,title) values(#{videoId},#{title})")
    void insert(String videoId, String title);

    @Select("select videoId, title from playList")
    List<PlayListentity> select();
}
