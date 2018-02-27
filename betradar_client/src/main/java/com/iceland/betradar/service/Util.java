package com.iceland.betradar.service;

import java.util.Random;

public class Util {

	public static Integer randomGen(Integer low, Integer high) {
		Random r = new Random();
		return r.nextInt(high-low) + low;
	}
}
