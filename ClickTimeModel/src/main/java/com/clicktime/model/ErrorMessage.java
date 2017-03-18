/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clicktime.model;

/**
 * 
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 */
public class ErrorMessage {
    public static final String NOT_NULL = "Este campo é obrigatório!";
    public static final String NOME_USUARIO_UNIQUE = "Este nome de usuario já esta sendo utilizado!";
    public static final String TELEFONE_UNIQUE = "Este telefone já está sendo utilizado!";
    public static final String EMAIL_UNIQUE = "Este e-mail já está sendo utilizado!";
    public static final String SENHA_NE = "As senhas não conferem!";
    public static final int MIN_LENGTH_SENHA = 3;
    public static final String SENHA_CURTA = "A senha deve ter pelo menos 3 caracteres!";
    public static final String HORA_INVALIDA ="Hora está em formato invalido!";
    public static final String HORA_INICIO_MAIOR_HORA_FIM = "A hora inicial deve ser menor que a hora final!";
    public static final String HORA_FIM_MENOR_HORA_INICIO = "A hora final deve ser maior que a hora inicial!";
    public static final String TELEFONE_INVALIDO = "Telefone inválido!";
    public static final String HORA_INCORRETA = "A hora deve terminar com 30 ou 0 minutos";
    public static final String DIFERENCA_MINIMA = "A diferenca entre o inicio e fim deve ser de pelo menos uma hora";
    public static final String DEVE_SER_NUMERO = "Este campo deve conter um número!";
    public static final String DURACAO_MAIOR_DIA_ATENDIMENTO = "A duração de um serviço deve ser menor que o tamanho do expediente!";
    public static final String DURACAO_INCORRETA = "Os minutos da duração devem ser multiplos de 15 minutos!";
    public static final String NOT_ZERO = "A duração deve ser maior que zero!";
    public static final String NOME_CATEGORIA_UNIQUE = "Esta categoria já existe!";
    
}
