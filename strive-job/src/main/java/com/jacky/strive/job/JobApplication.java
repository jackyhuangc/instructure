package com.jacky.strive.job;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.MemberCharge;
import com.jacky.strive.service.ChargeService;
import com.jacky.strive.service.dto.ChargeQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobApplication {


    public static void main(String[] args) {
        //SpringApplication.run(JobApplication.class, args);


        while (true) {
            updateCharge();
        }
    }

    public static void updateCharge() {
        ChargeService chargeService = new ChargeService();

        ChargeQueryDto chargeQueryDto = new ChargeQueryDto();
        chargeQueryDto.setChargeType(0);
        chargeQueryDto.setChargeStatus(2);
        PageInfo<MemberCharge> pageInfo = chargeService.findMemberChargeList(chargeQueryDto);

        pageInfo.getList().stream().forEach(charge -> {

            // 处理中的订单，需要从通道去查询一下，看是否支付成功
            if (charge.getChargeStatus().equals((2))) {

                // 去通道查询

                // 更新为成功或失败
                chargeService.activate(charge.getChargeNo(), 0);
            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
