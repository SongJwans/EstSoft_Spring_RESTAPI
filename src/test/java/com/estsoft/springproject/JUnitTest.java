package com.estsoft.springproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JUnitTest {
    @Test
    public void test() {
        // given
        int a = 1;
        int b = 2;

        // when
        int sum = a + b;

        // then
        Assertions.assertEquals(3, sum);
        org.assertj.core.api.Assertions.assertThat(sum).isEqualTo(3);
    }
}
