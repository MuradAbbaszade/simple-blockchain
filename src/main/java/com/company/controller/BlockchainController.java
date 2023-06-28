package com.company.controller;

import com.company.bean.Block;
import com.company.bean.Blockchain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class BlockchainController {
    @PostMapping("/mine-block")
    public ResponseEntity<Block> mineBlock() throws NoSuchAlgorithmException, IOException {
        Block block = new Block(Blockchain.getPreviousBlock().hash(),Blockchain.proofOfWork());
        Blockchain.blocks.add(block);
        return ResponseEntity.ok(block);
    }
    @GetMapping("/get-chain")
    public ResponseEntity<List<Block>> getChain(){
        return ResponseEntity.ok(Blockchain.blocks);
    }
    @GetMapping("/chain-is-valid")
    public ResponseEntity<Boolean> chainIsValid() throws NoSuchAlgorithmException {
        return ResponseEntity.ok(Blockchain.chainIsValid());
    }
}
