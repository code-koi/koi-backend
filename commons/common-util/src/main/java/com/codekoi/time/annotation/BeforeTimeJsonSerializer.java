package com.codekoi.time.annotation;


import com.codekoi.time.formatter.BeforeTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = BeforeTimeSerializer.class)
public @interface BeforeTimeJsonSerializer {
}
