package org.example.submitter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitterTest {
    @Test
    void testSubmitJob() {
        // this is an example test to demonstrate how to create a unit test
        Submitter submitter = new Submitter();
        String result = submitter.submitJob();
        assertEquals("Job submitted", result, "The submitJob method should return 'Job submitted'");
    }
}
