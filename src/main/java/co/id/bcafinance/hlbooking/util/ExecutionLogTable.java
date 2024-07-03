package co.id.bcafinance.hlbooking.util;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:52
@Last Modified 05/05/2024 21:52
Version 1.0
*/

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ExecutionLogTable implements Callable<String> {
    private String[] datax;
    private Exception exc;
    private String flag;
    private Long createdBy;
    private String urlApi;

    public ExecutionLogTable() {
    }

    public ExecutionLogTable(String[] datax, Exception exc, String flag, Long createdBy, String urlApi) {
        this.datax = datax;
        this.exc = exc;
        this.flag = flag;
        this.createdBy = createdBy;
        this.urlApi = urlApi;
    }

    /**
     * Ini adalah method untuk eksekusi call api external buatan kita (bukan third party API contoh SMTP , gateway lain, google / youtube dll)
     * sudah dilengkapi dengan timeout yang di set di function pemanggil nya !!
     * Selama tidak ada balasan dari API yang dipanggil otomatis akan berhenti berdasarkan konfigurasi yang kita set di file properties
     */
    @Override
    public String call() throws Exception {
/** Mainkan Thread sleep untuk mencoba meng-errorkan proses ini, set sleep nya harus lebih besar dari set konfigurasi */
//        Thread.sleep(12000);
        apiExecutioner(datax,exc,flag,createdBy,urlApi);
        return null;
    }

    public void apiExecutioner(String[] datax, Exception exc, String flag, Long createdBy, String urlApi) throws Exception {
        Map<String , Object> mapz = new HashMap<String,Object>();
        mapz.put("className",datax[0]);
        mapz.put("dataRequest",datax[1]);
        mapz.put("createdBy",createdBy);
        mapz.put("errorMessagez",exc.getMessage());

        URL url = new URL (urlApi);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = new JSONObject(mapz).toString();
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}

