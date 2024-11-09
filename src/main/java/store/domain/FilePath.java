package store.domain;

public enum FilePath {
    Products("src/main/resources/products.md"),
    Promotions("src/main/resources/promotions.md");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String filePath() {
        return path;
    }
}
