package jp.ac.hcs.mbraw.controller.jr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

@Controller
/** JR交通情報を取得するコントローラー */
public class JrController {

	@Autowired
	private JrService jrService;

	@RequestMapping(Mapping.MAPPING_JR)
	public String mainTrafficData(Model model) {
		final JrEntity entity = this.jrService.getallJrData();

		model.addAttribute("JrEntity", entity);

		return Mapping.RESOURCE_JR;
	}
}
