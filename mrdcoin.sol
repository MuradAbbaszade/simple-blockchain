// SPDX-License-Identifier: MIT
pragma solidity ^0.8.18;

contract mrdcoin{
    uint public max_mrdcoin=1000000;
    uint public bought=0;
    uint public usd_to_mrdcoin=100;

    mapping (address=>uint) equity_mrdcoins;
    mapping (address=>uint) equity_usd;

    modifier can_buy_mrdcoins(uint invested_usd){
        require((invested_usd*usd_to_mrdcoin)+bought<=max_mrdcoin);
        _;
    }
    modifier can_sell_mrdcoins(address investor,uint mrdcoins){
        require(mrdcoins<=equity_mrdcoins[investor]);
        _;
    }

    function equity_in_mrdcoins(address investor) external view returns (uint) {
    return equity_mrdcoins[investor];
    }

    function equity_in_usd(address investor) external view returns (uint){
        return equity_usd[investor];
    }

    function buy_mrdcoins(address investor,uint usd_invested) external 
    can_buy_mrdcoins(usd_invested){
        uint bought_mrdcoins = usd_invested*usd_to_mrdcoin;
        bought+=bought_mrdcoins;
        equity_mrdcoins[investor]+=bought_mrdcoins;
        equity_usd[investor]=equity_mrdcoins[investor]/100;
    }

    function sell_mrdcoins(address investor,uint mrdcoins) external 
    can_sell_mrdcoins(investor,mrdcoins){
        bought-=mrdcoins;
        equity_mrdcoins[investor]-=mrdcoins;
        equity_usd[investor]=equity_mrdcoins[investor]/100;
    }

}