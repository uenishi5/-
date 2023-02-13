package jp.ac.hcs.mbraw.controller.horoscope;

import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.mbraw.controller.horoscope.pojo.HoroscopeInfo;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
public class HoroscopeData {

	/** アドバイス（詳細) */
	private HoroscopeInfo info;

	private HoroscopeSign sign;

	public static HoroscopeData error() {
		final HoroscopeData horoscopeData = new HoroscopeData();

		return horoscopeData;
	}

	public static HoroscopeData map(HoroscopeInfo info) {
		final HoroscopeData data = new HoroscopeData();

		data.setInfo(info);
		data.setSign(HoroscopeSign.parseJp(info.getSign()));

		return data;
	}

	@Getter
	public enum HoroscopeSign {
		SAGITTARIUS("射手座", Mapping.RESOURCE_PNG_SAGITTARIUS, Mapping.RESOURCE_SVG_SAGITTARIUS),
		VIRGO("乙女座", Mapping.RESOURCE_PNG_VIRGO, Mapping.RESOURCE_SVG_SAGITTARIUS),
		TAURUS("牡牛座", Mapping.RESOURCE_PNG_TAURUS, Mapping.RESOURCE_SVG_SAGITTARIUS),
		CANCER("蟹座", Mapping.RESOURCE_PNG_CANCER, Mapping.RESOURCE_SVG_SAGITTARIUS),
		GEMINI("双子座", Mapping.RESOURCE_PNG_GEMINI, Mapping.RESOURCE_SVG_SAGITTARIUS),
		ARIES("牡羊座", Mapping.RESOURCE_PNG_ARIES, Mapping.RESOURCE_SVG_SAGITTARIUS),
		PISCES("魚座", Mapping.RESOURCE_PNG_PISCES, Mapping.RESOURCE_SVG_SAGITTARIUS),
		SCORPIO("蠍座", Mapping.RESOURCE_PNG_SCORPIO, Mapping.RESOURCE_SVG_SAGITTARIUS),
		CAPRICORN("山羊座", Mapping.RESOURCE_PNG_CAPRICORN, Mapping.RESOURCE_SVG_SAGITTARIUS),
		LEO("獅子座", Mapping.RESOURCE_PNG_LEO, Mapping.RESOURCE_SVG_SAGITTARIUS),
		AQUARIUS("水瓶座", Mapping.RESOURCE_PNG_AQUARIUS, Mapping.RESOURCE_SVG_SAGITTARIUS),
		LIBRA("天秤座", Mapping.RESOURCE_PNG_LIBRA, Mapping.RESOURCE_SVG_LIBRA);

		private final String signJp;
		private final String resourcePng;
		private final String resourceSvg;

		HoroscopeSign(String signJp, String resourcePng, String resourceSvg) {
			this.signJp = signJp;
			this.resourcePng = resourcePng;
			this.resourceSvg = resourceSvg;
		}

		public static HoroscopeSign parseJp(String signJp) {
			for (HoroscopeSign sign : values()) {
				if (sign.getSignJp().equals(signJp)) {
					return sign;
				}
			}

			return HoroscopeSign.PISCES;
		}
	}
}
