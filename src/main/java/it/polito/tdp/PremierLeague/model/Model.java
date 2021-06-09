package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
				
			}
		}
		
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
