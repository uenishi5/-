package jp.hcs.ac.s3a321.r4_project.web;

import jp.hcs.ac.s3a321.r4_project.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/r4project")
@RequiredArgsConstructor
public class R4Controller {
    private final SearchService service;

    @GetMapping("/r4Index")
    public String showList() {
        return "r4project/r4Index";
    }

    @PostMapping("/r4Index")
    public String SearchResult(@RequestParam String detail, Model model){
        model.addAttribute("searchLists",service.getList(detail));
        return "r4project/r4Index";
    }
}
