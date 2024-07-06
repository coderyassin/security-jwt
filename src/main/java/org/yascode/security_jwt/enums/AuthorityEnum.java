package org.yascode.security_jwt.enums;

public enum AuthorityEnum {
    READ_PRIVILEGE("READ_PRIVILEGE"),
    WRITE_PRIVILEGE("WRITE_PRIVILEGE"),
    DELETE_PRIVILEGE("DELETE_PRIVILEGE"),
    UPDATE_PRIVILEGE("UPDATE_PRIVILEGE");

    AuthorityEnum(final String authority) {
        this.authority = authority;
    }

    private String authority;

    public String getAuthority() {
        return authority;
    }
}
