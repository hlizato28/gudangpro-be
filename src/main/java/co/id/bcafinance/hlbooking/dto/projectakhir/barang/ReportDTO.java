package co.id.bcafinance.hlbooking.dto.projectakhir.barang;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 03/07/2024 10:19
@Last Modified 03/07/2024 10:19
Version 1.0
*/

import java.util.List;

public class ReportDTO {
    private Long idReport;
    private List<ReportBalancingDTO> details;

    public Long getIdReport() {
        return idReport;
    }

    public void setIdReport(Long idReport) {
        this.idReport = idReport;
    }

    public List<ReportBalancingDTO> getDetails() {
        return details;
    }

    public void setDetails(List<ReportBalancingDTO> details) {
        this.details = details;
    }
}

