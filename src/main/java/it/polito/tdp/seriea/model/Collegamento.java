package it.polito.tdp.seriea.model;

public class Collegamento {

	private Team teamA;
	private Team teamB;
	private Integer peso;
	
	public Collegamento(Team teamA, Team teamB, Integer peso) {
		super();
		this.teamA = teamA;
		this.teamB = teamB;
		this.peso = peso;
	}

	public Team getTeamA() {
		return teamA;
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	
	
}
