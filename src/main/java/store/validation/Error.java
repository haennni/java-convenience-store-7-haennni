package store.validation;

public enum Error {
    OVER_INVENTORY("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INVALID_FORMAT("ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_INPUT("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."),
    PROMOTION_NOT_FOUND("[ERROR] 존재하지 않는 프로모션입니다."),
    PROMOTION_PRODUCT_NOT_FOUND("[ERROR] 적용 가능한 프로모션이 없는 제품입니다.");

    private final String errorMsg;

    Error(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String errorMsg() {
        return errorMsg;
    }
}
