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
		SAGITTARIUS("射手座", Mapping.RESOURCE_SAGITTARIUS_PNG, Mapping.RESOURCE_SAGITTARIUS_SVG),
		VIRGO("乙女座", Mapping.RESOURCE_VIRGO_PNG, Mapping.RESOURCE_VIRGO_SVG),
		TAURUS("牡牛座", Mapping.RESOURCE_TAURUS_PNG, Mapping.RESOURCE_TAURUS_PNG),
		CANCER("蟹座", Mapping.RESOURCE_CANCER_PNG, Mapping.RESOURCE_CANCER_PNG),
		GEMINI("双子座", Mapping.RESOURCE_GEMINI_PNG, Mapping.RESOURCE_GEMINI_PNG),
		ARIES("牡羊座", Mapping.RESOURCE_ARIES_PNG, Mapping.RESOURCE_ARIES_SVG),
		PISCES("魚座", Mapping.RESOURCE_PISCES_PNG, Mapping.RESOURCE_PISCES_SVG),
		SCORPIO("蠍座", Mapping.RESOURCE_SCORPIO_PNG, Mapping.RESOURCE_SCORPIO_SVG),
		CAPRICORN("山羊座", Mapping.RESOURCE_CAPRICORN_PNG, Mapping.RESOURCE_CAPRICORN_SVG),
		LEO("獅子座", Mapping.RESOURCE_LEO_PNG, Mapping.RESOURCE_LEO_SVG),
		AQUARIUS("水瓶座", Mapping.RESOURCE_AQUARIUS_PNG, Mapping.RESOURCE_AQUARIUS_SVG),
		LIBRA("天秤座", Mapping.RESOURCE_LIBRA_PNG, Mapping.RESOURCE_LIBRA_SVG);

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
