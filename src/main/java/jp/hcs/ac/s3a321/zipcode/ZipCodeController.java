package jp.hcs.ac.s3a321.zipcode;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ZipCodeController {

	@Autowired
	private ZipCodeService zipCodeService;

	/**
	 * 郵便番号から住所を検索し、結果画面を表示します。
	 * <p>
	 * 本機能は、郵便番号検索APIを内部で呼び出して結果を表示します。<br>
	 * 仕様については、下記のドキュメントを参照してください。
	 * <p>
	 * https://zipcloud.ibsnet.co.jp/doc/api
	 *
	 * @param zipCodeForm   入力された内容を格納
	 * @param principal ログイン中のユーザ情報を格納
	 * @param model     Viewに値を渡すオブジェクト
	 * @return 郵便番号結果画面へのパス
	 */
	@GetMapping(ZipCodeVO.MAPPING_RESULT)
	public String getZipCode(@ModelAttribute @Validated ZipCodeForm zipCodeForm,
			Principal principal, Model model) {
		log.info(zipCodeForm.getZipCode());
		ZipCodeEntity zipCodeEntity = this.zipCodeService.execute(zipCodeForm);
		model.addAttribute("results", zipCodeEntity);
		return ZipCodeVO.RESOURCE_RESULT;
	}
}
