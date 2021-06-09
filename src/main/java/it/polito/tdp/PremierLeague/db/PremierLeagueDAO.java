package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllTeams(Map<Integer,Team>idMap){
		String sql = "SELECT * FROM Teams";
		//List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("TeamID"))){

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				idMap.put(res.getInt("TeamID"), team);
				}
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// seleziono due squadre e mi prendo il risultato di ognuna quando è in casa
	public List<Adiacenza> getAdiacenza(Map<Integer,Team>idMap){
		String sql="SELECT DISTINCT m1.TeamHomeID as t1 , m1.TeamAwayID as t2 "
				+ "FROM matches m1 ";
		
		List<Adiacenza> result = new LinkedList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team t1= idMap.get(res.getInt("t1"));
				Team t2= idMap.get(res.getInt("t2"));
				if(t1!=null && t2!=null) {
				double pesoArco= this.calcolaPesoArco(t1, t2);
				if(pesoArco!=0) {
				Adiacenza a= new Adiacenza(t1, t2, pesoArco);
				result.add(a);
				}
			}
			 else {
				System.out.println("errore in getAdiacenze");
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}



		public double calcolaPunti (Team squadra) {
			
		double punti=0.0;
			String sql="SELECT distinct m1.TeamHomeID as t1, m1.TeamAwayID as t2, m1.ResultOfTeamHome AS risHome "
					+ "FROM matches m1 ";
		
			Connection conn = DBConnect.getConnection();
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();
				while (res.next()) {
		     	double risHome = res.getDouble("risHome");
		     	if(squadra.getTeamID()==(res.getInt("t1"))) {
			     	if(risHome==1) {
			     		punti+=3;
			     		
			     	} else if(risHome==0) {
			     		punti+=1;
			     	
			     	} else {
			     		punti+=0;
			     		
			     	}
			     	squadra.setPuntiTeam(punti);
			     }
		     	
		     	else if(squadra.getTeamID()==(res.getInt("t2"))) {  // caso opposto quindi -1 significa che la mia squadra ha vinto
		     		if(risHome==-1) {
			     		punti+=3;
			     		
			     	} else if(risHome==0) {
			     		punti+=1;
			     	
			     	} else {
			     		punti+=0;
			     		
			     	}
			     	squadra.setPuntiTeam(punti);
		     		
		     	}
				}
				
				
				conn.close();
				return punti;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
		
		}
		
		
		//metodo che date due squadre calcoli la differenza 
		public double calcolaPesoArco(Team squadra1, Team squadra2) {
			
			Double pesoArco=0.0;
			Double pesoS1= this.calcolaPunti(squadra1);
			Double pesoS2= this.calcolaPunti(squadra2);
			pesoArco= pesoS1-pesoS2;
			
			return pesoArco;
			}
		
		}