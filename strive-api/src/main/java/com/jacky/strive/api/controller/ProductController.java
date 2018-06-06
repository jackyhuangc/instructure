package com.jacky.strive.api.controller;

import com.jacky.strive.dao.model.Product;
import com.jacky.strive.service.dto.ProductQueryDto;
import org.springframework.web.bind.annotation.*;
import qsq.biz.scheduler.entity.ResResult;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/query")
    public ResResult query(ProductQueryDto queryDto) {
        return null;
    }

    @GetMapping("/{product_no}")
    public ResResult get(@PathVariable("product_no") String productNo) {
        return null;
    }

    @PostMapping("/add")
    public ResResult add(Product product) {
        return null;
    }

    @PutMapping("/modify/{product_no}")
    public ResResult modify(@PathVariable("product_no") String productNo, Product product) {
        return null;
    }

    @PutMapping("/shelves/{product_no}")
    public ResResult shelves(@PathVariable("product_no") String productNo, boolean shelves) {
        return null;
    }
}
