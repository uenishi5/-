package jp.ac.hcs.morning.jr;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

@Controller
/** JR交通情報を取得するコントローラー*/
public class JrController {
	@RequestMapping("/Jr")
	public String mainTrafficData(Model model) {
		JrService service = new JrService();
		JrEntity entity = service.getallJrData();
		model.addAttribute("JrEntity", entity);
		return Mapping.RESOURCE_JR;
	}
}
