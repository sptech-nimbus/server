package com.chat.chat.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String resourceType, UUID resourceId) {
        super(resourceType + " não encontrado com o id: " + resourceId);
    }
}