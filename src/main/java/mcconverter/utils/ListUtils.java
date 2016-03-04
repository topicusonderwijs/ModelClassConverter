package mcconverter.utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListUtils {
	
	/**
	 * Filters and returns the given list using the given predicate.
	 */
	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		
		return list.stream().filter(predicate).collect(Collectors.toList());
		
	}
	
}
