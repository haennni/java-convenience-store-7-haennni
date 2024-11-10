package store.util;

import store.domain.FilePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileReader {
    public List<String> readProducts() throws IOException {
        Path path = Path.of(FilePath.Products.filePath());
        return Files.readAllLines(path);
    }

    public List<String> readPromotions() throws IOException {
        Path path = Path.of(FilePath.Promotions.filePath());
        return Files.readAllLines(path);
    }
}
