package jp.ac.hcs.morning.jr;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class JrEntity {
	private final List<JrData> jrList = new ArrayList<>();

	public static JrEntity error() {
		final JrEntity jrEntity = new JrEntity();

		jrEntity.getJrList().add(JrData.error());

		return jrEntity;
	}
}
