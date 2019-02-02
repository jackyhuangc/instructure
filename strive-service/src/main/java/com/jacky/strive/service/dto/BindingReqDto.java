package com.jacky.strive.service.dto;

import com.jacky.strive.dao.model.MemberCard;
import com.jacky.strive.dao.model.MemberOrder;
import lombok.Data;

@Data
public class BindingReqDto extends MemberCard {
    private String verifyCode;
    private String verifySeq;
}
