package com.ra.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String productIdNotFound) {
        super(productIdNotFound);
    }
}
