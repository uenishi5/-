package jp.ac.hcs.morning.jr;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class JrController {
	@RequestMapping("/Jr")
	public String mainTrafficData(Model model) {
		JrService service = new JrService();
		JrEntity entity = new JrEntity();
		entity = service.getallJrData();
		model.addAttribute("JrEntity", entity);
		return "morning/jr_traffic";
	}
}
