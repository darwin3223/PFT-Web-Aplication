package com.repositories;

import com.entities.Itr;
import com.exceptions.ServiciosException;

public interface ItrRepository extends Repository<Itr>{

	Itr findByName(String name) throws ServiciosException;
}
