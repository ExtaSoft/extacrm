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
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;

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

	private static final Table<String, String, BigDecimal> tarifTable;

	static {
		tarifTable = HashBasedTable.create();
        fillBrandTarif(newHashSet("Kawasaki", "Arctic Cat", "Polaris", "CECTEK"), BigDecimal.valueOf(.045), BigDecimal.valueOf(.032));
        fillBrandTarif(newHashSet("Kawasaki", "Arctic Cat", "Polaris", "CECTEK"), BigDecimal.valueOf(.05), BigDecimal.valueOf(.035), true);

        fillBrandTarif(newHashSet("Baltmotors", "Suzuki", "Honda", "Polar Fox"), BigDecimal.valueOf(.045), BigDecimal.valueOf(.032));
        fillBrandTarif(newHashSet("Baltmotors", "Suzuki", "Honda", "Polar Fox"), BigDecimal.valueOf(.05), BigDecimal.valueOf(.035), true);

        fillBrandTarif(newHashSet("STELS"), BigDecimal.valueOf(.06), BigDecimal.valueOf(.042));
        fillBrandTarif(newHashSet("STELS"), BigDecimal.valueOf(.065), BigDecimal.valueOf(.046), true);

        fillBrandTarif(newHashSet("BRP", "Yamaha"), BigDecimal.valueOf(.055), BigDecimal.valueOf(.039));
        fillBrandTarif(newHashSet("BRP", "Yamaha"), BigDecimal.valueOf(.06), BigDecimal.valueOf(.043), true);

        fillBrandTarif(newHashSet("Тингер"), BigDecimal.valueOf(.035), BigDecimal.valueOf(.025));
        fillBrandTarif(newHashSet("Тингер"), BigDecimal.valueOf(.04), BigDecimal.valueOf(.029), true);

        fillBrandTarif(newHashSet("CFMOTO"), BigDecimal.valueOf(.035), BigDecimal.valueOf(.025));
        fillBrandTarif(newHashSet("CFMOTO"), BigDecimal.valueOf(.04), BigDecimal.valueOf(.029), true);
	}

    private static void fillBrandTarif(HashSet<String> brands, BigDecimal yearTarif, BigDecimal halfTarif) {
        fillBrandTarif(brands, yearTarif, halfTarif, false);

    }

    private static void fillBrandTarif(Set<String> brands, BigDecimal yearTarif, BigDecimal halfTarif, boolean isUsed) {
        for(String brand : brands) {
            tarifTable.put(brand, getTarifKey(Insurance.PeriodOfCover.YEAR, isUsed), yearTarif);
            tarifTable.put(brand, getTarifKey(Insurance.PeriodOfCover.HALF_A_YEAR, isUsed), halfTarif);
        }
    }

    private static String getTarifKey(Insurance.PeriodOfCover periodOfCover, boolean isUsed) {
        return periodOfCover.toString() + (isUsed ? "_used" : "");
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

		final BigDecimal tarif = findTarif(ins.getMotorBrand(), ins.getCoverTime(), ins.isUsedMotor());

		if (tarif == null)
			throw new IllegalArgumentException("Unsupported motor brand");

		return ins.getRiskSum().multiply(tarif);
	}

	@Override
	public BigDecimal findTarif(final String motorBrand, final Insurance.PeriodOfCover coverTime, boolean isUsed) {
		checkArgument(motorBrand != null, "Can't calculate premium. No insurance motor brand.");
		checkArgument(coverTime != null, "Can't calculate premium. No period of cover.");

        return tarifTable.get(motorBrand, getTarifKey(coverTime, isUsed));
	}

}
