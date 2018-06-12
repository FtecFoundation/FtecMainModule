package com.ftec.resources.enums;

public enum ConfirmScope {
    RestorePass(0), ConfirmEmail(1);

    ConfirmScope(int scope){
        this.scope = scope;
    }
    private int scope;

    public int getScope(){
        return scope;
    }
}
