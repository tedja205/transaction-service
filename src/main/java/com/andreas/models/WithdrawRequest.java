package com.andreas.models;

import lombok.Data;

@Data
public class WithdrawRequest {
    private int userId;
    private int withdrawMoneyRequest;

}
