/**
 *
 */
package ru.extas.server;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.extas.model.Insurance;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Реализация калькулятора страховых премий
 *
 * @author Valery Orlov
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class InsuranceCalculatorImpl implements InsuranceCalculator {

//	private static final ArrayList<String> tarif055Brands = newArrayList("BRP", "Yamaha");
//	private static final ArrayList<String> tarif045Btands = newArrayList("CFMOTO", "Kawasaki", "Arctic Cat", "Polaris", "CECTEK", "Baltmotors",
//			"Suzuki", "Honda", "Polar Fox");
//	private static final ArrayList<String> tarig06Brands = newArrayList("STELS");

	private static final Table<String, Insurance.PeriodOfCover, BigDecimal> tarifTable;

	static {
		tarifTable = HashBasedTable.create();
		BigDecimal yearTarif = BigDecimal.valueOf(.045);
		BigDecimal halfTarif = BigDecimal.valueOf(.032);
		tarifTable.put("Kawasaki", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Kawasaki", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("Arctic Cat", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Arctic Cat", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("Polaris", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Polaris", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("CECTEK", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("CECTEK", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);

		yearTarif = BigDecimal.valueOf(.045);
		halfTarif = BigDecimal.valueOf(.032);
		tarifTable.put("Baltmotors", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Baltmotors", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("Suzuki", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Suzuki", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("Honda", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Honda", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("Polar Fox", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Polar Fox", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);

		yearTarif = BigDecimal.valueOf(.06);
		halfTarif = BigDecimal.valueOf(.042);
		tarifTable.put("STELS", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("STELS", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);

		yearTarif = BigDecimal.valueOf(.055);
		halfTarif = BigDecimal.valueOf(.039);
		tarifTable.put("BRP", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("BRP", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
		tarifTable.put("Yamaha", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Yamaha", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);

		yearTarif = BigDecimal.valueOf(.035);
		halfTarif = BigDecimal.valueOf(.025);
		tarifTable.put("Тингер", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("Тингер", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);

		yearTarif = BigDecimal.valueOf(.035);
		halfTarif = BigDecimal.valueOf(.025);
		tarifTable.put("CFMOTO", Insurance.PeriodOfCover.YEAR, yearTarif);
		tarifTable.put("CFMOTO", Insurance.PeriodOfCover.HALF_A_YEAR, halfTarif);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.extas.server.InsuranceCalculator#calcPropInsPremium(ru.extas.model
	 * .Insurance)
	 */
	@Override
	public BigDecimal calcPropInsPremium(Insurance ins) {
		checkArgument(ins != null, "Can't calculate premium. No insurance paramenets.");
		checkArgument(ins.getRiskSum() != null, "Can't calculate premium. No insurance risk sum.");
		checkArgument(ins.getMotorBrand() != null, "Can't calculate premium. No insurance motor brand.");
		checkArgument(ins.getCoverTime() != null, "Can't calculate premium. No period of cover.");

		final BigDecimal tarif = findTarif(ins.getMotorBrand(), ins.getCoverTime());

		if (tarif == null)
			throw new IllegalArgumentException("Unsupported motor brand");

		return ins.getRiskSum().multiply(tarif);
	}

	@Override
	public BigDecimal findTarif(final String motorBrand, final Insurance.PeriodOfCover coverTime) {
		checkArgument(motorBrand != null, "Can't calculate premium. No insurance motor brand.");
		checkArgument(coverTime != null, "Can't calculate premium. No period of cover.");

		final BigDecimal tarif = tarifTable.get(motorBrand, coverTime);

		return tarif;
	}

}
