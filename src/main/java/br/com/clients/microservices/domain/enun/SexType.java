package br.com.clients.microservices.domain.enun;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SexType {
	MALE, FEMALE, OTHER;

	public static List<String> getEnumNames() {
		return Arrays.asList(SexType.values()).stream().map(SexType::name).collect(Collectors.toList());
	}
	
	public static SexType getSexEnum(String sex) {
		SexType sexType = null;
		
		switch (sex) {
		case "MALE":
			sexType = SexType.MALE;
			break;
			
		case "FEMALE":
			sexType = SexType.FEMALE;
			break;
			
		case "OTHER":
			sexType = SexType.OTHER;
			break;
			
		}
		
		return sexType;
	}
}
