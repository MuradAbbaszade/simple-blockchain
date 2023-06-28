package com.company;

import com.company.bean.Block;
import com.company.bean.Blockchain;
import com.company.bean.Node;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BlockchainApplication {
    public static void main(String[] args) throws IOException {
        Blockchain.replaceChain();
        Blockchain.initChain();
        Blockchain.nodes.add(new Node("sadasdads"));
        SpringApplication.run(BlockchainApplication.class);
    }
}
