package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.common.util.AssertUtil;
import com.jacky.common.util.DateUtil;
import com.jacky.common.util.StringUtil;
import com.jacky.common.*;
import com.jacky.strive.dao.KeyValueDao;
import com.jacky.strive.dao.ProductDao;
import com.jacky.strive.dao.model.Product;
import com.jacky.strive.service.dto.ProductQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc
 **/
@Service
@Scope("prototype")
public class ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    KeyValueDao keyValueDao;


    public Product add(Product product) {

        Product u = findByProductName(product.getProductNo());
        AssertUtil.isTrue(null == u, "产品编号已存在");

        product.setCreatedAt(DateUtil.now());
        if(null==product.getProductAmount()) {
            product.setProductAmount(BigDecimal.ZERO);
        }
        product.setProductOpinionRate(BigDecimal.ZERO);
        int ret = productDao.insert(product);
        return ret > 0 ? product : null;
    }

    public Product modify(Product product) {

        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productNo", product.getProductNo());

        int ret = productDao.updateByExampleSelective(product, example);
        return ret > 0 ? product : null;
    }

    public boolean activate(String productNo, boolean active) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productNo", productNo);

        Product product = findByProductNo(productNo);
        AssertUtil.notNull(product, "商品不存在");

        product.setProductStatus(active);

        int ret = productDao.updateByExampleSelective(product, example);
        return ret > 0;
    }

    public boolean delete(String productNo) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productNo", productNo);

        Product product = findByProductNo(productNo);
        AssertUtil.notNull(product, "商品不存在");

        int ret = productDao.deleteByExample(example);
        return ret > 0;
    }

    public Product findByProductName(String productName) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productNo", productName);
        criteria.orEqualTo("productName", productName);
        criteria.orEqualTo("productAttr", productName);

        Product product = productDao.selectOneByExample(example);
        return product;
    }

    public Product findByProductNo(String productNo) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productNo", productNo);

        Product product = productDao.selectOneByExample(example);

        return product;
    }

    public PageInfo<Product> findProductList(ProductQueryDto queryDto) {

        Page<Product> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(Product.class);
//        Example.Criteria criteria1 = example.createCriteria();
//        criteria1.andEqualTo("productStatus", true);
//        criteria1.orIsNull("productStatus");

        Example.Criteria criteria2 = example.createCriteria();

        String condition = "%%";
        if (null != queryDto.getProductNo()) {
            condition = "%" + queryDto.getProductNo() + "%";
        }

        criteria2.andLike("productNo", condition);
        criteria2.orLike("productName", condition);
        criteria2.orLike("productAttr", condition);

        example.and(criteria2);
        example.setOrderByClause("created_at desc");
        List<Product> productList = productDao.selectByExample(example);

        PageInfo<Product> pageInfo = new PageInfo<>(productList);

        return pageInfo;
    }

    public String generateNewProductNo() {

        String maxProductNo = keyValueDao.getDynamicResult("SELECT max(product_no) FROM `product`");

        if (!StringUtil.isEmtpy(maxProductNo)) {
            maxProductNo = "G" + String.format("%06d", Integer.valueOf(Integer.parseInt(maxProductNo.substring(1)) + 1));
        } else {
            maxProductNo = "G000001";
        }

        return maxProductNo;
    }
}