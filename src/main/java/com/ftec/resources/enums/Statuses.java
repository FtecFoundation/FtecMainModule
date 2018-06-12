package com.ftec.resources.enums;

public enum Statuses {
    Ok(0), InvalidCredentials(1), Invalid2FA(2), LoginTaken(3), EmailTaken(4), AuthenticationFailed(5), ModelMalformed(6),
    UnexpectedError(7), TokenNotCreated(8), WeakPassword(9), InvalidHash(10), InvalidUserData(11);

    private int status;

    Statuses(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
