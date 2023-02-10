package jp.ac.hcs.mbraw.traffic;

import lombok.Data;
@Data
public class TrafficData {
	
	/** 通行止めなど、障害が起きているかのフラグ*/
	private boolean alertflg ;
	
	/** アラートが出ている高速バスの場所名 */
	private String alertbus;
	
	/** バスの路線名 */
	private String rosen;
	
	/** バス方面 */
	private String houmen;
	
	/** バス運行状況 */
	private String situation;
	
	/** バス備考欄 */
	private String remarks;
	
	/** 要素（行）数 */
	private int rowspan;
	
	/** 高速道路の上りor下り情報*/
	private String highwayroad;
	
	/** 高速道路詳細情報 */
	private String highwaydata;
	
	/** 路線連番*/
	private int count;
	
	/** エラーキャッチフラグ*/
	private boolean catchflg;
}
