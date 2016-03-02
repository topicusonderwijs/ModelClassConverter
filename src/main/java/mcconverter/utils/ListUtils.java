package mcconverter.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListUtils {
	
	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		
		return list.stream().filter(predicate).collect(Collectors.toList());
		
	}
	
}
