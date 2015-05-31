package ru.extas.server.product;

import ru.extas.model.contacts.SalePoint;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProdInstallments;
import ru.extas.model.sale.ProdInsurance;
import ru.extas.model.sale.Product;

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
    public Map<Product.Type, List<Product>> findAvailableProducts(SalePoint salePoint) {
        List<Product> prodList;
        if(salePoint == null)
            prodList = productRepository.findByActiveOrderByNameAsc(true);
        else
            prodList = productRepository.findBySalePoint(salePoint);

        Map<Product.Type, List<Product>> prodMap = newHashMap();
        List<Product> creditList = newArrayList();
        List<Product> installList = newArrayList();
        List<Product> insurList = newArrayList();
        for (Product product : prodList) {
            if(product instanceof ProdCredit)
                creditList.add(product);
            else if(product instanceof ProdInstallments)
                installList.add(product);
            else if(product instanceof ProdInsurance)
                creditList.add(product);
        }
        prodMap.put(Product.Type.CREDIT, creditList);
        prodMap.put(Product.Type.PAYMENT_BY_INSTALLMENTS, installList);
        prodMap.put(Product.Type.INSURANCE, insurList);

        return prodMap;
    }
}
