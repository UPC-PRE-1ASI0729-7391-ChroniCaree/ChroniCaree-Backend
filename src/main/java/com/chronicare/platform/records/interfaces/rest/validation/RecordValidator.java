package com.chronicare.platform.records.interfaces.rest.validation;

import com.chronicare.platform.records.interfaces.rest.resources.CreateRecordResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecordValidator {

    public List<String> validateCreateRecordResource(CreateRecordResource resource) {
        List<String> errors = new ArrayList<>();

        if (resource.tenantId() == null || resource.tenantId() <= 0) {
            errors.add("tenantId must be a positive number");
        }

        if (resource.patientId() == null || resource.patientId() <= 0) {
            errors.add("patientId must be a positive number");
        }

        if (resource.authorId() == null || resource.authorId() <= 0) {
            errors.add("authorId must be a positive number");
        }

        if (resource.type() == null) {
            errors.add("type is required");
        }

        if (resource.title() == null || resource.title().trim().isEmpty()) {
            errors.add("title is required");
        } else if (resource.title().length() > 500) {
            errors.add("title must not exceed 500 characters");
        }

        if (resource.content() == null || resource.content().trim().isEmpty()) {
            errors.add("content is required");
        }

        if (resource.attachments() != null && resource.attachments().size() > 10) {
            errors.add("maximum 10 attachments allowed");
        }

        if (resource.tags() != null && resource.tags().size() > 20) {
            errors.add("maximum 20 tags allowed");
        }

        return errors;
    }
}
