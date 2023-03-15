package com.demo.controller;

import com.demo.model.DemoModel;
import com.demo.model.OutputModel;
import com.demo.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController {

    private final DemoService demoService;

    /**
     * It processes the uploaded file and return the result in json format for all the rows in files
     *
     * @param files multipart file uploaded by user
     * @Return List of DemoModel in json Format
     */
    @PostMapping
    public List<DemoModel> fileUpload(@RequestParam("file") MultipartFile files) {

        return demoService.uploadFile(files);
    }

    /**
     * It processes the uploaded file and return the result excel with additional column if row is valid .
     * Also returns the status as ok if all rows are valid else returns 429
     *
     * @param files multipart file uploaded by user
     * @Return ResponseEntity with file
     */
    @PostMapping("v1")
    public ResponseEntity<Resource> fileUploadV1(@RequestParam("file") MultipartFile files) {

        String filename = "output.xlsx";
        OutputModel outputModel = demoService.uploadFileV1(files);
        InputStreamResource file = new InputStreamResource(outputModel.getByteArrayInputStream());

        return ResponseEntity.status(outputModel.getIsValid() ? HttpStatus.OK : HttpStatus.FAILED_DEPENDENCY).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
