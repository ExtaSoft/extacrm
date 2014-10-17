package com.programmisty.numerals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.programmisty.numerals.Util.toUpperCaseFirstLetter;

/**
 * <p>Uzbek class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
public class Uzbek extends AbstractNumeral {
	private static final String EDINICHI[] = { "nol", "bir", "ikki", "uch",
			"to'rt", "besh", "olti", "etti", "sakkiz", "tuqqiz" };
	private static final String DESYATKI[] = { "", "o'n", "yigirma", "o'ttiz",
			"qirq", "ellik", "oltmish", "etmish", "sakson", "to'qson" };
	private static final String SOTNI[] = { "", "yuz", "ikki yuz", "uch yuz",
			"to'rt yuz", "besh yuz", "olti yuz", "etti yuz", "sakkiz yuz",
			"to'qqiz yuz" };
	private static final String LIONS[] = { "", "ming", "million", "milliard",
			"trillion", "kvadrillion", "kvintillion", "sekstillion",
			"septillion", "oktillion", "nonillion", "detsillion" };

	/** {@inheritDoc} */
	@Override
	public String format(final Number number) {
		// check number type
		checkSupported(number);
		//
		return formatImpl(number.toString());
	}

	private String formatImpl(String text) {
		if ("0".equals(text)) {
			return EDINICHI[0];
		}
		final StringBuilder sb = new StringBuilder();
		if (text.startsWith("-")) {
			sb.append("minus ");
			text = text.substring(1);
		}

		final byte[][] n = Util.groups(text, 3);

		for (int i = 0; i < n.length; ++i) {

			final int h = n[i][0]; // сотни
			final int t = n[i][1]; // десятки
			final int u = n[i][2]; // единицы
			if (h == 0 && t == 0 && u == 0) {
				// этих вобще
				continue;
			}
			// есть сотенные...
			if (h > 0) {
				if (h == 1 && t == 0 && u == 0) {
					final String sotni = SOTNI[h];
					sb.append(sotni);
					sb.append(" ");
				} else {
					final String sotni = SOTNI[h];

					sb.append("bir " + sotni);
					sb.append(" ");
				}

			}
			if (t > 0) {

				sb.append(DESYATKI[t]);
				sb.append(" ");
			}
			if (u > 0) {
				sb.append(EDINICHI[u]);
				sb.append(" ");
			}
			sb.append(LIONS[n.length - i - 1]);
			sb.append(" ");
		}
		Util.toUpperCaseFirstLetter(sb);
		sb.trimToSize();
		return sb.toString().trim();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Сумма прописью: Семьсот семьдесят семь рублей 77 копеек
	 */
	@Override
	public String amount(final BigDecimal bi) {
		final String txt = bi.toPlainString();

		final int point = txt.indexOf('.');
		final StringBuilder sb = new StringBuilder();
		String rubli = txt;
		if (point > 0) {
			rubli = txt.substring(0, point);
		}

		final String celaya = formatImpl(rubli);
		sb.append(celaya);
		sb.append(" so'm");
		sb.append(" ");

		final int k = roundKopeyki(bi);
		assert (k >= 0 && k < 100);
		
		sb.append(String.valueOf(k)+" tiyin");
		toUpperCaseFirstLetter(sb);
		return sb.toString();
	}

	private static int roundKopeyki(BigDecimal b) {
		b = b.abs();
		final int k = b.multiply(BigDecimal.valueOf(100))
				.remainder(BigDecimal.valueOf(100))
				.setScale(0, RoundingMode.HALF_UP).intValue();
		return k;
	}

}
