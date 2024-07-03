package co.id.bcafinance.hlbooking.util;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 21:53
@Last Modified 05/05/2024 21:53
Version 1.0
*/

import co.id.bcafinance.hlbooking.handler.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class GlobalFunction {
    private Map<String,Object> mapSortValue = new HashMap<>();
    public GlobalFunction() {
        mapSortValue.put("asc","asc");
        mapSortValue.put("desc","desc");
    }

//FUNGSIONAL STANDARD ============================================================================================================ FUNGSIONAL STANDARD
    /** Function untuk set value sort yang ada hanya asc dan desc saja */
    public String checkSortParameter(String sortz)
    {
        sortz = mapSortValue.get(sortz)==null?"asc":sortz;
        return sortz;
    }

    /**
     * Mengconvert Object java ke JSON
     */
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * Digunakan untuk functional download file dll
     * yang bersifat tidak mengembalikan data response
     */
    public void manualResponse(HttpServletResponse response, ResponseEntity<Object> respObject){
        try {
            response.getWriter().write(convertObjectToJson(respObject.getBody()));
            response.setStatus(respObject.getStatusCodeValue());
        } catch (IOException e) {

        }
    }
    public Pageable processingPagination(Integer page, Integer size, String sort, String sortBy, Map<String, String> mapSorting, String defaultSortBy) {
        /** untuk tipe data numeric page dan size , jika diisi dengan huruf akan dihandle  otomatis oleh system dan akan mengirim response 500
         * Namun tetap tercapture di log file secara otomatis dari class GlobalExceptionHandler
         */
        page = (page==null)?0:page;
        /** function yang bersifat global di paging , untuk memberikan default jika data request tidak mengirim format sort dengan benar asc/desc */
        sort   = checkSortParameter(sort);
        Object objSortBy = mapSorting.get(sortBy);
        /** di define berdasarkan map di masing-masing class untuk functional sorting
         *  otomatis memberikan default field primary key dari model tersebut jika data request tidak mengirimkan format sortby yang benar berdasarkan
         *  object mapSorting yang telah di define di class ini
         */
        sortBy = objSortBy==null?defaultSortBy:objSortBy.toString();
        return PageRequest.of(page,
                (size==null)?10:size,
                sort.equals("desc")? Sort.by(sortBy).descending():Sort.by(sortBy)
        );
    }

    /**
     * Response Header untuk download file
     */
    public HttpHeaders headers(String name) {

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + name);
        header.add("Cache-Control", "no-cache, no-store,"
                + " must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    /** saya hardcode pattern nya karena kebutuhan report aja , jadi tidak perlu memasukkan pattern saat menggunakan functional ini */
    public String formatingDateDDMMMMYYYY(){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date());
    }
    public String formatingDateDDMMMMYYYY(LocalDate localDate){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy").
                format(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    public String formatingDateDDMMMMYYYY(Date date){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }

    /** untuk mekanisme casting date beraneka ragam, tidak seperti functional yang dioverload sebelumnya
     * jadi parameter pattern harus diikutkan
     * y – Year (yyyy->1996; yy-> 96)
     * M – Month in year (MMMM->July; MMM->Jul; MM->07)
     * d – Day in month (dd--> 1-31)
     * E – Day name in week (EEE ---> Friday, EE --> Fri)
     * a – AM/PM marker (AM, PM)
     * H – Hour in day (HH--> 0-23)
     * h – Hour in AM/PM (hh--> 1-12)
     * m – Minute in hour (mm---> 0-60)
     * s – Second in minute (ss-->0-60)
     * S – millsecond (SSS--> 138)
     * */
    public String formatingDateDDMMMMYYYY(String strDate,String pattern) throws ParseException {
        return new SimpleDateFormat("dd MMMM yyyy").
                format(new SimpleDateFormat(pattern, Locale.ENGLISH).parse(strDate));
    }
    public String formatingDate(String newPattern){
        return new SimpleDateFormat(newPattern).format(new Date());
    }
    public String formatingDate(LocalDate localDate,String newPattern){
        return new SimpleDateFormat(newPattern).
                format(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    public String formatingDate(Date date,String newPattern){
        return new SimpleDateFormat(newPattern).format(date);
    }
    public String formatingDate(String strDate,String currentPattern,String newPattern) throws ParseException {
        return new SimpleDateFormat(newPattern).
                format(new SimpleDateFormat(currentPattern, Locale.ENGLISH).parse(strDate));
    }

    public String manualEmptyBlankValidation(String strData) throws Exception {
        if("".equals(strData) || " ".equals(strData))
        {
            throw new Exception("Harap Isi Data yang Sesuai .... !!");
        }
        return strData;
    }
    //BATAS UNTUK DATA REQUEST / VALIDASI ERROR ======================================================================= BATAS UNTUK DATA REQUEST / VALIDASI ERROR
    public ResponseEntity<Object> unauthorizedUser(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Anda Tidak Memiliki Akses !!",
                HttpStatus.FORBIDDEN,
                null,
                "X-AUTHO-001", request);
    }

    /** pembuatan kode DR (Data Request) berarti masalah terjadi karena dari sisi client
     mengirimkan request yang tidak valid
     */
    public ResponseEntity<Object> dataNotFoundById(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Data Tidak Ditemukan",
                HttpStatus.BAD_REQUEST,
                null,
                "X-DR-001", request);//FAILED VALIDATION
    }

    /** jika data kiriman dalam bentuk list ternyata kosong , maka akan mengembalikan response ini */
    public ResponseEntity<Object> dataRequestIsEmpty(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Data Tidak Ditemukan",
                HttpStatus.BAD_REQUEST,
                null,
                "X-DR-002", request);
    }

    /** Untuk Filtering, jika data nya berbentuk numeric sedangkan value yang diisi bukan numeric
     * untuk pencarian kolom numeric maka akan melakukan response di function ini
     */
    public ResponseEntity<Object> dataValidationNumericType(HttpServletRequest request)
    {
        return new ResponseHandler().
                generateResponse("DATA FILTER TIDAK SESUAI FORMAT HARUS ANGKA",
                        HttpStatus.BAD_REQUEST,
                        null,
                        "X-DR-003",
                        request);
    }

    /** pembuatan kode DR (Data Request) berarti masalah terjadi karena dari sisi client
     mengirimkan request yang tidak valid
     */
    public ResponseEntity<Object> datalistNotFoundByPaging(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Data Tidak Ditemukan",
                HttpStatus.BAD_REQUEST,
                null,
                "X-DR-004", request);
    }

    /** pembuatan kode DR (Data Request) berarti masalah terjadi karena dari sisi client
     mengirimkan request yang tidak valid
     */
    public ResponseEntity<Object> contentTypeCSV(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Ekstensi File harus CSV",
                HttpStatus.BAD_REQUEST,
                null,
                "X-DR-005", request);
    }

    /** pembuatan kode DR (Data Request) berarti masalah terjadi karena dari sisi client
     mengirimkan request yang tidak valid
     */
    public ResponseEntity<Object> failedToUploadFile(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("File gagal diupload / diproses",
                HttpStatus.BAD_REQUEST,
                null,
                "X-DR-006", request);
    }


//BATAS UNTUK INTERNAL SERVER ERROR ======================================================================= BATAS UNTUK INTERNAL SERVER ERROR
    /**
     * Server gagal memproses data
     * ISE = Internal Server Error
     */
    public ResponseEntity<Object> downloadFailed(HttpServletRequest request)
    {
        return new ResponseHandler().
                generateResponse("DATA GAGAL DI DOWNLOAD !!",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        "X-ISE-001",
                        request);
    }
    /** Failed to download template
     */
    public ResponseEntity<Object> failedToDownloadTemplate(HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Request Template Gagal",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                "X-ISE-002", request);
    }
}

