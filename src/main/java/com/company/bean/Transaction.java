package com.company.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String sender;
    private String receiver;
    private Integer amount;
}
