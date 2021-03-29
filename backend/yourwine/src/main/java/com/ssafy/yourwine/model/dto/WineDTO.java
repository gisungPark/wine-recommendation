package com.ssafy.yourwine.model.dto;

import com.ssafy.yourwine.model.entity.Grape;

import lombok.Data;
import lombok.ToString;



@Data
public class WineDTO {

	private Long wineId;
	private String kname;
	private String ename;
	private String alcohol;
	private String type;
	private String temper;
	private float avg;
	private int price;
	private String year;
	private String detail;
	private String shop;
	private int sweet;
	private int acidity;
	private int body;
	private int tannin;
	private String area;
	private Grape grape;
}
