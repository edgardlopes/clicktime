package com.clicktime.model;

import java.util.List;

import com.clicktime.model.entity.CategoriaServico;


public class Teste {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			List<CategoriaServico> list = ServiceLocator.getCategoriaServicoService().readByCriteria(null, null);
			list.forEach(c -> System.out.println(c.getNome()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
