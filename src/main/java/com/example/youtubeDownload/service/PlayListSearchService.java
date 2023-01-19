package com.example.youtubeDownload.service;

import com.example.youtubeDownload.entity.PlayList;
import com.example.youtubeDownload.entity.PlayListentity;
import com.example.youtubeDownload.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayListSearchService {

    private final YoutubeRepository youtubeRepository;
    public void addPlayList(String videoId ,String title) {
        youtubeRepository.insert(videoId,title);
    }

    public List<PlayListentity> findPlayList() {
        return youtubeRepository.select();
    }
}
