package ru.extas.server.product;

import ru.extas.model.contacts.SalePoint;
import ru.extas.model.product.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * @author Valery Orlov
 *         Date: 13.05.2015
 *         Time: 13:40
 */
public class ProductRepositoryImpl implements ProductService {

    @Inject
    private ProductRepository productRepository;

    @Override
    public Map<Product.Type, List<Product>> findAvailableProducts(final SalePoint salePoint) {
        final List<Product> prodList;
        if(salePoint == null)
            prodList = productRepository.findByActiveOrderByNameAsc(true);
        else
            prodList = productRepository.findBySalePoint(salePoint);

        final Map<Product.Type, List<Product>> prodMap = newHashMap();
        final List<Product> creditList = newArrayList();
        final List<Product> installList = newArrayList();
        final List<Product> insurList = newArrayList();
        final List<Product> hireList = newArrayList();
        for (final Product product : prodList) {
            if(product instanceof ProdCredit)
                creditList.add(product);
            else if(product instanceof ProdInstallments)
                installList.add(product);
            else if(product instanceof ProdInsurance)
                creditList.add(product);
            else if(product instanceof ProdHirePurchase)
                hireList.add(product);
        }
        prodMap.put(Product.Type.CREDIT, creditList);
        prodMap.put(Product.Type.PAYMENT_BY_INSTALLMENTS, installList);
        prodMap.put(Product.Type.INSURANCE, insurList);
        prodMap.put(Product.Type.HIRE_PURCHASE, hireList);

        return prodMap;
    }
}
