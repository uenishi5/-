package jp.hcs.ac.s3a321.main;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalController {

	@RequestMapping(PortalVO.MAPPING_INDEX)
	public String index(Principal principal, Model model) {
		return PortalVO.RESOURCE_INDEX;
	}
}
