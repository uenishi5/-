package jp.hcs.ac.s3a321.r4_project.web;

import jp.hcs.ac.s3a321.r4_project.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class R4Controller {
    private SearchService service;

    @GetMapping("/r4project/r4Index")
    public String showList(Model model) {
        return "/r4project/r4Index";
    }
}
