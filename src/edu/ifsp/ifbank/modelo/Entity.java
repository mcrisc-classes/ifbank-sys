package edu.ifsp.ifbank.modelo;

public abstract class Entity {
	private int id = -1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNew() {
		return id == -1;
	}
}
