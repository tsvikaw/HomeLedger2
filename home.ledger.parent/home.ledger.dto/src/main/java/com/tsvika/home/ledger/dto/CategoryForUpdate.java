package com.tsvika.home.ledger.dto;

import javax.validation.constraints.NotNull;

public class CategoryForUpdate extends CategoryForCreation {

    @NotNull
    private Boolean isEnabled;

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
