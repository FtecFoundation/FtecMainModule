package com.ftec.resources.enums;

public enum Statuses {
    Ok(0), InvalidCredentials(1), Invalid2FA(2), LoginTaken(3), EmailTaken(4), AuthenticationFailed(5), ModelMalformed(6);

    private int status;

    Statuses(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
