package com.bailiban.day1.myMVC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MethodInfo {
    private Method method;
    private String className;
}
