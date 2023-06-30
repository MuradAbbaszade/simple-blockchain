package com.company.controller;

import com.company.bean.Block;
import com.company.bean.Blockchain;
import com.company.bean.Node;
import com.company.bean.Transaction;
import com.company.controller.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class BlockchainController {

    @PostMapping("/mine-block")
    public ResponseEntity<Block> mineBlock() throws NoSuchAlgorithmException, IOException {
        Blockchain.addTransaction(new Transaction(Blockchain.nodeAddress,"Miner",1));
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

    @PostMapping("/add-transaction")
    public ResponseEntity addTransaction(@RequestBody Transaction transaction){
        int index = Blockchain.addTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(index);
    }

    @PostMapping("/add-nodes")
    public ResponseEntity addNodes(@RequestBody List<String> addresses){
        if(addresses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found");
        }
        for(String address : addresses){
            Blockchain.nodes.add(new Node(address));
        }
        return ResponseEntity.ok(Blockchain.nodes);
    }

    @GetMapping("/replace-chain")
    public ResponseEntity<ResponseDto<List<Block>>> replaceChain() throws IOException {
        boolean replaced = Blockchain.replaceChain();
        if (replaced) return ResponseEntity.ok(new ResponseDto<List<Block>>(Blockchain.blocks,"Chain replaced"));
        return ResponseEntity.ok(new ResponseDto<List<Block>>(Blockchain.blocks,"Chain not replaced"));
    }

}
