/**
 * 
 */
package org.antispin.java.lang;

public final class Enums {
	
	public static <E extends Enum<E>> E previous(E constant) {
		final E[] enumConstants = constant.getDeclaringClass().getEnumConstants();
		if(constant.ordinal() == 0) {
			throw new IllegalArgumentException();
		}
		return enumConstants[constant.ordinal() - 1];
	}
	
	public static <E extends Enum<E>> E next(E constant) {
		final E[] enumConstants = constant.getDeclaringClass().getEnumConstants();
		if(constant.ordinal() == enumConstants.length - 1) {
			throw new IllegalArgumentException();
		}
		return enumConstants[constant.ordinal() + 1];
	}
	
}