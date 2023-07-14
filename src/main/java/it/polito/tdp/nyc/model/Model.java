package it.polito.tdp.nyc.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	NYCDao dao;
	SimpleWeightedGraph<Localita, DefaultWeightedEdge> graph;
	
	public Model() {
		dao = new NYCDao();
	}
	
	public List<String> getAllProvider(){
		return dao.getAllProvider();
	}
	
	public SimpleWeightedGraph<Localita, DefaultWeightedEdge> creaGrafo(String provider, double x){
		
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<Localita> vertex = new ArrayList<>(dao.getAllLocation(provider));
		
		Graphs.addAllVertices(graph, vertex);
		
		for (Localita l1 : graph.vertexSet())
			for (Localita l2 : graph.vertexSet())
				if (!l1.equals(l2) && Math.abs(LatLngTool.distance(l1.getCoordinate(), l2.getCoordinate(), LengthUnit.KILOMETER)) < x)
					Graphs.addEdge(graph, l1, l2, Math.abs(LatLngTool.distance(l1.getCoordinate(), l2.getCoordinate(), LengthUnit.KILOMETER)));
		
		return graph;
	}
	
	private int maxVicini;
	
	public NYCDao getDao() {
		return dao;
	}

	public void setDao(NYCDao dao) {
		this.dao = dao;
	}

	public SimpleWeightedGraph<Localita, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public void setGraph(SimpleWeightedGraph<Localita, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public void setMaxVicini(int maxVicini) {
		this.maxVicini = maxVicini;
	}
	
	public int getMaxVicini() {
		return this.maxVicini;
	}

	public List<Localita> getVicini(){
		
		maxVicini = 0;
		List<Localita> result = new ArrayList<>();
		
		for (Localita l : graph.vertexSet())
			if (Graphs.neighborListOf(graph, l).size() > maxVicini)
				maxVicini = Graphs.neighborListOf(graph, l).size();
		for (Localita l : graph.vertexSet())
			if (Graphs.neighborListOf(graph, l).size() == maxVicini)
				result.add(l);
		
		return result;
	}
	
	private List<Localita> soluzione;
	
	public List<Localita> getCammino(Localita arrivo, String s){
		
		soluzione = new ArrayList<>();
		List<Localita> parziale = new ArrayList<>();
		
		List<Localita> vertici = new ArrayList<>(graph.vertexSet());
		
		for (Localita l : graph.vertexSet())
			if (l.getLocation().contains(s))
				vertici.remove(l);
		
		Localita partenza = vertici.get((int) (Math.random()*(vertici.size()-1)));
		
		parziale.add(partenza);
		vertici.remove(partenza);
		
		ricorsiva(partenza, arrivo, parziale, vertici);
		
		return soluzione;
		
	}
	
	private void ricorsiva(Localita corrente, Localita arrivo, List<Localita> parziale, List<Localita> rimanenti) {
		
		if ((parziale.size() > soluzione.size()) && (corrente.equals(arrivo)))
			soluzione = new ArrayList<>(parziale);
		
		Set<Localita> successori = Graphs.neighborSetOf(graph, corrente);
		successori.retainAll(rimanenti);
		
		for (Localita l : successori) {
			if (!parziale.contains(l)) {
				parziale.add(l);
				ricorsiva(l,arrivo,parziale,rimanenti);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
