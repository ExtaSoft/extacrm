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
import java.math.BigInteger;

/**
 * <p>Abstract AbstractNumeral class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class AbstractNumeral {

    /** Constant <code>MAX_SUPPORTED</code> */
    public static final BigInteger MAX_SUPPORTED = new BigInteger("1000000000000000000000000000000000000").subtract(BigInteger.ONE);
    //10^33
    
    /**
     * <p>checkSupported.</p>
     *
     * @param number a {@link java.lang.Number} object.
     */
    protected void checkSupported(final Number number) {
        if (number instanceof Integer
                || number instanceof Long
                || number instanceof Short
                ||number instanceof Byte) {
        } else if (number instanceof BigInteger) {
            final BigInteger bi = (BigInteger) number;
            if (bi.abs().compareTo(MAX_SUPPORTED) > 0) {
                throw new IllegalArgumentException("Max supported number:" + MAX_SUPPORTED);
            }
        } else {
            throw new IllegalArgumentException("Support only Integer numbers: BigInteger, Integer, Long and Short."
                    + "Floating-point is not supported");
        }
    }

    /**
     * <p>format.</p>
     *
     * @param number a {@link java.lang.Number} object.
     * @return a {@link java.lang.String} object.
     */
    public abstract String format(Number number);

    /**
     * <p>amount.</p>
     *
     * @param amount a {@link java.math.BigDecimal} object.
     * @return a {@link java.lang.String} object.
     */
    public abstract String amount(BigDecimal amount);

    /**
     * <p>amount.</p>
     *
     * @param amount a {@link java.lang.Number} object.
     * @return a {@link java.lang.String} object.
     */
    public String amount(final Number amount) {
        return amount(Util.toBigDecimal(amount));
    }
}
