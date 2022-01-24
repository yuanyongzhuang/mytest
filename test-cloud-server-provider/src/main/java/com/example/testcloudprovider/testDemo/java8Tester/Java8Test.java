package com.example.testcloudprovider.testDemo.java8Tester;

import cn.hutool.poi.excel.cell.CellEditor;

import java.util.function.IntBinaryOperator;

/**
 * java8 新特性--lambda表达式
 * @author 86185
 */
public class Java8Test {

    public static void main(String[] args) {
        //类型声明
        MathOperation addition = (int a, int b) -> a + b;
        //不用声明类型
        MathOperation subtraction = (a, b) -> a - b;
        //大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> { return a * b;};
        //没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;

    }
}
