package com.github.cstroe.spendhawk.dvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDvo {
    private Long id;
    private String total;
    private List<EntryWithAccountDvo> entries;
}
