package com.synergy.backend.domain.booth.dto.boothParticipateDto.collection;

import com.synergy.backend.domain.booth.dto.boothParticipateDto.BoothParticipateInterestedTechDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class BoothParticipateInterestedTechDtoList {

    private static final int MAX_TECH_COUNT = 6;
    private static final String ETC_LABEL = "기타";

    private final List<BoothParticipateInterestedTechDto> values;

    private BoothParticipateInterestedTechDtoList(List<BoothParticipateInterestedTechDto> values) {
        this.values = values;
    }

    public static BoothParticipateInterestedTechDtoList from(List<BoothParticipateInterestedTechDto> values) {
        return new BoothParticipateInterestedTechDtoList(new ArrayList<>(values));
    }

    public BoothParticipateInterestedTechDtoList convertTop6WithEtc(Long boothId) {
        if (values.size() <= MAX_TECH_COUNT) {
            return new BoothParticipateInterestedTechDtoList(values);
        }

        List<BoothParticipateInterestedTechDto> sortedValues = sortListOrderByDesc();
        List<BoothParticipateInterestedTechDto> result = new ArrayList<>(sortedValues.subList(0, MAX_TECH_COUNT));
        addEtc(boothId, sortedValues, result);

        return new BoothParticipateInterestedTechDtoList(result);
    }

    private List<BoothParticipateInterestedTechDto> sortListOrderByDesc() {
        return values.stream()
                .sorted(Comparator.comparingLong(BoothParticipateInterestedTechDto::getAttendeeCount)
                .reversed())
                .toList();
    }

    private void addEtc(Long boothId, List<BoothParticipateInterestedTechDto> sortedValues, List<BoothParticipateInterestedTechDto> result) {
        long etcCount = sortedValues.subList(MAX_TECH_COUNT, sortedValues.size()).stream()
                .mapToLong(BoothParticipateInterestedTechDto::getAttendeeCount)
                .sum();

        BoothParticipateInterestedTechDto etc = new BoothParticipateInterestedTechDto(boothId, etcCount, ETC_LABEL);
        result.add(etc);
    }
}
