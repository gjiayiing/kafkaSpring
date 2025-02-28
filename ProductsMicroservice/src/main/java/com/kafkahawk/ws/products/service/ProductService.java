package com.kafkahawk.ws.products.service;

import com.kafkahawk.ws.products.rest.CreateProductRestModel;

public interface ProductService {
    String createProduct(CreateProductRestModel productRestModel) throws Exception;
}
