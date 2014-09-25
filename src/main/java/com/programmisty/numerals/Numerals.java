/*
 * Copyright (C) 2011 Vit <vitalker@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmisty.numerals;

import java.math.BigDecimal;

/**
 *
 * <pre>
 * import java.math.BigDecimal;
 * import java.math.BigInteger;
 * import static com.programmisty.numerals.Numerals.*;
 *
 * public class Sample {
 *
 * 	public static void main(String[] args) {
 * 		// one hundred twenty-three
 * 		System.out.println(english(123));
 *
 * 		// one hundred twenty-three thousand four hundred fifty-six
 * 		System.out.println(english(new BigInteger(&quot;123456&quot;)));
 *
 * 		// Ninety-nine and 89/100
 * 		System.out.println(amount(new BigDecimal(&quot;99.89&quot;)));
 * 	}
 * }
 * </pre>
 *
 * @author vit
 * @version $Id: $Id
 * @since 0.3
 */
public class Numerals {

	/**
	 * Number in word in english. Sample:
	 *
	 * <pre>
	 * Numerals.english(123); // one hundred twenty-three
	 * </pre>
	 *
	 * Supported only integer numbers: Integer, Long, Short, Byte, BigInteger
	 * Max supported number: 1000000000000000000000000000000000000-1
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String english(final Number n) {
		return new English().format(n);
	}

	/**
	 * Number in word in russian. Число прописью по-русски. Sample:
	 *
	 * <pre>
	 * Numerals.russian(123); // семьсот семьдесят семь
	 * </pre>
	 *
	 * Supported only integer numbers: Integer, Long, Short, Byte, BigInteger
	 * Max supported number: 1000000000000000000000000000000000000-1
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String russian(final Number n) {
		return new Russian().format(n);
	}

	/**
	 * Number in word in uzbek. Число прописью по-русски. Sample:
	 *
	 * <pre>
	 * Numerals.uzbek(123); // бир юз йигирма уч
	 * </pre>
	 *
	 * Supported only integer numbers: Integer, Long, Short, Byte, BigInteger
	 * Max supported number: 1000000000000000000000000000000000000-1
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String uzbek(final Number n) {
		return new Uzbek().format(n);
	}

	/**
	 * Amount in words. English. Sometimes useful in payment docs.
	 *
	 * <pre>
	 * BigDecimal x = new BigDecimal(&quot;99.89&quot;);
	 * Numerals.amount(x); // Ninety-nine and 89/100
	 * </pre>
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String amount(final Number n) {
		final BigDecimal bd = Util.toBigDecimal(n);
		return new English().amount(bd);
	}

	/**
	 * Amount in words. Russian. Sometimes useful in finan docs. Сумма прописью.
	 * Бывает используются в разных платежных документах.
	 *
	 * <pre>
	 * BigDecimal x = new BigDecimal(&quot;777.77&quot;);
	 * Numerals.russianRubles(x); // Семьсот семьдесят семь рублей 77 копеек
	 * </pre>
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String russianRubles(final Number n) {
		final BigDecimal bd = Util.toBigDecimal(n);
		return new Russian().amount(bd);
	}

	/**
	 * Amount in words. Russian. Sometimes useful in finan docs. Сумма прописью.
	 * Бывает используются в разных платежных документах.
	 *
	 * <pre>
	 * BigDecimal x = new BigDecimal(&quot;777.77&quot;);
	 * Numerals.russianRubles(x); // Семьсот семьдесят семь рублей 77 копеек
	 * </pre>
	 *
	 * @param n a {@link java.lang.Number} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String uzbekSums(final Number n){
		final BigDecimal bd=Util.toBigDecimal(n);
		
		return new Uzbek().amount(bd); 
	}
	
}
