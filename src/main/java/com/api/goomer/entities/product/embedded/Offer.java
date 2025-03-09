package com.api.goomer.entities.product.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Offer {
    @Column(name = "promotional_description")
    private String promotionalDescription;

    @Column(name = "promotional_price")
    private BigDecimal promotionalPrice;

    @Column(name = "promotional_days")
    private String promotionalDays;

    @Column(name = "promotional_start_time")
    private LocalTime promotionalStartTime;

    @Column(name = "promotional_end_time")
    private LocalTime promotionalEndTime;
}
