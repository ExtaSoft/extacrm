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
 * Utility-class for internal usage only
 * @author vit
 */
class Util {

    /**
     * 
     * @param txt
     * @param groupSize
     * @return
     */
    static byte[][] groups(final String txt, final int groupSize) {
    	
        final int length = txt.length();
        int groupCount = length / groupSize;
        final int remainder = length % groupSize;

        int j = 0, k = 0;
        if (remainder > 0) {
            groupCount++;
            k = groupSize - remainder;
        }
        final byte[][] nn = new byte[groupCount][groupSize];
        // 1234567 - 1,2,3,4,5,6,7 -- [0,0,1],[2,3,4],[5,6,7]
        for (int i = 0; i < txt.length(); ++i) {
            final byte x = (byte) (txt.charAt(i) - '0');
            if (x < 0 || x > 9) {
                throw new IllegalArgumentException("Wrong string:" + txt);
            }
            nn[j][k] = x;
            if (k == groupSize - 1) {
                k = 0;
                j++;
            } else {
                k++;
            }
        }
        return nn;
    }

    /**
     * @param array
     * @param token
     * @return -1 = not found, else index
     */
    static int search(final String[] array, final String token) {
        int x = -1;
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(token)) {
                x = i;
                break;
            }
        }
        return x;
    }

    static void toUpperCaseFirstLetter(final StringBuilder sb) {
        // to upper case
        char first = sb.charAt(0);
        first = Character.toUpperCase(first);
        sb.setCharAt(0, first);
    }

    static BigDecimal toBigDecimal(final Number n) {
        final BigDecimal bd;
        if (n == null) {
            throw new IllegalArgumentException("Your number is null");
        } else if (n instanceof BigDecimal) {
            bd = (BigDecimal) n;
        } else if (n instanceof BigInteger) {
            bd = new BigDecimal((BigInteger) n);
        } else if (n instanceof Short
                || n instanceof Long
                || n instanceof Integer) {
            bd = BigDecimal.valueOf(n.longValue());
        } else if (n instanceof Float
                || n instanceof Double) {
            bd = BigDecimal.valueOf(n.doubleValue());
        } else {
            throw new IllegalArgumentException("Unsupported type:" + n);
        }
        return bd;
    }
}
