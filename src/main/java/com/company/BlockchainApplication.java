package com.company;

import com.company.bean.Blockchain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BlockchainApplication {
    public static void main(String[] args) throws IOException {
        Blockchain.initChain();
        //Blockchain.nodes.add(new Node(address));
        SpringApplication.run(BlockchainApplication.class);
    }
}
