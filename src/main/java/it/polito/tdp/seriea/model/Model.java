package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private List<Season> stagioni;
	
	//grafo semplice, pesato, orientato
	private Graph<Team, DefaultWeightedEdge> grafo;
	private List<Team> squadre;
	private List<Collegamento> collegamenti;
	
	public Model() {
		this.dao = new SerieADAO();
		this.stagioni = new ArrayList<>();
	}

	public List<Season> elencoStagioni(){
		this.stagioni= this.dao.listSeasons();
		return this.stagioni;
	}
	
	public void creaGrafo(String stagione) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.squadre = new ArrayList<>(this.dao.squadreStagione(stagione));
		System.out.println(this.squadre.size());
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.squadre);
		
		this.collegamenti = new ArrayList<>(this.dao.prendiCollegamenti(stagione, this.squadre));
		System.out.println(this.collegamenti.size());
		
		//VERTICI
		for(Collegamento c: this.collegamenti) {
			Graphs.addEdge(this.grafo, c.getTeamA(), c.getTeamB(), c.getPeso());
		}
		
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String classificaFinale(String stagione) {
		
		List<PuntiSquadra> punteggi = new ArrayList<>();
		List<PuntiSquadra> vitt = new ArrayList<>(this.dao.vittoriaSquadra(stagione));
		List<PuntiSquadra> pare = new ArrayList<>(this.dao.pareggiSquadra(stagione));

		for(PuntiSquadra p: vitt) {
			for(PuntiSquadra s: pare) {
				if(p.getSquadra().equals(s.getSquadra())) {
					punteggi.add(new PuntiSquadra(p.getSquadra(), p.getPunti()+s.getPunti()));
				}
			}
		}
		
		Collections.sort(punteggi);
		
		String stampa = "";
		for(PuntiSquadra ps: punteggi) {
			stampa += ps.getSquadra()+"-->"+ps.getPunti()+"\n";
		}
		return stampa;
	}
	
	public Set<Team> elencoSquadre(){
		return this.grafo.vertexSet();
	}
	//---PUNTO 2---
	private List<Collegamento> soluzione;
	private int max;
	
	public String risultato(Team squadra) {
		
		this.soluzione = new ArrayList<>();
		
		List<Collegamento> parziale = new ArrayList<>();
		
		parziale.add(new Collegamento(squadra, null, -5));
		
		ricorsione(parziale, 0);
		
		String stampa = "";
		for(Collegamento c: soluzione) {
			stampa += c.getTeamA()+"-"+c.getTeamB()+"\n";
		}
		return stampa;
	}
	
	private void ricorsione(List<Collegamento> parziale, int livello) {
		//caso finale: lunghezza massima
		if(livello>max) {
			this.soluzione = new ArrayList<>(parziale);
			max = livello;
		}
		
		Team ultimo = parziale.get(parziale.size()-1).getTeamB();
		//primo caso
		if(ultimo == null) {
			for(Team t: Graphs.neighborListOf(this.grafo, parziale.get(0).getTeamA())) {
				if(this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(0).getTeamA(), t))==1) {
					parziale.add(new Collegamento(parziale.get(0).getTeamA(), t, 0));
					ricorsione(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}//altri casi
		else{
			for(Team t: Graphs.neighborListOf(this.grafo, ultimo)) {
				Collegamento temp = new Collegamento(ultimo, t, 0);
				if(this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, t))==1 && (!parziale.contains(temp))) {
					parziale.add(temp);
					ricorsione(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}
}
