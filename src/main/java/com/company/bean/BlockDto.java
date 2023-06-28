package com.company.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BlockDto {
    private int proof;
    private String timestamp;
    private String previousHash;
    private int index;
}
