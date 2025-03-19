package com.synergy.backend.domain.qrCode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.synergy.backend.domain.qrCode.exception.NotGenerateQRCodeException;
import com.synergy.backend.domain.session.exception.DecodingException;
import com.synergy.backend.domain.session.exception.NotMatchedAttendeeCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrService {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;

    public byte[] generateQRCode(String url, Long id, String secretCode) throws WriterException {

        try{
            secretCode = URLEncoder.encode(secretCode, StandardCharsets.UTF_8);
            BitMatrix encode = new MultiFormatWriter()
                    .encode(convertToQueryString(url, id, secretCode), BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", out);

            return out.toByteArray();
        } catch (Exception e) {
            throw new NotGenerateQRCodeException();
        }
    }

    public String decodingSecretCode(String secretCode) {
        try{
            return URLDecoder.decode(secretCode, StandardCharsets.UTF_8);
        } catch (Exception e){
            throw new DecodingException();
        }
    }

    private String convertToQueryString(String url, Long id, String secretCode) {
        return url + "/" + id + "?code=" + secretCode;
    }
}
