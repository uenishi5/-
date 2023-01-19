package com.example.youtubeDownload.controller;

import com.example.youtubeDownload.entity.PlayList;
import com.example.youtubeDownload.service.PlayListSearchService;
import com.example.youtubeDownload.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class YoutubeListController {
    private final SearchService service;
    private final PlayListSearchService playListSearchService;

    @GetMapping("")
    public String showList(@ModelAttribute PlayList playList) {
        return "index";
    }

    @PostMapping("/index")
    public String SearchResult(@RequestParam String detail, Model model){
        model.addAttribute("searchLists",service.getList(detail));
        return "index";
    }

    @PostMapping("/unti")
    public String  addPlayList(@Validated PlayList playList){
        playListSearchService.addPlayList(playList.getVideoId(),playList.getTitle());
        return "redirect:/";
    }

    @GetMapping("/playList")
    public String showPlayList(Model model) {
        model.addAttribute("playListentity",playListSearchService.findPlayList());
        return "playList";
    }

    @GetMapping("/mp3Changer")
    public String showMp3Changer() {
        return "mp3Changer";
    }
}
