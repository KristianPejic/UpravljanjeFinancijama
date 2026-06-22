package org.example.sustavzaupravljajeosobnimfinancijama.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private TransactionType type;
}
