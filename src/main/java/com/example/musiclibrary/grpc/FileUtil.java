package com.example.musiclibrary.grpc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtil {
    public static void saveReceiptToFile(String receipt, String operationType) {
        File receiptsFolder = new File("receipts");
        if (!receiptsFolder.exists()) {
            receiptsFolder.mkdir();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%s_чек_%s.txt", operationType, timestamp);

        File receiptFile = new File(receiptsFolder, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))) {
            writer.write(receipt);
            System.out.println("Чек успешно сохранён в файл: " + receiptFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка сохранения чека в файл: " + e.getMessage());
        }
    }
}
