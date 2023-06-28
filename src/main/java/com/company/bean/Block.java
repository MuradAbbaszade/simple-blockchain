package com.company.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Block {
    private Integer proof;
    private LocalDateTime timestamp;
    private String previousHash;
    private Integer index;

    public Block(String previousHash,int proof){
        this.timestamp = LocalDateTime.now();
        this.proof=proof;
        this.previousHash=previousHash;
        this.index=Blockchain.blocks.size();
    }

    public String hash() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(this.proof.toString().getBytes());
        messageDigest.update(this.index.toString().getBytes());
        messageDigest.update(this.timestamp.toString().getBytes());
        messageDigest.update(this.previousHash.getBytes());
        return Blockchain.bytesToHex(messageDigest.digest());
    }

}
