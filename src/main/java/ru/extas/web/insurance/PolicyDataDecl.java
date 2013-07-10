/**
 * 
 */
package ru.extas.web.insurance;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отображения полисов БСО
 * 
 * @author Valery Orlov
 * 
 */
public class PolicyDataDecl extends GridDataDecl {

	public PolicyDataDecl() {
		addMapping("regNum", "Номер полиса");
		addMapping("bookTime", "Зарезервирован");
		addMapping("issueDate", "Реализован");
		super.addCreateModifyMarkers();
	}

}
