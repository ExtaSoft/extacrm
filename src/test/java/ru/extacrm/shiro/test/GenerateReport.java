/**
 *
 */
package ru.extacrm.shiro.test;

import org.joda.time.LocalDate;
import ru.extas.model.Insurance;
import ru.extas.model.Person;

import java.math.BigDecimal;

/**
 * @author Valery Orlov
 */
public class GenerateReport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * @return
	 */
	private static Insurance createInsurance() {
		Insurance ins = new Insurance();
		Person client = new Person();
		client.setName("Снегирев Владимир Иванович");
		client.setBirthday(new LocalDate(1979, 5, 25));
		client.setPhone("+7 925 300 20 40");
		client.setSex(Person.Sex.MALE);
		ins.setRegNum("СП-2013/00046");
		ins.setDate(new LocalDate(2013, 5, 28));
		ins.setMotorType("Снегоболотоход");
		ins.setMotorBrand("POLARIS");
		ins.setMotorModel("Sportsman 550  Touring EFI EPS");
		ins.setRiskSum(new BigDecimal(434714));
		ins.setPremium(new BigDecimal(19563));
		ins.setClient(client);
		return ins;
	}
}
