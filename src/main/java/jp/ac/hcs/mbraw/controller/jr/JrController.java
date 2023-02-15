package jp.ac.hcs.mbraw.controller.jr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ac.hcs.config.Mapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
/** JR交通情報を取得するコントローラー */
public class JrController {

	@Autowired
	private JrService jrService;

	@GetMapping(Mapping.MAPPING_JR)
	public String mainTrafficData(Model model) {
		log.debug("GET {}", Mapping.MAPPING_JR);

		final JrEntity entity = this.jrService.getallJrData();

		model.addAttribute("JrEntity", entity);

		model.asMap().forEach((k, v) -> log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_JR;
	}
}
