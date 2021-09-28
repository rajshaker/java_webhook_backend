package com.attra.gcldemo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.attra.gcldemo.GoCardlessFactory;

// NOTE: GCL is short for GoCardLess. Just a naming convention to save a few keystrokes :-)
public class GCLConfiguration extends Configuration {

    @JsonProperty
    @NotNull
    @Valid
    private GoCardlessFactory goCardless;

    public GoCardlessFactory getGoCardless() {
        return goCardless;
    }
}
