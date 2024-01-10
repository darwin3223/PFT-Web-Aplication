package com.dto;

import java.util.Date;

import com.entities.Analista;
import com.entities.Constancia;

import lombok.Data;

@Data
public class AccionConstanciaDTO {
	private long idAcc_Con;

	private Date fechaHora;

	private String detalle;

	private Analista analista;

	private Constancia constancia;
}
