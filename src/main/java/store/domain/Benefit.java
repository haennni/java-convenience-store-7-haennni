package store.domain;

public class Benefit {
    private String benefitProductName;
    private int benefitQuantity;
    private int benefitAmount;

    public Benefit(String benefitProductName, int benefitQuantity, int price) {
        this.benefitProductName = benefitProductName;
        this.benefitQuantity = benefitQuantity;
        this.benefitAmount = benefitQuantity * price;
    }

    public void plusBenefitQuantity(int price) {
        benefitQuantity++;
        benefitAmount += price;
    }

    public String benefitProductName() {
        return benefitProductName;
    }

    public int benefitQuantity() {
        return benefitQuantity;
    }

    public int benefitAmount() {
        return benefitAmount;
    }
}
