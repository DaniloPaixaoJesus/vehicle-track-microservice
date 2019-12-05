package br.com.danilopaixao.vehicle.track.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeUtils {
	
//	public static void main(String[] args) {
//		LocalDateTime hoje = LocalDateTime.now();
//		LocalDateTime outraData = LocalDateTime.of(2019, Month.NOVEMBER, 13, 11, 40);
//		System.out.println(secsDiff(outraData, hoje));
//	}
	
	public static long minsDiff(LocalDateTime iniDt, LocalDateTime endDt) {
        return ChronoUnit.MINUTES.between(iniDt, endDt);
	}
	public static long secsDiff(LocalDateTime iniDt, LocalDateTime endDt) {
        return ChronoUnit.SECONDS.between(iniDt, endDt);
	}
}
