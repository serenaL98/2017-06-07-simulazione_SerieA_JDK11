package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Collegamento;
import it.polito.tdp.seriea.model.PuntiSquadra;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRORE DB: controlla la sintassi!");
			return null ;
		}
	}
	
	public List<Team> squadreStagione(String stagione) {
		String sql = "SELECT DISTINCT m.HomeTeam casa " + 
				"FROM seasons s, matches m " + 
				"where s.description= ? " + 
				"		AND s.season = m.Season " ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, stagione);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("casa"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRORE DB: controlla la sintassi!");
			return null ;
		}
	}

	public List<Collegamento> prendiCollegamenti(String stagione, List<Team> squadre) {
		String sql = "SELECT m.HomeTeam casa, m.AwayTeam fuori, m.FTR esito " + 
				"FROM seasons s, matches m " + 
				"where s.description= ? " + 
				"		AND s.season = m.Season " ;
		
		List<Collegamento> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, stagione);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Team a = null;
				Team b= null;
				
				for(Team t: squadre) {
					if(t.getTeam().equals(res.getString("casa"))) {
						a = t;
					}
					if(t.getTeam().equals(res.getString("fuori"))) {
						b = t;
					}
				}
				
				Integer peso = 0;
				
				if(res.getString("esito").equals("H")) {
					peso = 1;
				}else if(res.getString("esito").equals("A")) {
					peso = -1;
				}
				
				Collegamento temp = new Collegamento(a, b, peso);
				
				result.add(temp);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRORE DB: controlla la sintassi!");
			return null ;
		}
	}
	
	public List<PuntiSquadra> vittoriaSquadra(String stagione) {
		String sql = "SELECT m.HomeTeam casa, COUNT(DISTINCT m.AwayTeam)*3 vitt " + 
				"FROM seasons s, matches m " + 
				"where s.description= ? " + 
				"		AND s.season = m.Season " + 
				"		AND m.FTR = 'H' " + 
				"GROUP BY m.HomeTeam " ;
		
		List<PuntiSquadra> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, stagione);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String a = res.getString("casa");
				int p = res.getInt("vitt");
				
				PuntiSquadra ps = new PuntiSquadra(a, p);
				
				result.add(ps);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRORE DB: controlla la sintassi!");
			return null ;
		}
	}
	
	public List<PuntiSquadra> pareggiSquadra(String stagione) {
		String sql = "SELECT m.HomeTeam casa, COUNT(DISTINCT m.AwayTeam) par " + 
				"FROM seasons s, matches m " + 
				"where s.description= ? " + 
				"		AND s.season = m.Season " + 
				"		AND m.FTR = 'D' " + 
				"GROUP BY m.HomeTeam " ;
		
		List<PuntiSquadra> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, stagione);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String a = res.getString("casa");
				int p = res.getInt("par");
				
				PuntiSquadra ps = new PuntiSquadra(a, p);
				
				result.add(ps);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERRORE DB: controlla la sintassi!");
			return null ;
		}
	}
}
