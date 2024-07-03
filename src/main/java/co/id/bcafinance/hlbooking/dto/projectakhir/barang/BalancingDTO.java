package co.id.bcafinance.hlbooking.dto.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 30/06/2024 15:03
@Last Modified 30/06/2024 15:03
Version 1.0
*/

import java.util.Date;
import java.util.List;

public class BalancingDTO {
    private Long idBalancing;
    private Date createdAt;
    private Boolean isApproved;

    private List<DetailBalancingDTO> details;

    public Long getIdBalancing() {
        return idBalancing;
    }

    public void setIdBalancing(Long idBalancing) {
        this.idBalancing = idBalancing;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public List<DetailBalancingDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailBalancingDTO> details) {
        this.details = details;
    }
}

