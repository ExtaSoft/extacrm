package ru.extas;

import com.google.code.appengine.awt.Color;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;

public class ExtaFontProvider implements IFontProvider {
	private boolean systemEncodingDeterminated;

	private String systemEncoding;

	private static boolean registerFonts = false;

	@Override
	public Font getFont(String familyName, String encoding, float size, int style, Color color) {
		registerFontsIfNeeded();
		if (familyName != null) {
			familyName = resolveFamilyName(familyName, style);
		}

		return FontFactory.getFont(familyName, encoding, size, style, color);
	}

	/**
	 * Register fonts from files (ex : for windows, load files from
	 * C:\WINDOWS\Fonts).
	 */
	private void registerFontsIfNeeded() {
		if (!registerFonts) {

			FontFactory.register("fonts/arial.ttf");
			FontFactory.register("fonts/arialbd.ttf");
			FontFactory.register("fonts/arialbi.ttf");
			FontFactory.register("fonts/ariali.ttf");
			FontFactory.register("fonts/cour.ttf");
			FontFactory.register("fonts/courbd.ttf");
			FontFactory.register("fonts/courbi.ttf");
			FontFactory.register("fonts/couri.ttf");
			FontFactory.register("fonts/times.ttf");
			FontFactory.register("fonts/timesbd.ttf");
			FontFactory.register("fonts/timesbi.ttf");
			FontFactory.register("fonts/timesi.ttf");

			registerFonts = true;
		}
	}

	/**
	 * checks if this font is Bold.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */
	public boolean isBold(int style) {
		if (style == Font.UNDEFINED) {
			return false;
		}
		return (style & Font.BOLD) == Font.BOLD;
	}

	/**
	 * checks if this font is Bold.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */
	public boolean isItalic(int style) {
		if (style == Font.UNDEFINED) {
			return false;
		}
		return (style & Font.ITALIC) == Font.ITALIC;
	}

	/**
	 * checks if this font is underlined.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */
	public boolean isUnderlined(int style) {
		if (style == Font.UNDEFINED) {
			return false;
		}
		return (style & Font.UNDERLINE) == Font.UNDERLINE;
	}

	/**
	 * checks if the style of this font is STRIKETHRU.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */
	public boolean isStrikethru(int style) {
		if (style == Font.UNDEFINED) {
			return false;
		}
		return (style & Font.STRIKETHRU) == Font.STRIKETHRU;
	}

	public String getSystemEncoding() {
		if (systemEncodingDeterminated) {
			return systemEncoding;
		}
		// don't rely on file.encoding property because
		// it may be changed if application is launched inside an ide
		systemEncoding = System.getProperty("sun.jnu.encoding");
		if (systemEncoding != null && systemEncoding.length() > 0) {
			systemEncodingDeterminated = true;
			return systemEncoding;
		}
		systemEncoding = System.getProperty("ibm.system.encoding");
		if (systemEncoding != null && systemEncoding.length() > 0) {
			systemEncodingDeterminated = true;
			return systemEncoding;
		}
		systemEncoding = FontFactory.defaultEncoding;
		systemEncodingDeterminated = true;
		return systemEncoding;
	}

	private static final String TIMES_NEW_ROMAN_FONT_FAMILY_NAME = "Times New Roman";

	private static final String COURRIER_NEW_FONT_FAMILY_NAME = "Courier New";

	protected String resolveFamilyName(String familyName, int style) {
		boolean bold = isBold(style);
		boolean italic = isItalic(style);

		if (COURRIER_NEW_FONT_FAMILY_NAME.equals(familyName)) {
			if (bold && italic) {
				return BaseFont.COURIER_BOLDOBLIQUE;
			} else if (bold) {
				return BaseFont.COURIER_BOLD;
			} else if (italic) {
				return BaseFont.COURIER_OBLIQUE;
			}
			return BaseFont.COURIER;
		}

		if (TIMES_NEW_ROMAN_FONT_FAMILY_NAME.equals(familyName)) {
			if (bold && italic) {
				return BaseFont.TIMES_BOLDITALIC;
			} else if (bold) {
				return BaseFont.TIMES_BOLD;
			} else if (italic) {
				return BaseFont.TIMES_ITALIC;
			}
			return BaseFont.TIMES_ROMAN;
		}

		return familyName;
	}

}
