package com.dto;

import java.util.Date;


import com.entities.Analista;
import com.entities.Reclamo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccionReclamoDTO {
	private long idAcc_Rec;

	private Date fechaHora;

	private String detalle;

	private Analista analista;

	private Reclamo reclamo;

}
