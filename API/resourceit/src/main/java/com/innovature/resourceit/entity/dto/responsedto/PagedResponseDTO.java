package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagedResponseDTO<T> {
    private List<T> items;
    private int totalPages;
    private long totalItems;
    private int currentPage;

    public PagedResponseDTO(List<T> items, int totalPages, long totalItems, int currentPage) {
        this.items = items;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
    }
}
