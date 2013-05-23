package ru.extas.server;

import java.util.Collection;

import ru.extas.model.Insurance;

public interface InsuranceRepository {

	public abstract Collection<Insurance> getAll();

	public abstract void create(Insurance insurance);

	public abstract void deleteById(Long id);

	public abstract void update(Insurance insurance);

}