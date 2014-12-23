/**
 *
 */
package ru.extas.server.insurance;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.extas.model.insurance.Insurance;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализация калькулятора страховых премий
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
        fillBrandTarif(newHashSet("Baltmotors", "Suzuki", "Honda", "Kawasaki", "Polaris", "Русская Механика", "SYM", "Тингер"),
                BigDecimal.valueOf(.053), BigDecimal.valueOf(.037));
        fillBrandTarif(newHashSet("Baltmotors", "Suzuki", "Honda", "Kawasaki", "Polaris", "Русская Механика", "SYM", "Тингер"),
                BigDecimal.valueOf(.057), BigDecimal.valueOf(.039), true);

        fillBrandTarif(newHashSet("HSUN (HISUN)"), BigDecimal.valueOf(.054), BigDecimal.valueOf(.038));
        fillBrandTarif(newHashSet("HSUN (HISUN)"), BigDecimal.valueOf(.058), BigDecimal.valueOf(.04), true);

        fillBrandTarif(newHashSet("STELS"), BigDecimal.valueOf(.068), BigDecimal.valueOf(.048));
        fillBrandTarif(newHashSet("STELS"), BigDecimal.valueOf(.073), BigDecimal.valueOf(.052), true);

        fillBrandTarif(newHashSet("CECTEK", "BRP", "Yamaha", "Arctic Cat"), BigDecimal.valueOf(.062), BigDecimal.valueOf(.044));
        fillBrandTarif(newHashSet("CECTEK", "BRP", "Yamaha", "Arctic Cat"), BigDecimal.valueOf(.068), BigDecimal.valueOf(.048), true);

        fillBrandTarif(newHashSet("CFMOTO"), BigDecimal.valueOf(.035), BigDecimal.valueOf(.028));
        fillBrandTarif(newHashSet("CFMOTO"), BigDecimal.valueOf(.045), BigDecimal.valueOf(.032), true);
    }

    private static void fillBrandTarif(final HashSet<String> brands, final BigDecimal yearTarif, final BigDecimal halfTarif) {
        fillBrandTarif(brands, yearTarif, halfTarif, false);

    }

    private static void fillBrandTarif(final Set<String> brands, final BigDecimal yearTarif, final BigDecimal halfTarif, final boolean isUsed) {
        for (final String brand : brands) {
            tarifTable.put(brand, getTarifKey(Insurance.PeriodOfCover.YEAR, isUsed), yearTarif);
            tarifTable.put(brand, getTarifKey(Insurance.PeriodOfCover.HALF_A_YEAR, isUsed), halfTarif);
        }
    }

    private static String getTarifKey(final Insurance.PeriodOfCover periodOfCover, final boolean isUsed) {
        return periodOfCover.toString() + (isUsed ? "_used" : "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calcPropInsPremium(final Insurance ins) {
        checkArgument(ins != null, "Can't calculate premium. No insurance paramenets.");

        final String motorBrand = ins.getMotorBrand();
        final Insurance.PeriodOfCover coverTime = ins.getCoverTime();
        final boolean usedMotor = ins.isUsedMotor();
        final BigDecimal riskSum = ins.getRiskSum();
        return calcPropInsPremium(motorBrand, riskSum, coverTime, usedMotor);
    }

    @Override
    public BigDecimal calcPropInsPremium(String motorBrand, BigDecimal riskSum, Insurance.PeriodOfCover coverTime, boolean usedMotor) {
        checkArgument(riskSum != null, "Can't calculate premium. No insurance risk sum.");
        checkArgument(motorBrand != null, "Can't calculate premium. No insurance motor brand.");
        checkArgument(coverTime != null, "Can't calculate premium. No period of cover.");

        final BigDecimal tarif = findTarif(motorBrand, coverTime, usedMotor);

        if (tarif == null)
            throw new IllegalArgumentException("Unsupported motor brand");

        return riskSum.multiply(tarif);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal findTarif(final String motorBrand, final Insurance.PeriodOfCover coverTime, final boolean isUsed) {
        checkArgument(motorBrand != null, "Can't calculate premium. No insurance motor brand.");
        checkArgument(coverTime != null, "Can't calculate premium. No period of cover.");

        return tarifTable.get(motorBrand, getTarifKey(coverTime, isUsed));
    }

}
