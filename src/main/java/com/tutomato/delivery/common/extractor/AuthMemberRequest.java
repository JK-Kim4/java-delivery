package com.tutomato.delivery.common.extractor;

public record AuthMemberRequest(
    Long id,
    String account
) {

    public static AuthMemberRequest fromIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("identifier must not be null or blank");
        }

        String[] parts = identifier.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                "identifier format must be '{id},{account}' : " + identifier
            );
        }

        Long id;
        try {
            id = Long.parseLong(parts[0].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("identifier id must be a number: " + parts[0], e);
        }

        String account = parts[1].trim();
        if (account.isEmpty()) {
            throw new IllegalArgumentException("identifier account must not be empty");
        }

        return new AuthMemberRequest(id, account);
    }
}
