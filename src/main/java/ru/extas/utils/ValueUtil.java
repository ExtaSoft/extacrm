package ru.extas.utils;

import java.text.MessageFormat;

/**
 * <p>ValueUtil class.</p>
 *
 * @author Valery Orlov
 *         Date: 29.08.13
 *         Time: 16:45
 * @version $Id: $Id
 */
public class ValueUtil {

    private final static int DG_POWER = 6;
    private static String[][] a_power = new String[][]{
            {"0", "", "", ""}, // 1
            {"1", "тысяча ", "тысячи ", "тысяч "}, // 2
            {"0", "миллион ", "миллиона ", "миллионов "}, // 3
            {"0", "миллиард ", "миллиарда ", "миллиардов "}, // 4
            {"0", "триллион ", "триллиона ", "триллионов "}, // 5
            {"0", "квадриллион ", "квадриллиона ", "квадриллионов "}, // 6
            {"0", "квинтиллион ", "квинтиллиона ", "квинтиллионов "} // 7
    };
    private static String[][] digit = new String[][]{
            {"", "", "десять ", "", ""},
            {"один ", "одна ", "одиннадцать ", "десять ", "сто "},
            {"два ", "две ", "двенадцать ", "двадцать ", "двести "},
            {"три ", "три ", "тринадцать ", "тридцать ", "триста "},
            {"четыре ", "четыре ", "четырнадцать ", "сорок ", "четыреста "},
            {"пять ", "пять ", "пятнадцать ", "пятьдесят ", "пятьсот "},
            {"шесть ", "шесть ", "шестнадцать ", "шестьдесят ", "шестьсот "},
            {"семь ", "семь ", "семнадцать ", "семьдесят ", "семьсот "},
            {"восемь ", "восемь ", "восемнадцать ", "восемьдесят ", "восемьсот "},
            {"девять ", "девять ", "девятнадцать ", "девяносто ", "девятьсот "}
    };

    private static String intToString(int sum) {
        int i, mny;
        StringBuilder result = new StringBuilder("");
        long divisor; //делитель
        int psum = sum;

        int one = 1;
        int four = 2;
        int many = 3;

        int hun = 4;
        int dec = 3;
        int dec2 = 2;

        if (sum == 0) {
            return "ноль ";
        }
        if (sum < 0) {
            result.append("минус ");
            psum = -psum;
        }

        for (i = 0, divisor = 1; i < DG_POWER; i++) {
            divisor *= 1000;
        }

        for (i = DG_POWER - 1; i >= 0; i--) {
            divisor /= 1000;
            mny = (int) (psum / divisor);
            psum %= divisor;
            //str="";
            if (mny == 0) {
                if (i > 0) {
                    continue;
                }
                result.append(a_power[i][one]);
            } else {
                if (mny >= 100) {
                    result.append(digit[mny / 100][hun]);
                    mny %= 100;
                }
                if (mny >= 20) {
                    result.append(digit[mny / 10][dec]);
                    mny %= 10;
                }
                if (mny >= 10) {
                    result.append(digit[mny - 10][dec2]);
                } else {
                    if (mny >= 1) {
                        result.append(digit[mny]["0".equals(a_power[i][0]) ? 0 : 1]);
                    }
                }
                switch (mny) {
                    case 1:
                        result.append(a_power[i][one]);
                        break;
                    case 2:
                    case 3:
                    case 4:
                        result.append(a_power[i][four]);
                        break;
                    default:
                        result.append(a_power[i][many]);
                        break;
                }
            }
        }
        return result.toString();
    }

    private static String doubleToString(double num, String[][] curs) {
        return MessageFormat.format("{0} {1} {2} {3}",
                intToString((int) num),
                declOfNum((int) num, curs[0]),
                intToString((int) (num * 100 - ((int) num) * 100)),
                declOfNum((int) (num * 100 - ((int) num) * 100), curs[1]));
    }

    private static String declOfNum(Integer number, String[] titles) {
        Integer[] cases = new Integer[6];
        cases[0] = 2;
        cases[1] = 0;
        cases[2] = 1;
        cases[3] = 1;
        cases[4] = 1;
        cases[5] = 2;
        String result = "";

        Integer position;
        if (number % 100 > 4 && number % 100 < 20) {
            position = 2;
        } else {
            position = cases[Math.min(number % 10, 5)];
        }
        result += titles[position];

        return result;
    }

    /**
     * <p>spellOutInteger.</p>
     *
     * @param num a int.
     * @return a {@link java.lang.String} object.
     */
    public static String spellOutInteger(int num) {
        return intToString(num);
    }

    /**
     * <p>spellOutRubles.</p>
     *
     * @param num a double.
     * @return a {@link java.lang.String} object.
     */
    public static String spellOutRubles(double num) {
        String[][] curRubKop = {{"рубль", "рубля", "рублей"}, {"копейка", "копейки", "копеек"}};
        return doubleToString(num, curRubKop);
    }

    /**
     * <p>spellOutThing.</p>
     *
     * @param num a int.
     * @return a {@link java.lang.String} object.
     */
    public static String spellOutThing(int num) {
        String[] curs = {"штука", "штуки", "штук"};
        return String.format("%s %s", intToString(num), declOfNum(num, curs));
    }
};
