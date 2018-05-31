package com.ftec.modules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.exceptions.NotEnoughCreditsException;
import com.ftec.utils.Logger;
import com.ftec.resources.Resources;
import com.ftec.resources.Stocks;
import com.ftec.utils.RequestsHelper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ArbitrageModule {
    private final Resources resources;

    private final double serviceFee = 0.25;

    @Autowired
    public ArbitrageModule(Resources resources) {
        this.resources = resources;
    }

    public List<ArbitrageWindow> processRequest(double minVolume, double minPercent, Stocks[] stocks, double orderVolume, boolean isOrderVolume, long userId, boolean updateSession) throws NotEnoughCreditsException, Exception {
//        if (!user.isTrial() && user.getBalance()<serviceFee){
//            Logger.log("User("+user.getLogin()+") tried to use arbitrage module while having not enough credits("+user.getBalance()+")");
//            throw new NotEnoughCreditsException("Not enough credits");
//        }
        List<ArbitrageWindow> windows = getWindows(minVolume, minPercent, stocks, orderVolume, isOrderVolume);
        if(windows==null) throw  new Exception("Module encountered error");
        windows.removeIf(arbitrageWindow -> arbitrageWindow.getProfitPercent() < minPercent || (isOrderVolume && arbitrageWindow.getVolumeOnBuy()<orderVolume) || (isOrderVolume && arbitrageWindow.getVolumeOnSell()<orderVolume));
//        if(windows.size()>0 && !user.isTrial() && windows.stream().anyMatch(arbitrageWindow -> !arbitrageWindow.isLockedOnSell()&&!arbitrageWindow.isLockedOnBuy())) {
//            user.addToBalance(-1*serviceFee);
//            processResults(user);
//        }
        return windows;
    }

    private List<ArbitrageWindow> getWindows(double minVolume, double minPercent, Stocks[] stocks, double orderVolume, boolean isOrderVolume){
        ObjectMapper om = new ObjectMapper();
        StringBuilder stocksString = new StringBuilder();
        for(Stocks stock:stocks){
            stocksString.append("stocks=").append(stock).append("&");
        }
        stocksString.deleteCharAt(stocksString.length()-1);
        String getWindows = "/API/arbitrage/getWindows";
        String answer = makeArbitrageRequest(resources.endpoints.getBotsModule()+ getWindows, new ArrayList<NameValuePair>(){
            {
                add(new BasicNameValuePair("minVolume",""+minVolume));
                add(new BasicNameValuePair("minPercent", ""+minPercent));
                add(new BasicNameValuePair("orderVolume", ""+((isOrderVolume)?orderVolume:0)));
                add(new BasicNameValuePair("",stocksString.toString()));
            }
        });
        try {
            return om.readValue(answer, new TypeReference<List<ArbitrageWindow>>(){});
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException("While mapping values:"+answer, e, true);
        }
        return null;
    }

    private String makeArbitrageRequest(String url, List<NameValuePair> params){
        try {
            StringBuilder urlParams = new StringBuilder();
            for (int i=0; i<params.size(); i++) {
                if(params.get(i).getName().isEmpty()) urlParams.append(params.get(i).getValue());
                else urlParams.append(params.get(i).getName()).append("=").append(params.get(i).getValue());
                if(i<params.size()) urlParams.append("&");
            }
            List<NameValuePair> headers = new ArrayList<NameValuePair>() {
                {
                    add(new BasicNameValuePair(resources.botsModuleParamName, resources.botsModuleAPISecret));
                }
            };
            return RequestsHelper.getHttp(url + "?" + urlParams.toString(), headers);
        }catch (Exception e){
            Logger.logException("While sending request to arbitrage server module server", e, true);
        }
        return null;
    }



    public class ArbitrageWindow{
        private String pairName;
        private Stocks stockToBuy;
        private double priceToBuy;
        private Stocks stockToSell;
        private double priceToSell;
        private boolean lockedOnBuy;
        private boolean lockedOnSell;
        private double volumeOnBuy;
        private double volumeOnSell;
        private double profitPercent;

        public ArbitrageWindow() {
        }

        public ArbitrageWindow(String pairName, Stocks stockToBuy, double priceToBuy, Stocks stockToSell, double priceToSell, boolean lockedOnBuy, boolean lockedOnSell, double volumeOnBuy, double volumeOnSell, double profitPercent) {
            this.pairName = pairName;
            this.stockToBuy = stockToBuy;
            this.priceToBuy = priceToBuy;
            this.stockToSell = stockToSell;
            this.priceToSell = priceToSell;
            this.lockedOnBuy = lockedOnBuy;
            this.lockedOnSell = lockedOnSell;
            this.volumeOnBuy = volumeOnBuy;
            this.volumeOnSell = volumeOnSell;
            this.profitPercent = profitPercent;
        }

        @Override
        public String toString() {
            return "ArbitrageWindow{" +
                    "pairName='" + pairName + '\'' +
                    ", stockToBuy=" + stockToBuy +
                    ", priceToBuy=" + priceToBuy +
                    ", stockToSell=" + stockToSell +
                    ", priceToSell=" + priceToSell +
                    ", lockedOnBuy=" + lockedOnBuy +
                    ", lockedOnSell=" + lockedOnSell +
                    ", volumeOnBuy=" + volumeOnBuy +
                    ", volumeOnSell=" + volumeOnSell +
                    ", profitPercent=" + profitPercent +
                    '}';
        }

        public boolean isLockedOnBuy() {
            return lockedOnBuy;
        }

        public void setLockedOnBuy(boolean lockedOnBuy) {
            this.lockedOnBuy = lockedOnBuy;
        }

        public boolean isLockedOnSell() {
            return lockedOnSell;
        }

        public void setLockedOnSell(boolean lockedOnSell) {
            this.lockedOnSell = lockedOnSell;
        }

        public String getPairName() {
            return pairName;
        }

        public void setPairName(String pairName) {
            this.pairName = pairName;
        }

        public Stocks getStockToBuy() {
            return stockToBuy;
        }

        public void setStockToBuy(Stocks stockToBuy) {
            this.stockToBuy = stockToBuy;
        }

        public double getPriceToBuy() {
            return priceToBuy;
        }

        public void setPriceToBuy(double priceToBuy) {
            this.priceToBuy = priceToBuy;
        }

        public Stocks getStockToSell() {
            return stockToSell;
        }

        public void setStockToSell(Stocks stockToSell) {
            this.stockToSell = stockToSell;
        }

        public double getPriceToSell() {
            return priceToSell;
        }

        public void setPriceToSell(double priceToSell) {
            this.priceToSell = priceToSell;
        }

        public double getVolumeOnBuy() {
            return volumeOnBuy;
        }

        public void setVolumeOnBuy(double volumeOnBuy) {
            this.volumeOnBuy = volumeOnBuy;
        }

        public double getVolumeOnSell() {
            return volumeOnSell;
        }

        public void setVolumeOnSell(double volumeOnSell) {
            this.volumeOnSell = volumeOnSell;
        }

        public double getProfitPercent() {
            return profitPercent;
        }

        public void setProfitPercent(double profitPercent) {
            this.profitPercent = profitPercent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArbitrageWindow window = (ArbitrageWindow) o;
            return Objects.equals(pairName, window.pairName) &&
                    stockToBuy == window.stockToBuy &&
                    stockToSell == window.stockToSell;
        }

        @Override
        public int hashCode() {

            return Objects.hash(pairName, stockToBuy, stockToSell);
        }
    }
}
