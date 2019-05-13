package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.MemberAddress;
import com.jacky.strive.dao.model.MemberVoucher;
import com.jacky.strive.service.dto.MemberQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jacky.strive.common.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc
 **/
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class VoucherService {

    @Autowired
    MemberVoucherDao memberVoucherDao;

    @Autowired
    KeyValueDao keyValueDao;

    public MemberVoucher add(MemberVoucher memberVoucher) {

        memberVoucher.setCreatedAt(DateUtil.now());
        int ret = memberVoucherDao.insert(memberVoucher);
        AssertUtil.isTrue(ret > 0, "添加代价券失败");

        return ret > 0 ? memberVoucher : null;
    }

    public boolean activate(String voucherNo, boolean active) {
        Example example = new Example(MemberVoucher.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("voucherNo", voucherNo);

        MemberVoucher voucher = findByVoucherNo(voucherNo);
        AssertUtil.notNull(voucher, "代金券不存在");

        voucher.setVoucherValid(active);

        int ret = memberVoucherDao.updateByExampleSelective(voucher, example);
        return ret > 0;
    }

    public boolean delete(String voucherNo) {
        Example example = new Example(MemberVoucher.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("voucherNo", voucherNo);

        MemberVoucher voucher = findByVoucherNo(voucherNo);
        AssertUtil.notNull(voucher, "代金券不存在");

        int ret = memberVoucherDao.deleteByExample(example);
        return ret > 0;
    }

    public MemberVoucher findByVoucherNo(String voucherNo) {
        Example example = new Example(MemberVoucher.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("voucherNo", voucherNo);

        MemberVoucher voucher = memberVoucherDao.selectOneByExample(example);

        return voucher;
    }

    public PageInfo<MemberVoucher> findVoucherList(MemberQueryDto queryDto) {

        Page<MemberVoucher> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(MemberVoucher.class);

        Example.Criteria criteria2 = example.createCriteria();

        criteria2.andEqualTo("memberNo", queryDto.getMemberNo());

        example.and(criteria2);
        example.setOrderByClause("created_at desc");
        List<MemberVoucher> memberList = memberVoucherDao.selectByExample(example);

        PageInfo<MemberVoucher> pageInfo = new PageInfo<>(memberList);

        return pageInfo;
    }

    public String generateNewVoucherNo() {

        String maxVoucherNo = keyValueDao.getDynamicResult("SELECT max(voucher_no) FROM `member_voucher`");

        if (!StringUtil.isEmtpy(maxVoucherNo)) {
            maxVoucherNo = "V" + String.format("%06d", Integer.valueOf(Integer.parseInt(maxVoucherNo.substring(1)) + 1));
        } else {
            maxVoucherNo = "V000001";
        }
        return maxVoucherNo;
    }
}