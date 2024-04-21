package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = "https://www.olx.pl/elektronika/telefony/smartfony-telefony-komorkowe/iphone/q-u%C5%BCywane-telefony/";
        OlxScrapingProcessor olxScrapingProcessor = OlxScrapingProcessor.getInstance();

        int pagesCount = 1;
        for (int i = 1; i <= pagesCount; i++) {
            if (i == 1)
                olxScrapingProcessor.readPage(url);
            else
                olxScrapingProcessor.readPage(url + "?page=" + i);
        }

        olxScrapingProcessor.printListingsInfo();
    }
}

