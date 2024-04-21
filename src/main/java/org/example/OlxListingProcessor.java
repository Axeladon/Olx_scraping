package org.example;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OlxListingProcessor {
    private Integer internalSequenceNum;
    private Long olxId;
    private String title;
    private Double price;
    private String link;
    private String description;
}
