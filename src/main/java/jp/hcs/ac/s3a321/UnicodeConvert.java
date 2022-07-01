package jp.hcs.ac.s3a321;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 文字列を変換するメソッドを提供するクラスです。
 *
 * @author s20203029
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnicodeConvert {
	/**
	 * Unicode文字列に変換する("あ" -> "\u3042") <div> <div>仕組み</div> "あ"(String) ->
	 * 3042(char) -> "\u3042"(String) 順番に変換されています。 </div>
	 *
	 * @return 変換後の文字列
	 */
	public static String toUnicode(@NonNull String str) {
		StringBuilder sb = new StringBuilder();
		/*
		 * 文字列を文字配列にします。 すでに16進数になっているので format メソッドを使い、 ゼロパディング（４桁）・文字列変換をします。
		 */
		str.chars().mapToObj(ch -> String.format("\\u%04X", ch)).forEach(ch -> sb.append(ch));
		return sb.toString();
	}

	/**
	 * Unicode文字列から元の文字列に変換する ("\u3042" -> "あ") <div> <div>仕組み</div>
	 * "\u3042"(String) -> 3042(int) -> "あ"(String) 順番に変換されています。 </div>
	 *
	 * @return 変換後の文字列
	 */
	public static String toOriginal(@NonNull String str) {
		StringBuilder sb = new StringBuilder();
		/*
		 * unicodeは\\uと16進数で構成されています。 なので、\\uで区切って配列にします。 Integerクラスで変換を掛けて数値に変換後、
		 * 文字をStringBuilderにappendしてます。
		 */
		Stream.of(str.split("\\\\u")).mapToInt(s -> !s.isEmpty() ? Integer.parseInt(s, 16) : 0)
				.forEach(ch -> sb.append(ch > 0 ? (char) ch : ""));
		return sb.toString();
	}
}