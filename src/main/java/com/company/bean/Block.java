package com.company.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Block {
    private Integer proof;
    private LocalDateTime timestamp;
    private String previousHash;
    private Integer index;
    private List<Transaction> transactions;

    public Block(String previousHash,int proof){
        this.timestamp = LocalDateTime.now();
        this.proof=proof;
        this.previousHash=previousHash;
        this.index=Blockchain.blocks.size();
        this.transactions = new ArrayList<>(Blockchain.transactions);
        Blockchain.transactions.clear();
    }

    public String hash() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(this.proof.toString().getBytes());
        messageDigest.update(this.index.toString().getBytes());
        messageDigest.update(this.timestamp.toString().getBytes());
        messageDigest.update(this.previousHash.getBytes());
        for(Transaction transaction : transactions){
            messageDigest.update(transaction.getAmount().toString().getBytes());
            messageDigest.update(transaction.getReceiver().getBytes());
            messageDigest.update(transaction.getSender().getBytes());
        }
        return Blockchain.bytesToHex(messageDigest.digest());
    }

}
