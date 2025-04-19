package com.example.zaitoona.dataaccess;

import com.example.zaitoona.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDA implements IProudctDA {
    private List<OliveOilProduct> products = new ArrayList<>();
    public ProductDA(){
        products.add(new OliveOilProduct(1,"biona","Extra Virgin",10,30, R.drawable.ev1,0.5));
        products.add(new OliveOilProduct(2,"Jaypore Olives","Extra Virgin",15,35, R.drawable.ev2,0.5));
        products.add(new OliveOilProduct(3,"OLIVADO","Extra Virgin",10,10, R.drawable.ev3,0.5));
        products.add(new OliveOilProduct(4,"Anbiguina","Extra Virgin",10,30, R.drawable.ev4,0.4));
        products.add(new OliveOilProduct(5,"BEAOLIVA","Pure",25,60, R.drawable.po2,1));
        products.add(new OliveOilProduct(6,"pams","Pure",20,30, R.drawable.po3,1));
        products.add(new OliveOilProduct(7,"BEAOLIVA","Pomace",20,20, R.drawable.m1,1));
        products.add(new OliveOilProduct(8,"Laila","Pomace",60,10, R.drawable.m2,5));
        products.add(new OliveOilProduct(9,"Del Monte","Pomace",15,40, R.drawable.m3,0.5));
    }


    @Override
    public List<OliveOilProduct> getProducts(String search) {
        List<OliveOilProduct> result = new ArrayList<>();
        for (OliveOilProduct p :products) {
            if (p.getName().toLowerCase().contains(search.toLowerCase()) ||
                    p.getType().toLowerCase().contains(search.toLowerCase()) ||
                    String.valueOf(p.getPrice()).contains(search) ||
                    String.valueOf(p.getQuantity()).contains(search)
                    || String.valueOf(p.getLiters()).contains(search)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<OliveOilProduct> allProducts(){
        return products;
    }

    @Override
    public String[] getTypes() {
        return new String[]{"All Types", "Extra Virgin","Pomace","Pure"};
    }

    public void setProducts(List<OliveOilProduct> products) {
        this.products = products;
    }

    @Override
    public List<OliveOilProduct> getAdvancedFilteredProducts(String searchText, String type, Double minPrice, Double maxPrice, Double minLiter, Double maxLiter) {
        List<OliveOilProduct> result = new ArrayList<>();
        for (OliveOilProduct product : products) {
            if (searchText != null && !searchText.isEmpty()) {
                String query = searchText.toLowerCase();
                if (!product.getName().toLowerCase().contains(query) &&
                        !product.getType().toLowerCase().contains(query))
                    continue;
            }
            if (type != null && !type.equals("All Types") && !type.isEmpty() &&
                    !product.getType().equals(type)) continue;
            if (minPrice != null && product.getPrice() <minPrice)
                continue;
            if (maxPrice != null && product.getPrice() >maxPrice)
                continue;
            if (minLiter != null && product.getLiters() < minLiter)
                continue;
            if (maxLiter != null && product.getLiters() > maxLiter)
                continue;
            result.add(product);
        }
        return result;
    }
}