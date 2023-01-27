package jp.ac.hcs.config;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CommonAttribute {

	// @Autowired
	// NotificateCrud notificateCrud;

	@ModelAttribute
	public void addAttributes(Principal principal, Model model) {
		// String notice = "";
		// try {
		// notice = NotificateCrudUtils.getNoticeCount(this.notificateCrud,
		// principal.getName(), false);
		// } catch (NullPointerException e) {
		// notice = "";
		// }
		// model.addAttribute("notice", notice);
	}
}