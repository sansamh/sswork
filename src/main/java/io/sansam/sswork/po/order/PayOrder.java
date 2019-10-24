package io.sansam.sswork.po.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PayOrder {

    private Long id;
    private BigDecimal amount;
    private Date createTime;
    private Long userId;


}
