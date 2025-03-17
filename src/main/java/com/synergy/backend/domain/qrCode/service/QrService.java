package com.synergy.backend.domain.qrCode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.synergy.backend.domain.qrCode.exception.NotGenerateQRCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrService {

    private final int width = 200;
    private final int height = 200;

    public byte[] generateQRCode(String url, String secretCode) throws WriterException {

        BitMatrix encode = new MultiFormatWriter()
                .encode(convertToQueryString(url, secretCode), BarcodeFormat.QR_CODE, width, height);

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", out);

            return out.toByteArray();
        } catch (IOException e) {
            throw new NotGenerateQRCodeException();
        }
    }

    private String convertToQueryString(String url, String secretCode) {
        return url + "&secretCode=" + secretCode;
    }
}
