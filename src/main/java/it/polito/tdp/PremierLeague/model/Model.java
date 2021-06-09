package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Map<Integer,Team> idMap;
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>grafo;
	
	public Model() {
		dao= new PremierLeagueDAO();
		idMap= new HashMap<Integer,Team>();
		dao.listAllTeams(idMap);
		
	}
	
	
	public void creaGrafo() {
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//vertici
		Graphs.addAllVertices(grafo, idMap.values());
		
		//archi
		for(Adiacenza a: dao.getAdiacenza(idMap)) {
			if(this.grafo.containsVertex(a.getT1()) && this.grafo.containsVertex(a.getT2())) {
				if(dao.calcolaPunti(a.getT1()) > dao.calcolaPunti(a.getT2())) {
					//da t1 a t2
					Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2(), Math.abs(a.getPeso()));
				} else if(dao.calcolaPunti(a.getT1()) < dao.calcolaPunti(a.getT2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getT2(), a.getT1(), Math.abs(a.getPeso()));
				}
				}
			}
		}
		

	
	//lista di squadre con punti < passata come parametro
	public List<TeamClassifica> getSquadrePeggiori(Team tendina){
		
		List<TeamClassifica> peggiori = new LinkedList<TeamClassifica>();
		
		for(Team s: Graphs.neighborListOf(grafo, tendina)){
			if(!s.equals(tendina)) {
			if(dao.calcolaPunti(s) < dao.calcolaPunti(tendina)) {
				
				DefaultWeightedEdge arco = this.grafo.getEdge(tendina, s);
				double pesoArco=this.grafo.getEdgeWeight(arco);
				
				TeamClassifica tc= new TeamClassifica(s, pesoArco);
				peggiori.add(tc);
			}	
			}
			
		}
		Collections.sort(peggiori, new Comparator<TeamClassifica>() {

			@Override
			public int compare(TeamClassifica o1, TeamClassifica o2) {
				// TODO Auto-generated method stub
				return o1.getPesoArco().compareTo(o2.getPesoArco());
			}
		
		});
		return peggiori;
		
	}
	
	
public List<TeamClassifica> getSquadreMigliori(Team tendina){
		
		List<TeamClassifica> migliori = new LinkedList<TeamClassifica>();
		
		for(Team s: Graphs.neighborListOf(grafo, tendina)){
			if(!s.equals(tendina)) {
			if(dao.calcolaPunti(s) >  dao.calcolaPunti(tendina)) {
				
				DefaultWeightedEdge arco = this.grafo.getEdge(s, tendina);
				double pesoArco=this.grafo.getEdgeWeight(arco);
				
				TeamClassifica tc= new TeamClassifica(s, pesoArco);
				 migliori.add(tc);
			}	
			}
			
		}
		Collections.sort( migliori, new Comparator<TeamClassifica>() {

			@Override
			public int compare(TeamClassifica o1, TeamClassifica o2) {
				// TODO Auto-generated method stub
				return o1.getPesoArco().compareTo(o2.getPesoArco());
			}
		
		});
		return  migliori;
		
	}
	public int numVertici() {
		if(grafo!=null) {
			return this.grafo.vertexSet().size();
		}
		return 0;
		
	}
	
	public Set<Team> getSquadreTendina(){
		return this.grafo.vertexSet();
	}
	public int numArchi() {
		if(grafo!=null) {
			return this.grafo.edgeSet().size();
		}
		return 0;
		}


	
	
	
}
