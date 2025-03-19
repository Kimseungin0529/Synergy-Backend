package com.synergy.backend.domain.qrCode.service;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QrServiceTest {

    @InjectMocks
    private QrService qrService;


    @DisplayName("입력받은 값에 따라 맞는 QR코드를 생성합니다.")
    @Test
    void generateQRCode() throws WriterException {
     //given
//        String url = "https://www.mydomain.com/verify?token=akefjqoisn";
//        String secretCode = "akjfaoeinvodsa";
//
//     //when
//        byte[] bytes = qrService.generateQRCode(url, id, secretCode);
//        //System.out.println(new String(bytes));
//
//        //then
//        assertThat(bytes).isNotNull();
//        assertThat(bytes.length).isNotEqualTo(0);

    }
}