package jp.hcs.ac.s3a321.r4_project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class R4Controller {

    @GetMapping("/r4project/r4Index")
    public String chowToursIndex() {
        return "/r4project/r4Index";
    }
}
