package ru.rtln.workingnormservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.rtln.workingnormservice.config.ClientConfiguration;
import ru.rtln.workingnormservice.dto.ProductResponse;

@FeignClient(name = "productApiClient", url = "${internal.service.product.url:http://localhost:9104/api/product}", configuration = ClientConfiguration.class)
public interface ProductApiClient {

    @GetMapping(value = "/products/{productId}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") Long productId);

}
