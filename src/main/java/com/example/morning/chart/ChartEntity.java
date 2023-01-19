package com.example.morning.chart;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChartEntity {
	private List<ChartData> chartList = new ArrayList<>();
}
