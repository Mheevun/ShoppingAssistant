package com.mheev.helpthemshop.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by mheev on 9/22/2016.
 */

@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface NetNavigatorItemScope {
}
