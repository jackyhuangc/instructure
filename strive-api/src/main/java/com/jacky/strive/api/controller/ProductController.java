package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.Product;
import com.jacky.strive.service.ProductService;
import com.jacky.strive.service.dto.ProductQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jacky.strive.common.*;
import com.jacky.strive.common.entity.ResResult;
/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/{product_no}")
    public ResResult get(@PathVariable("product_no") String productNo) {

        Product m = productService.findByProductNo(productNo);
        AssertUtil.notNull(m, "产品不存在");

        return ResResult.success("", m);
    }

    @PostMapping("/create")
    public ResResult create(@RequestBody Product product) {

        Product m = productService.add(product);
        AssertUtil.notNull(m, "添加失败");

        return ResResult.success("", m);
    }

    @PostMapping("/modify/{product_no}")
    public ResResult modify(@PathVariable("product_no") String productNo, @RequestBody Product product) {

        product.setProductNo(productNo);
        Product m = productService.modify(product);
        AssertUtil.notNull(m, "修改失败");

        return ResResult.success("", m);
    }

    @PostMapping("/activate/{product_no}/{active}")
    public ResResult activate(@PathVariable("product_no") String productNo, @PathVariable("active") boolean active) {

        boolean ret = productService.activate(productNo, active);
        AssertUtil.isTrue(ret, "修改失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/delete/{product_no}")
    public ResResult delete(@PathVariable("product_no") String productNo) {

        boolean ret = productService.delete(productNo);
        AssertUtil.isTrue(ret, "删除失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody ProductQueryDto queryDto) {

        PageInfo<Product> productList = productService.findProductList(queryDto);

        return ResResult.success("", productList);
    }

    @GetMapping("/generateNewProductNo")
    public ResResult generateNewProductNo() {

        String ret = productService.generateNewProductNo();

        return ResResult.success("生成ID成功", ret);
    }
}
