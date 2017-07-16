package com.clicktime.model;

import java.util.List;

import com.clicktime.model.entity.CategoriaServico;
import java.time.Month;
import org.joda.time.Months;


public class Teste {

	public static void main(String[] args) {
            final Month[] values = Month.values();
            
            for (Month value : values) {
                System.out.println(value.name());
                System.out.println(value.getValue());
            }
            
	}

}
