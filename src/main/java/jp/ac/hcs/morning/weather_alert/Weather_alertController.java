package jp.ac.hcs.morning.weather_alert;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.morning.main.MainController;
/**警報注意報のコントローラー*/
@Controller
public class Weather_alertController {
	@RequestMapping("/Weather_alert")
	public String getMainData(Model model){
		Weather_alertEntity entity = new Weather_alertEntity();
		Weather_alertService service = new Weather_alertService();
		entity = service.getWeather_alertData();
		model.addAttribute("Weather_alert",entity);
		Weather_alertData a = entity.getWeather_alertnameList().get(0);
		String name = a.getName();
		if(!(entity.isError())) {
		if(name.equals("発表なし")) {
			model.addAttribute("flg", true);
		}else {
			model.addAttribute("flg", false);
		}
		}else {
			MainController main = new MainController();
			model.addAttribute("errormessage","エラーが発生しました。");
			main.getMainData(model);
		}
		return "morning/alert";
	}
		
}
