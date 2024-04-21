package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OlxScrapingProcessor {
    private static OlxScrapingProcessor instance;
    private static List<OlxListingProcessor> olxListingProcessorsList;
    private String mainUrl;
    private static int salesCounter;

    private OlxScrapingProcessor() {
        olxListingProcessorsList = new ArrayList<>();
        mainUrl = "https://www.olx.pl/";
        salesCounter = 0;
    }

    public static OlxScrapingProcessor getInstance() {
        if (instance == null) {
            instance = new OlxScrapingProcessor();
        }
        return instance;
    }

    public void printListingsInfo() {
        for (OlxListingProcessor item : olxListingProcessorsList) {
            System.out.println("#" + item.getInternalSequenceNum() + "   ID: " + item.getOlxId());
            System.out.println("Title: " + item.getTitle());
            System.out.println("Price: " + item.getPrice() + " zl");
            System.out.println("Link: " + mainUrl + item.getLink());
            System.out.println("Description: " + item.getDescription());
            System.out.println();
        }
    }

    public void readPage(String url) throws IOException {
        Document websitePage = Jsoup.connect(url).get();
        Elements saleOffers = websitePage.select("div[data-cy=\"l-card\"][data-testid=\"l-card\"]");

        for (Element saleOffer : saleOffers) {
            String auctionDescription = "to see description unblock the method, but remember, OLX blocks Web scraping";
            auctionDescription = getDescription(saleOffer);

            OlxListingProcessor olxScraping = OlxListingProcessor.builder()
                    .internalSequenceNum(++salesCounter)
                    .title(getTitle(saleOffer))
                    .price(getPrice(saleOffer))
                    .link(getLink(saleOffer))
                    .description(auctionDescription)
                    .olxId(getOlxId(saleOffer))
                    .build();

            olxListingProcessorsList.add(olxScraping);
        }
    }

    private String getTitle(Element saleOffer) {
        Element titleElement = saleOffer.selectFirst("h6.css-16v5mdi.er34gjf0");
        if (titleElement != null) {
            return titleElement.text();
        }
        return "";
    }

    private double getPrice(Element saleOffer) {
        Element priceElement = saleOffer.selectFirst("p[data-testid=ad-price].css-10b0gli.er34gjf0");
        if (priceElement != null
                && priceElement.text().matches(".*\\d.*")) {

            String[] priceStr = priceElement.text()
                    .replaceFirst("(\\d)\\s(\\d)", "$1$2")
                    .split(" ");
            return Double.parseDouble(priceStr[0].replaceFirst(",", "."));
        }
        return 0.0;
    }

    private String getLink(Element saleOffer) {
        Element linkElement = saleOffer.selectFirst("a");
        if (linkElement != null) {
            return linkElement.attr("href");
        }
        return "";
    }

    private Long getOlxId(Element saleOffer) {
        String id = saleOffer.attr("id");
        if (!id.isEmpty()) {
            return Long.parseLong(id);
        }
        return 0l;
    }

    private String getDescription(Element saleOffer) throws IOException {
        String link = mainUrl + getLink(saleOffer);
        Document document = Jsoup.connect(link).get();
        Element descriptionElement = document.selectFirst(".css-1t507yq.er34gjf0");
        if (descriptionElement.text().isEmpty())
            return "";
        return descriptionElement.wholeText().replaceAll("\n+", "\n");
    }
}
