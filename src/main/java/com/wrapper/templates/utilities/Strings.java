package com.wrapper.templates.utilities;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.mongodb.annotations.ThreadSafe;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;


@ThreadSafe
public final class Strings {
	private static final String BOUNDARY_MATCH_DELIMITER = "\\A";

	// privatized for non-instantiability
	private Strings() {
	}

	public static <T> String collectionToNewLineDelimitedString(Collection<T> collection) {
		return StringUtils.collectionToDelimitedString(collection, "\n");
	}

	@SuppressWarnings("resource")
	public static String readFromStream(InputStream inputStream) {
		return new Scanner(inputStream, "UTF-8").useDelimiter(BOUNDARY_MATCH_DELIMITER).next();
	}

	/**
	 * <p>
	 * Joins the elements of the provided array into a single String containing
	 * the provided list of elements.
	 * </p>
	 * 
	 * <p>
	 * No separator is added to the joined String. Null objects or empty strings
	 * within the array are represented by empty strings.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.join(null)            = null
	 * StringUtils.join([])              = ""
	 * StringUtils.join(["a", "b", "c"]) = "abc"
	 * StringUtils.join([null, "", "a"]) = "a"
	 * </pre>
	 * 
	 * @param objects
	 * @return
	 */
	public static <T> String join(T... objects) {
		if ((objects == null) || (objects.length == 0)) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (T object : objects) {
			if (object != null) {
				builder.append(object.toString());
			}
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param input
	 * @param <T>
	 * @return
	 */
	public static <T> Collection<String> toStrings(Collection<T> input) {
		if (CollectionUtils.isEmpty(input)) {
			return java.util.Collections.emptySet();
		}

		Collection<String> strings = new HashSet<>();
		for (T t : input) {
			if (t != null) {
				strings.add(t.toString());
			}
		}

		return strings;
	}

	/**
	 * 
	 * @param input
	 * @param toStringFunction
	 * @param <T>
	 * @return
	 */
	public static <T> Collection<String> toStrings(Collection<T> input, com.google.common.base.Function<T, String> toStringFunction) {
		if (CollectionUtils.isEmpty(input) || (toStringFunction == null)) {
			return java.util.Collections.emptySet();
		}

		Collection<String> strings = new HashSet<>();
		for (T t : input) {
			if (t != null) {
				strings.add(toStringFunction.apply(t));
			}
		}

		return strings;
	}
}
