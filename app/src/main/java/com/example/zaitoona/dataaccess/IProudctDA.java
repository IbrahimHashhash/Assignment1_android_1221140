package com.example.zaitoona.dataaccess;

import java.util.List;

public interface IProudctDA {
    String[] getTypes();
    List<OliveOilProduct> getProducts(String search);
    List<OliveOilProduct> allProducts();
    List<OliveOilProduct> getAdvancedFilteredProducts(String searchText,String type, Double minPrice,Double maxPrice, Double minLiter,Double maxLiter);


}
