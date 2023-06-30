package com.company.bean;

import com.company.controller.dto.BlockDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Blockchain {
    public static String nodeAddress = UUID.randomUUID().toString().replace("-","");

    public static List<Block> blocks = new ArrayList<>();
    public static List<Node> nodes = new ArrayList<>();
    public static List<Transaction> transactions = new ArrayList<>();

    public static void initChain(){
        Block block = new Block("0",1);
        blocks.add(block);
    }

    public static boolean chainIsValid() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        for(int i=blocks.size()-1;i>=1;i--){
            Block currentBlock = blocks.get(i);
            Block prevBlock = blocks.get(i-1);
            if (!currentBlock.getPreviousHash().equals(prevBlock.hash())) return false;
            double proof = Math.pow(blocks.get(i).getProof(),2)-Math.pow(blocks.get(i-1).getProof(),2);
            messageDigest.update(String.valueOf(proof).getBytes());
            String data = bytesToHex(messageDigest.digest());
            if (!data.substring(0,4).equals("0000")) return false;
        }
        return true;
    }

    public static Block getPreviousBlock(){
        if(blocks.isEmpty()){
            return null;
        }
        return blocks.get(blocks.size()-1);
    }

    public static int proofOfWork() throws NoSuchAlgorithmException {
        Integer proof=1;
        boolean proofOfWork = false;
        while (!proofOfWork){
            Block prevBlock = Blockchain.getPreviousBlock();
            String data = String.valueOf(Math.pow(proof,2) - Math.pow(prevBlock.getProof(),2));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            if (bytesToHex(hash).substring(0,4).equals("0000")){
                proofOfWork=true;
            }
            else {
                proof++;
            }
        }
        return proof;
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static int addTransaction(Transaction transaction){
        transactions.add(transaction);
        return getPreviousBlock().getIndex()+1; //Transaction will added to this block by the miner
    }

    public static boolean replaceChain() throws IOException {
        List<Block> largestChain=null;
        int maxSize = blocks.size();
        for (Node node : nodes){
            List<Block> blocksFromNode = toBlockList(getChainRequest(node.getAddress()));
            boolean isValid = chainIsValidRequest(node.getAddress());
            if(blocksFromNode.size()>maxSize && isValid){
                largestChain=blocksFromNode;
                maxSize=blocksFromNode.size();
            }
        }
        if(largestChain!=null){
            blocks=largestChain;
            return true;
        }
        return false;
    }

    public static List<Block> toBlockList(List<BlockDto> blockDtos){
        List<Block> blocksFromNode = new ArrayList<>();
        for (BlockDto blockDto : blockDtos) {
            String dateTimeString = blockDto.getTimestamp();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime timestamp = LocalDateTime.parse(dateTimeString, formatter);
            Block block = new Block();
            block.setIndex(blockDto.getIndex());
            block.setPreviousHash(blockDto.getPreviousHash());
            block.setProof(blockDto.getProof());
            block.setTimestamp(timestamp);
            blocksFromNode.add(block);
        }
        return blocksFromNode;
    }

    public static List<BlockDto> getChainRequest(String address) throws IOException {
        URL url = new URL("http://"+address+"/get-chain");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status == 200){
            String jsonString = getInputStream(con);
            Type blockListType = new TypeToken<List<BlockDto>>() {}.getType();
            List<BlockDto> blockDtos = new Gson().fromJson(jsonString, blockListType);
            return blockDtos;
        }
        return null;
    }

    public static boolean chainIsValidRequest(String address) throws IOException {
        URL url = new URL("http://"+address+"/chain-is-valid");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status == 200){
            String jsonString = getInputStream(con);
            Type blockListType = new TypeToken<Boolean>() {}.getType();
            Boolean isValid = new Gson().fromJson(jsonString, blockListType);
            return isValid;
        }
        return false;
    }

    public static String getInputStream(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();
        String jsonString = content.toString();
        return jsonString;
    }
}
