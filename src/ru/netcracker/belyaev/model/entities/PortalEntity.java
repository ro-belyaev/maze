package ru.netcracker.belyaev.model.entities;

import java.util.List;

public interface PortalEntity {
	public boolean addPortal(OnePointOnMap firstPoint, OnePointOnMap secondPoint, int step);
	public List<Portal> getPortals();
}
