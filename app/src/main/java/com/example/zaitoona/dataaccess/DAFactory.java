package com.example.zaitoona.dataaccess;
// factory class
public class DAFactory {
    public static IProudctDA getInstance(){
        return new ProductDA();
    }
}
