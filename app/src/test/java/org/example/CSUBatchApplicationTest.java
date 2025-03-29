package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CSUBatchApplicationTest {
    @Test
    void testMainRunsWithoutException() {
        assertDoesNotThrow(() -> CSUBatchApplication.main(new String[]{}));
    }
}
