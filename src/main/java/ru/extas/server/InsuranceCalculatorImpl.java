/**
 *
 */
package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.extas.model.Insurance;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализация калькулятора страховых премий
 *
 * @author Valery Orlov
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class InsuranceCalculatorImpl implements InsuranceCalculator {

    private static final ArrayList<String> tarif055Brands = newArrayList("BRP", "Yamaha");
    private static final ArrayList<String> tarif045Btands = newArrayList("CFMOTO", "Kawasaki", "Arctic Cat", "Polaris", "CECTEK", "Baltmotors",
            "Suzuki", "Honda", "Polar Fox");
    private static final ArrayList<String> tarig06Brands = newArrayList("STELS");

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
        checkArgument(ins.getMotorBrand() != null, "Can't calculate premium. No insurance motor brand.");
        checkArgument(ins.getRiskSum() != null, "Can't calculate premium. No insurance risk sum.");

        BigDecimal tarif;
        if (tarif055Brands.contains(ins.getMotorBrand()))
            tarif = BigDecimal.valueOf(.055);
        else if (tarif045Btands.contains(ins.getMotorBrand()))
            tarif = BigDecimal.valueOf(.045);
        else if (tarig06Brands.contains(ins.getMotorBrand()))
            tarif = BigDecimal.valueOf(.06);
        else
            throw new IllegalArgumentException("Unsupported motor brand");

        return ins.getRiskSum().multiply(tarif);
    }

}
