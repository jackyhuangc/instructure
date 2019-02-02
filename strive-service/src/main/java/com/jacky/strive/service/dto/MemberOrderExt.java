package com.jacky.strive.service.dto;

import com.jacky.strive.dao.model.MemberOrder;
import lombok.Data;

@Data
public class MemberOrderExt extends MemberOrder {
    private String productImage;
}
