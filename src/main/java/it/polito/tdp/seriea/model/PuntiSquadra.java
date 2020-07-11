package it.polito.tdp.seriea.model;

public class PuntiSquadra implements Comparable<PuntiSquadra> {
	
	private String squadra;
	private Integer punti;
	
	public PuntiSquadra(String squadra, Integer punti) {
		super();
		this.squadra = squadra;
		this.punti = punti;
	}
	
	public String getSquadra() {
		return squadra;
	}
	public void setSquadra(String squadra) {
		this.squadra = squadra;
	}
	public Integer getPunti() {
		return punti;
	}
	public void setPunti(Integer punti) {
		this.punti = punti;
	}

	@Override
	public int compareTo(PuntiSquadra o) {
		return -this.punti.compareTo(o.getPunti());
	}
	
	

}
