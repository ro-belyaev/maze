package ru.netcracker.belyaev.model.entities;

import java.util.List;

public interface SimpleEntity {
	public List<OnePointOnMap> getEntity();
	public void setEntity(List<OnePointOnMap> points);
}
