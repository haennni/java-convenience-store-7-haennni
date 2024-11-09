package store.domain;

public class Promotion {
    private String promotionName;
    private int promotionBuy;
    private int promotionGet;
    private String startDate;
    private String endDate;

    public Promotion(String promotionName, int promotionBuy, int promotionGet, String startDate, String endDate) {
        this.promotionName = promotionName;
        this.promotionBuy = promotionBuy;
        this.promotionGet = promotionGet;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String promotionName() {
        return promotionName;
    }

    public int promotionBuy() {
        return promotionBuy;
    }

    public int promotionGet() {
        return promotionGet;
    }

    public String promotionStartDate() {
        return startDate;
    }

    public String promotionEndDate() {
        return endDate;
    }
}
