package com.synergy.backend.module;

import jakarta.validation.constraints.NotNull;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TestExecutionTimeListener extends AbstractTestExecutionListener {

    private long classStartTime;
    private long methodStartTime;

    private static long totalExecutionTime = 0;
    private static long totalMethodTime = 0;
    private static int totalTestClasses = 0;

    @Override
    public void beforeTestClass(@NotNull TestContext testContext) {
        classStartTime = System.currentTimeMillis();
    }

    @Override
    public void beforeTestMethod(@NotNull TestContext testContext) {
        methodStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterTestMethod(@NotNull TestContext testContext) {
        long methodEndTime = System.currentTimeMillis();
        long methodExecutionTime = methodEndTime - methodStartTime;
        totalMethodTime += methodExecutionTime;

        System.out.println("📍 테스트 메서드 실행 시간: " + methodExecutionTime + "ms");
    }

    @Override
    public void afterTestClass(@NotNull TestContext testContext) {
        long classEndTime = System.currentTimeMillis();
        long classExecutionTime = classEndTime - classStartTime;
        totalExecutionTime += classExecutionTime;
        totalTestClasses++;

        System.out.println("✅ 클래스 실행 전체 시간 (컨텍스트 포함): " + classExecutionTime + "ms");
        System.out.println("⏱ 누적 메서드 실행 시간: " + totalMethodTime + "ms");
        System.out.println("🧪 컨텍스트 초기화 및 기타 오버헤드: " + (classExecutionTime - totalMethodTime) + "ms");

        // 전체 요약은 마지막 클래스일 때만 출력 (이 예시에서는 그냥 매번 출력)
        if (totalTestClasses > 0) {
            System.out.println("\n==================== 테스트 실행 총 요약 ====================");
            System.out.println("📦 전체 테스트 클래스 수: " + totalTestClasses);
            System.out.println("🧪 총 메서드 수행 시간 합계: " + totalMethodTime + "ms");
            System.out.println("🚀 총 컨텍스트 + 오버헤드 포함 시간: " + totalExecutionTime + "ms");
            System.out.println("⚡ 순수 컨텍스트 + 기타 비용: " + (totalExecutionTime - totalMethodTime) + "ms");
            System.out.println("============================================================\n");
        }
    }
}
