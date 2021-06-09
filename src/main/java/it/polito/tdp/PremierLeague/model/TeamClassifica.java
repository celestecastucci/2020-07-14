package it.polito.tdp.PremierLeague.model;

public class TeamClassifica {
	
	private Team team;
	private Double pesoArco;
	public TeamClassifica(Team team, Double pesoArco) {
		super();
		this.team = team;
		this.pesoArco = pesoArco;
	}
	@Override
	public String toString() {
		return team.getName() + "||" +pesoArco;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Double getPesoArco() {
		return pesoArco;
	}
	public void setPesoArco(Double pesoArco) {
		this.pesoArco = pesoArco;
	}
	
	
	

}
