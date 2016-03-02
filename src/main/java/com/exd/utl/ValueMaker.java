package com.exd.utl;

import java.util.UUID;

public class ValueMaker {
	public static String uuid(Object key){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
