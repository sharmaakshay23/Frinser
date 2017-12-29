package com.projectY.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.frinser.dto.TimeSpent;
import com.frinser.dto.UserIds;
import com.frinserService.wowData;
import com.frinserService.rest.JSONService;
import com.projectY.settings.Settings;

public class DataDb extends Settings{
	 Statement stmt = null;
	 Statement stmt2 = null;

 
		public void callDb() throws SQLException
		{
			stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String sql;
			sql = "SELECT * FROM testtable";
			ResultSet rs = stmt.executeQuery(sql);
			callDbU();
		}
		public void callDbU() throws SQLException
		{
			stmt = Settings.getUpdateConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String sql;
			sql = "SELECT * FROM testtable";
			ResultSet rs = stmt.executeQuery(sql);

		}
		
	public void insertDummyData(ArrayList<String> links,String user,ArrayList<String> friends) throws SQLException
	{
		stmt = Settings.getConn().createStatement();
		Random randomGenerator = new Random();
		for(int i=0;i<links.size();i++)
	      {String sql = "INSERT INTO linksTable " +
	                   "VALUES ('"+links.get(i)+"', '"+friends.get(randomGenerator.nextInt(5))+"')";
	      JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),sql);
	      stmt.executeUpdate(sql);
	      }
	}
	
	public int Vote(String link,String user,int voteType,String query, int tty) 
	{
		try{
		if(ifVoted(link,user))
		{
			String insertTableSQL = "update   linksTable set vote=1, modified_date='"+new Date(new java.util.Date().getTime())+"',query=?,tty=? where links=? and userid=?";
			PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, query);
			preparedStatement.setString(2, link);
			preparedStatement.setString(3, user);
			preparedStatement.setInt(4, tty);
			preparedStatement .executeUpdate();
			return 1;
		}
		JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"Link "+link+" User "+user+" Vote "+ voteType+" Query "+query);
		String insertTableSQL = "INSERT INTO linksTable (links,userid,modified_date,vote,query,tty) VALUES"
				+ "(?,?,?,?,?,?)";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
		preparedStatement.setString(1, link);
		preparedStatement.setString(2, user);
		preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
		preparedStatement.setInt(4, voteType);
		preparedStatement.setString(5, query);
		preparedStatement.setInt(6, tty);
		preparedStatement .executeUpdate();
		return 1;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	public int downVote(String link,String user,int voteType,String query) 
	{
		try{
		if(ifVoted(link,user))
		{
			
			String insertTableSQL = "update   linksTable set vote=0, modified_date='"+new Date(new java.util.Date().getTime())+"',query=? where links=? and userid=?";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, query);
			preparedStatement.setString(2, link);
			preparedStatement.setString(3, user);
			preparedStatement .executeUpdate();
			return 1;
		}
		String insertTableSQL = "INSERT INTO linksTable (links,userid,modified_date,vote,query) VALUES"
				+ "(?,?,?,?,?)";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
		preparedStatement.setString(1, link);
		preparedStatement.setString(2, user);
		preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
		preparedStatement.setInt(4, voteType);
		preparedStatement.setString(5, query);
		preparedStatement .executeUpdate();
		return 1;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	public boolean ifVoted(String link, String user) throws SQLException {

		String selectSQL = "SELECT *FROM linksTable WHERE userid = ? and links=?";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(selectSQL);
		preparedStatement.setString(1, user);
		preparedStatement.setString(2, link);
		
		ResultSet rs = preparedStatement.executeQuery(  );
		if(rs.next())
		{


				return true;
			
			
		}
		return false;
	}
	public boolean checkIfUpVoted(String link, String user) throws SQLException {

		String selectSQL = "SELECT *FROM linksTable WHERE userid = ? and links=?";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(selectSQL);
		preparedStatement.setString(1, user);
		preparedStatement.setString(2, link);
		
		ResultSet rs = preparedStatement.executeQuery(  );
		if(rs.next())
		{
			if(rs.getInt(4)==1)
			return true;
			Date mDate=rs.getDate(3);
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),mDate.getTime());
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),new java.util.Date().getTime());
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),mDate.getTime()-new java.util.Date().getTime());
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),(mDate.getTime()-new java.util.Date().getTime())/1000/60/60/24);
			int mon=(int) ((mDate.getTime()-new java.util.Date().getTime())/1000/60/60/24);
			mon =-mon;
			if(mon<30&&rs.getInt(4)==0)
				return true;
			
			
		}
		return false;
	}
	public JSONObject readLinksProfile(ArrayList<String> links ,ArrayList<String> friends, String site) throws SQLException, JSONException
	{
		/*
		{
		    "data": [
		        {
		            "autoportal.com/newcars/": [
		                {
		                    "facebook": [
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "882810008404972@fb.com"
		                        },
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "sumit@fb.com"
		                        }
		                    ]
		                },
		                {
		                    "gmail": [
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "882810008404972@fb.com"
		                        },
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "sumit@fb.com"
		                        }
		                    ]
		                },
		                {
		                    "twitter": [
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "882810008404972@fb.com"
		                        },
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "sumit@fb.com"
		                        }
		                    ]
		                }
		            ]
		        },
		        {
		            "zigwheels.com/newcars": [
		                {
		                    "facebook": [
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "882810008404972@fb.com"
		                        },
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "sumit@fb.com"
		                        }
		                    ]
		                },
		                {
		                    "gmail": [
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "882810008404972@fb.com"
		                        },
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "sumit@fb.com"
		                        }
		                    ]
		                },
		                {
		                    "twitter": [
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "882810008404972@fb.com"
		                        },
		                        {
		                            "links": "autoportal.com/newcars/",
		                            "userid": "sumit@fb.com"
		                        }
		                    ]
		                }
		            ]
		        }
		    ]
		}
		*/
		if (links.size() == 0 || friends.size() == 0) {
			JSONObject returnObj = new JSONObject();
			JSONArray finalObj = new JSONArray();
			returnObj.put("data", finalObj);
			return returnObj;
		} else {			
			String sql = "select * from linksTable where links in (";
			String timeSpentSql = "select * from timespent where link in (";
			String linksString = "";
			String usersString  = "";
			
			for (int i = 0; i < links.size(); i++) {
				if (i == 0) {
					linksString = linksString + "'" + links.get(i) + "'";
					continue;
				}
				linksString = linksString + "," + "'" + links.get(i) + "'";
	
			}
			sql = sql + linksString + ") and userid in (";
			timeSpentSql = timeSpentSql + linksString + ") and userid in (";
			for (int i = 0; i < friends.size(); i++) {
				if (i == 0) {
					usersString = usersString + "'" + friends.get(i) + "'";
					continue;
				}
				usersString = usersString + "," + "'" + friends.get(i) + "'";
	
			}
			sql = sql + usersString + ")  ";
			timeSpentSql = timeSpentSql + usersString + ") and vote = '1'";
			
			stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			stmt2 = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"SQL is " + sql);
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"SQL is " + timeSpentSql);

			
			ResultSet rs = stmt.executeQuery(sql);
			ResultSet rs1 = stmt2.executeQuery(timeSpentSql);
			
			HashMap<String, JSONObject> outerHash = new HashMap<String, JSONObject>();
			JSONObject obj = new JSONObject();
			JSONArray childArr = new JSONArray();
			
			for (int i = 0; i < links.size(); i++) {
				JSONObject childArrObj = new JSONObject();
				childArr = new JSONArray();
				childArrObj.put("facebook", childArr);
				// parentArr.put(childArrObj);

				childArr = new JSONArray();
				childArrObj.put("gmail", childArr);
				// parentArr.put(childArrObj);

				childArr = new JSONArray();
				childArrObj.put("twitter", childArr);
				// parentArr.put(childArrObj);

				childArr = new JSONArray();
				childArrObj.put("selfLike", childArr);
				// parentArr.put(childArrObj);

				outerHash.put(links.get(i), childArrObj);	
			}

	
			while (rs.next()) {
				JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"rs.getString(1): " + rs.getString(1));
				JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"trythis.containsKey(rs.getString(1)) : "
						+ outerHash.containsKey(rs.getString(1)));
				if (outerHash.containsKey(rs.getString(1))) {
					// If link exists, get type of user id (fb/gmail/twitter)
					// search if corresponding JSONArray exists
					// insert into JSONArray
				} else {
					// Create a key
					// do the rest
					// parentArr = new JSONArray();
					JSONObject childArrObj = new JSONObject();
					childArr = new JSONArray();
					childArrObj.put("facebook", childArr);
					// parentArr.put(childArrObj);
	
					childArr = new JSONArray();
					childArrObj.put("gmail", childArr);
					// parentArr.put(childArrObj);
	
					childArr = new JSONArray();
					childArrObj.put("twitter", childArr);
					// parentArr.put(childArrObj);

					childArr = new JSONArray();
					childArrObj.put("selfLike", childArr);
					// parentArr.put(childArrObj);

					outerHash.put(rs.getString(1), childArrObj);
	
				}
	
				JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"trythis building :::: \n" + outerHash + "\n");
	
				// Do common routine now
				// JSONArray tempParentArr = new JSONArray();
				JSONObject tempParentObj = new JSONObject();
				JSONArray tempChildArr = new JSONArray();
				
				if (site.equals("selfLike")) {
					tempParentObj = outerHash.get(rs.getString(1));
					tempChildArr = (JSONArray) tempParentObj.get("selfLike");
					obj = new JSONObject();
					obj.put("links", rs.getString(1));
					obj.put("userid", rs.getString(2));
					//obj.put("frinser_id", rs.getString(3));
					tempChildArr.put(obj);
				} else {	
					// get userid type
					String useridRegex = new String();
					useridRegex = rs.getString(2);
		
					String regexSite = new String();
					regexSite = useridRegex.substring(useridRegex.lastIndexOf("@") + 1);
		
					if (regexSite.contains("fb")) {
						tempParentObj = outerHash.get(rs.getString(1));
						tempChildArr = (JSONArray) tempParentObj.get("facebook"); // ;tempChildArrObj.getJSONArray("facebook");
						obj = new JSONObject();
						obj.put("links", rs.getString(1));
						obj.put("userid", rs.getString(2));
						//obj.put("frinser_id", rs.getString(3));
						tempChildArr.put(obj);
					} else if (regexSite.contains("gmail")) {
						tempParentObj = outerHash.get(rs.getString(1));
						tempChildArr = (JSONArray) tempParentObj.get("gmail");
						obj = new JSONObject();
						obj.put("links", rs.getString(1));
						obj.put("userid", rs.getString(2));
						//obj.put("frinser_id", rs.getString(3));
						tempChildArr.put(obj);
					} else {
						tempParentObj = outerHash.get(rs.getString(1));
						tempChildArr = (JSONArray) tempParentObj.get("twitter");
						obj = new JSONObject();
						obj.put("links", rs.getString(1));
						obj.put("userid", rs.getString(2));
						//obj.put("frinser_id", rs.getString(3));
						tempChildArr.put(obj);
					}
				}
			}
			
			/* Browse timeSpent rows now */
			while (rs1.next()) {
				//JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"rs.getString(1): " + rs.getString(1));
				//JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"trythis.containsKey(rs.getString(1)) : "
				//		+ outerHash.containsKey(rs.getString(1)));
				if (outerHash.containsKey(rs1.getString(2))) {
					// If link exists, get type of user id (fb/gmail/twitter)
					// search if corresponding JSONArray exists
					// insert into JSONArray
				} else {
					// Create a key
					// do the rest
					// parentArr = new JSONArray();
					JSONObject childArrObj = new JSONObject();
					childArr = new JSONArray();
					childArrObj.put("facebook", childArr);
					// parentArr.put(childArrObj);
	
					childArr = new JSONArray();
					childArrObj.put("gmail", childArr);
					// parentArr.put(childArrObj);
	
					childArr = new JSONArray();
					childArrObj.put("twitter", childArr);
					// parentArr.put(childArrObj);

					childArr = new JSONArray();
					childArrObj.put("selfLike", childArr);
					// parentArr.put(childArrObj);

					outerHash.put(rs1.getString(2), childArrObj);
	
				}
	
				//JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"trythis building :::: \n" + outerHash + "\n");
	
				// Do common routine now
				// JSONArray tempParentArr = new JSONArray();
				JSONObject tempParentObj = new JSONObject();
				JSONArray tempChildArr = new JSONArray();
				
				if (site.equals("selfLike")) {
					tempParentObj = outerHash.get(rs1.getString(2));
					tempChildArr = (JSONArray) tempParentObj.get("selfLike");
					obj = new JSONObject();
					obj.put("links", rs1.getString(2));
					obj.put("userid", rs1.getString(3));
					//obj.put("frinser_id", rs.getString(3));
					
					/* Ignore timespent rows for selfLike */
					//tempChildArr.put(obj);
				} else {	
					// get userid type
					String useridRegex = new String();
					useridRegex = rs1.getString(3);
		
					String regexSite = new String();
					regexSite = useridRegex.substring(useridRegex.lastIndexOf("@") + 1);
		
					if (regexSite.contains("fb")) {
						tempParentObj = outerHash.get(rs1.getString(2));
						tempChildArr = (JSONArray) tempParentObj.get("facebook"); // ;tempChildArrObj.getJSONArray("facebook");
						obj = new JSONObject();
						obj.put("links", rs1.getString(2));
						obj.put("userid", rs1.getString(3));
						//obj.put("frinser_id", rs.getString(3));
						tempChildArr.put(obj);
					} else if (regexSite.contains("gmail")) {
						tempParentObj = outerHash.get(rs1.getString(2));
						tempChildArr = (JSONArray) tempParentObj.get("gmail");
						obj = new JSONObject();
						obj.put("links", rs1.getString(2));
						obj.put("userid", rs1.getString(3));
						//obj.put("frinser_id", rs.getString(3));
						tempChildArr.put(obj);
					} else {
						tempParentObj = outerHash.get(rs1.getString(2));
						tempChildArr = (JSONArray) tempParentObj.get("twitter");
						obj = new JSONObject();
						obj.put("links", rs1.getString(2));
						obj.put("userid", rs1.getString(3));
						//obj.put("frinser_id", rs.getString(3));
						tempChildArr.put(obj);
					}
				}
			}

			// Set keyset=outerHash.keySet();
			JSONArray finalObj = new JSONArray();
	
			for (Map.Entry<String, JSONObject> childEntry : outerHash.entrySet()) {
				JSONObject tempParentObject = new JSONObject();
				// JSONArray tempChildEntrySubArr =
				// tempChildEntryVal.getJSONArray("facebook");
				// Map<String, Object> tempChildEntrySubHash = new HashMap<String,
				// Object>();
				// tempChildEntryVal = outerHash.get(rs.getString(1));
				// JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"tempUniqueCombo :::: " +
				// tempChildEntryVal.get("combo"));
				// tempChildEntrySubHash = jsonToMap((JSONObject)
				// tempChildEntryVal.get("combo"));
	
				tempParentObject.put(childEntry.getKey(), childEntry.getValue());
				finalObj.put(tempParentObject);
	
			}
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"trythis :::::: \n\n" + outerHash + "\n\n");
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"finalObj :::::: \n\n" + finalObj + "\n\n");
	
			JSONObject returnObj = new JSONObject();
	
			returnObj.put("data", finalObj);
	
			return returnObj;
		}
	}
	public static void main(String q[])  {
//		ArrayList<String> links =new ArrayList<String>();
//		links.add("http://www.cs.utexas.edu/users/djimenez/utsa/cs1723/lecture2.html");
//		links.add("http://en.wikipedia.org/wiki/Heap_(data_structure)");
//		links.add("http://www.studytonight.com/data-structures/time-complexity-of-algorithms");
//		links.add("http://www.cs.princeton.edu/courses/archive/spring06/cos423/Handouts/Amortized.pdf");
//		links.add("http://www.leda-tutorial.org/en/official/ch02s02s03.html");
//		links.add("http://dl.acm.org/citation.cfm?id=73040");
//		
//		ArrayList<String> friends = new ArrayList<String>();
//		friends.add("ishu");
//		friends.add("akshay");
//		friends.add("himank");
//		friends.add("vrinda");
//		friends.add("arushi");
//		
//		
//		new DataDb().readLinksProfile(links, friends);
//		new DataDb().readLinksProfile(links, friends);

	}
	public  List<String> upVotingFriends(String link, ArrayList<String> friends) throws SQLException {

		String sql="select * from linksTable where links in (";
		sql =sql +"'"+link+"'";
		sql= sql + ") and userid in (";
		for(int i=0;i<friends.size();i++)
		{
			if(i==0)
				{
				sql =sql +"'"+friends.get(i)+"'";
				continue;
				}
			sql = sql +","+"'"+friends.get(i)+"'";
			
		}
		sql= sql + ")  ";
		stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(sql);
		JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"SQL is "+ sql);
		List<String> res= new ArrayList<String>();
		while(rs.next())
		{
			res.add(rs.getString(2));
		}
		return res;
	}
	public String isVoted(String string, String string2) {
		
		return null;
	}
	public int storeTimTaken(String link, String twitter, String fb, String gapi, int total) {
		

		try{
		if(ifVoted(link,fb))
		{
			String insertTableSQL = "update   linksTable set  modified_date='"+new Date(new java.util.Date().getTime())+"',  totalTime=? where links=? and (userid=? or userid=? or userid=?  )";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setInt(1, total);
			preparedStatement.setString(2, link);
			preparedStatement.setString(3, fb);
			preparedStatement.setString(4, twitter);
			preparedStatement.setString(5, gapi);
			
			preparedStatement .executeUpdate();
			return 1;
		}
		JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"Link "+link+" User "+fb+" Vote " );
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	
		return 0;
	}
	
	public void healthcheck() throws SQLException
	{
		String sql="SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='frinser'";
		
		stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			//JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),rs.getString("TABLE_NAME"));
		}
		return;
	}
	
	public List<wowData> getWowData(ArrayList<String> links ,ArrayList<String> friends) throws SQLException
	{
		String sql="select * from linksTable where links in (";
		for(int i=0;i<links.size();i++)
		{
			if(i==0)
				{
				sql =sql +"'"+links.get(i)+"'";
				continue;
				}
			sql = sql +","+"'"+links.get(i)+"'";
			
		}
		if(friends.size() > 0) {
			sql= sql + ") and userid in (";
			for(int i=0;i<friends.size();i++)
			{
				if(i==0)
					{
					sql =sql +"'"+friends.get(i)+"'";
					continue;
					}
				sql = sql +","+"'"+friends.get(i)+"'";
				
			}
		} 
		sql= sql + ")" ;
		
		stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(sql);

		
	    /*ResultSetMetaData rsmd = rs.getMetaData();
	    JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"AKSHAY6 querying SELECT * FROM XXX");
	    int columnsNumber = rsmd.getColumnCount();*/
		
		List<wowData> wd = new ArrayList<wowData>();
		
		while (rs.next()) {
			wowData wo = new wowData();
			wo.add(rs.getString("links"),rs.getInt("vote"), rs.getString("query"), rs.getInt("tty"));
			wd.add(wo);
	    	/*JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),rs.getInt(4));;
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),rs.getString(5));
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),rs.getString(7));*/
	        /*for (int i = 1; i <= columnsNumber; i++) {
	            if (i > 1) System.out.print(",  ");
	            String columnValue = rs.getString(i);
	            System.out.print(columnValue + " " + rsmd.getColumnName(i));
	        }
	        JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"");
	        */
	    }
		
		for(wowData obj: wd) {
			JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"AKSHAY6 " + obj);
		}

		return wd;
		
	}
	public int storeQuery(String link, String user) {
		try{
			String selectSQL = "SELECT *FROM querytables WHERE user = ? and query=? and date > DATE_SUB(now(), INTERVAL 5 MINUTE)";
			PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
			p1.setString(1, user);
			p1.setString(2, link);
			
			ResultSet rs = p1.executeQuery();
			if(rs.next())
			{
				return -1;
			}
		String insertTableSQL = "INSERT INTO querytables (query,user,date) VALUES"
				+ "(?,?,NOW())";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
		preparedStatement.setString(1, link);
		preparedStatement.setString(2, user);
		JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),new Date(System.currentTimeMillis()));
//		preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
		preparedStatement .executeUpdate();
		return 1;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public HashMap<String,Integer> getNonFriendLinkData(ArrayList<String> links) throws SQLException
	{
		String sql="select * from link_upvotes where link in (";
		String timeSpentSql="SELECT link,count(*) as upvotes FROM `timespent_totalvotes` where link in  (";
		String linksString = "";

		if (links.size() == 0) {
			sql = sql + "''";
			timeSpentSql = timeSpentSql + "''";
		}
		for (int i=0;i<links.size();i++)
		{
			if (i==0) {
				linksString = linksString + "'" + links.get(i) + "'";
				continue;
			}
			linksString = linksString + "," + "'" + links.get(i) + "'";			
		}
		sql = sql + linksString + ")";
		timeSpentSql = timeSpentSql + linksString + ") and userid not in ('undefined@fb.com') group by link";
		
		stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		stmt2 = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = stmt.executeQuery(sql);
		ResultSet rs1 = stmt2.executeQuery(timeSpentSql);
		
		JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"SQL is "+ sql);
		JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),"SQL is "+ timeSpentSql);

		HashMap<String,Integer> res= new HashMap<String,Integer>();
		
		for (int i=0; i < links.size(); i++) {
			res.put(links.get(i),0);
		}
		
		while(rs.next()) {
			res.put(rs.getString("link"),rs.getInt("upvotes"));
		}
		while(rs1.next()) {
			int upvotes = res.get(rs1.getString("link")).intValue();
			res.put(rs1.getString("link"), upvotes + rs1.getInt("upvotes"));
		}
		
		return res;
		
	}
	public String getId(String user) throws SQLException {
		return user;
}
	private String checkType(String user) {

		if(user.contains("fb"))
			return "fb";
		else 
			if(user.contains("gmail"))
			return "gm";
		else  
			return "tw";
	
	}

	public String getUserId(String user,String type) throws SQLException {
		String selectSQL = "SELECT *FROM userTable WHERE "+type+"_user_name = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, user);
		
		ResultSet rs = p1.executeQuery();
		if(rs.next())
		{
			return rs.getString("id");
		}
		else
		{
			return null;
		}
	}
	public boolean checkAlreadyStoredId(String userId,String type,String rowId ) throws SQLException {
		String selectSQL = "SELECT *FROM userTable WHERE id = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, rowId);
		ResultSet rs = p1.executeQuery();
		String storedId="";
		if(rs.next())
		{
			storedId=rs.getString(type+"_user_name");
		}
		if(storedId==null || storedId.equals(userId))
			return true;
		else
			return false;
	}
	public void rollBack(String uuid) throws SQLException {
		String selectSQL = "delete FROM userTable WHERE id= ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, uuid);
		p1.executeUpdate();
	}
	public long storeId(String userId,String type,String rowId ) throws SQLException {
		long currTime=System.currentTimeMillis()%10000000;
		if(rowId==null)
		{String insertTableSQL = "INSERT INTO usertable ("+type+"_user_name,id) VALUES"
				+ "(?,?)";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
		preparedStatement.setString(1, userId);
		preparedStatement.setLong(2, currTime);
		preparedStatement .executeUpdate();
		return currTime;
		}
		else
		{
			if(!checkAlreadyStoredId(userId,type,rowId))
				return -1;
			String insertTableSQL = "update   usertable set "+type+"_user_name=? where id=? ";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, rowId);
			preparedStatement .executeUpdate();
			return Long.parseLong(rowId);
		}
		
		}
	public int storeFriends(String user, String friends, JSONArray friendsArray) throws SQLException {
		int i=0;
		user=getUserId( user,checkType(user));
		if(user==null)
			return 0;
		for(i=0;i<friendsArray.length();i++)
		{
			try{
			String friendId=	getUserId( (String) friendsArray.get(i),checkType( (String) friendsArray.get(i)));
			if(friendId==null)
				continue;
			String insertTableSQL = "INSERT INTO user_friends  VALUES"
					+ "(?,?)";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, user);
			preparedStatement.setString(2,friendId);
			preparedStatement .executeUpdate();
			}
			catch(SQLException e)
			{
				if(e.getMessage().contains("Duplicate entry"))
				{//pair of both the id is the primary key so it saves the time to check if the user already exists
					continue;
				}
			}
			catch(Exception e)
			{
				JSONService.printConsoleLog(JSONService.getMethodName(2),new java.util.Date(),e);
			}
		}
		
		return i;
	}
	public void storeUuidUser(String uuid, String user,String type) throws SQLException {

		String insertTableSQL = "INSERT INTO usertable (id,"+type+"_user_name) VALUES"
				+ "(?,?)";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
		preparedStatement.setString(1, uuid);
		preparedStatement.setString(2,user);
		preparedStatement .executeUpdate();
		
	}
	public user checkUsedUuid(String uuid) throws SQLException {
		String selectSQL = "SELECT *FROM userTable WHERE id = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, uuid);
		ResultSet rs = p1.executeQuery();
		user u =new user();
		if(rs.next())
		{
			u.setId(rs.getString("id"));
			u.setFb_user_name(rs.getString("fb_user_name"));
			u.setTw_user_name(rs.getString("tw_user_name"));
			u.setGm_user_name(rs.getString("gm_user_name"));
			return u;
		}
		else
		{
			return null;
		}
	}
	public void storeOldUuidUser(String uuid, String user, String type) throws SQLException {
		String insertTableSQL = "update   usertable set "+type+"_user_name=? where id=? ";
		PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
		preparedStatement.setString(1, user);
		preparedStatement.setString(2, uuid);
		preparedStatement .executeUpdate();
	}
	public user checkSavedEntryForUserId(String user, String type) throws SQLException {
		String selectSQL = "SELECT *FROM userTable WHERE "+type+"_user_name = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, user);
		ResultSet rs = p1.executeQuery();
		user u =new user();
		if(rs.next())
		{
			u.setId(rs.getString("id"));
			u.setFb_user_name(rs.getString("fb_user_name"));
			u.setTw_user_name(rs.getString("tw_user_name"));
			u.setGm_user_name(rs.getString("gm_user_name"));
			return u;
		}
		else
		{
			return null;
		}
		
	}
	public boolean mergeUuids(user oldRow, String oldUuId, String uuid) throws SQLException {
		user newRow=checkUsedUuid(uuid);
		ArrayList<String> types=new ArrayList<String>();
		types.add("fb");
		types.add("gm");
		types.add("tw");
		for(String type :types)
		{
//			storeOldUuidUser(oldUuId,newRow.getString(type+"_user_name"),checkType(newRow.getString(type+"_user_name")));
			if(newRow.getString(type+"_user_name")!=null)
			{
				if(oldRow.getString(type+"_user_name")==null)
				{
					storeOldUuidUser(oldUuId,newRow.getString(type+"_user_name"),checkType(newRow.getString(type+"_user_name")));
				}
				else if(!oldRow.getString(type+"_user_name").equals(newRow.getString(type+"_user_name")))
				{
					//Conflicting data
					return false;
				}
			}
		}

		String selectSQL = "delete FROM userTable WHERE id= ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, uuid);
		p1.executeUpdate();
		return true;
	}
	public JSONArray getFriends(String uuid) throws SQLException {

		String selectSQL = "SELECT friend_id FROM user_friends WHERE user_id= ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, uuid);
		ResultSet rs = p1.executeQuery();
		JSONArray friends=new JSONArray();
		while(rs.next())
		{
			friends.put(rs.getString("friend_id"));
		}
		return friends;
	
	}
	public boolean checkConflict(String oldUuId, String user, String type) throws SQLException {
		String selectSQL = "SELECT *FROM userTable WHERE id = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, oldUuId);
		ResultSet rs = p1.executeQuery();
		if(rs.next())
		{
			String storedUSerId=rs.getString(type+"_user_name");
			
			if( storedUSerId.equals(user))
				return false;
			else
				return true;
		}
		return false;
	}
	public void upVoteLink(String link) throws SQLException {
		String selectSQL = "SELECT * FROM link_upvotes WHERE link = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, link);
		ResultSet rs = p1.executeQuery();
		int upvotes=0;
		if(rs.next())
		{
			upvotes=rs.getInt("upvotes");
		}
		if(upvotes==0)
		{
			String insertTableSQL = "INSERT INTO link_upvotes (link,upvotes) VALUES"
					+ "(?,?)";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, link);
			preparedStatement.setInt(2,++upvotes);
			preparedStatement .executeUpdate();
		}
		else
		{
			String insertTableSQL = "update   link_upvotes set upvotes=? where link=? ";
			PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
			preparedStatement.setInt(1, ++upvotes);
			preparedStatement.setString(2, link);
			preparedStatement .executeUpdate();
		
		}
	}
	public void storeTimeTakenInDb(String id, String link, String query, long timeTaken,long uuid) throws SQLException {
		String selectSQL = "SELECT * FROM timeSpent WHERE uuid = ?  and userid =?";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setLong(1, uuid);
		p1.setString(2, id);
		
		ResultSet rs = p1.executeQuery();
		if(!rs.next())
		{
			String insertTableSQL = "INSERT INTO timeSpent (link,userid,query,time,uuid) VALUES"
					+ "(?,?,?,?,?)";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, link);
			preparedStatement.setString(2,id);
			preparedStatement.setString(3,query);
			preparedStatement.setLong(4,timeTaken);
			preparedStatement.setLong(5,uuid);
			preparedStatement .executeUpdate();
		}
		else
		{
			String insertTableSQL = "update   timeSpent set time=? where uuid=? and userid =? ";
			PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, timeTaken);
			preparedStatement.setLong(2, uuid);
			preparedStatement.setString(3, id);
			preparedStatement .executeUpdate();
		
		}
	}
	
	public ResultSet getTimeSpentRows() throws SQLException {
		String selectSQL = "SELECT * FROM timeSpent where `userid` not in ('undefined@fb.com')";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		ResultSet rs = p1.executeQuery();
		return rs;
	}
	
	public void storeComputedUsers(HashMap<Integer, UserIds> userMap) throws SQLException {
		 Iterator it = userMap.entrySet().iterator();
		 String selectSQL = "delete FROM userTable  ";
			PreparedStatement p1 = Settings.getUpdateConn().prepareStatement(selectSQL);
			p1.executeUpdate();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        UserIds userIds = (UserIds) pair.getValue();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        String insertTableSQL = "INSERT INTO usertable (fb_user_name,gm_user_name,tw_user_name) VALUES"
						+ "(?,?,?)";
				PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
				preparedStatement.setString(1,userIds.getFb());
				preparedStatement.setString(2,userIds.getGm());
				preparedStatement.setString(3,userIds.getTw());
				preparedStatement .executeUpdate();
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    System.out.println("PPPPP");
		
	}
	
	/*
	 * This method will return result of rows for which implicit
	 * likes algorithm needs to be run
	 */
	public ResultSet getRowsToProcess(int processedRows) throws SQLException {
		String selectSQL = "SELECT * FROM timeSpent LIMIT ?,18446744073709551615";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setInt(1, processedRows);
		ResultSet rs = p1.executeQuery();
		return rs;
	}
	
	/*
	 * This method constructs string for 'IN' query
	 */
	public String constructUserStringFromArrayList(ArrayList<String> list) {
		String sql="";
		
		if (list.size() == 0) {
			sql = sql + "''";
		}
		
		for (int i=0; i < list.size(); i++) {
			if (i==0) {
				sql =sql +"'"+list.get(i)+"'";
				continue;
			}
			sql = sql +","+"'"+list.get(i)+"'";
		}
		sql= sql + "";
		
		return sql;
	}
	
	/*
	 * This method returns usertable rows from which hashmap should be made
	 * to process once per user who has registered with (fb/gm/tw)
	 */
	public ResultSet getGroupedUsers(ArrayList<String> fbUserIds, ArrayList<String> gmUserIds, ArrayList<String> twUserIds) throws SQLException {
		String selectSQL = "SELECT * FROM usertable where fb_user_name IN (" +
							constructUserStringFromArrayList(fbUserIds) +
							") OR gm_user_name IN (" +
							constructUserStringFromArrayList(gmUserIds) + 
							") OR tw_user_name IN (" +
							constructUserStringFromArrayList(twUserIds) +
							")";
		
		stmt = Settings.getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = stmt.executeQuery(selectSQL);
		return rs;
	}
	
	/*
	 * This method returns all rows that need to be processed for particular user and query
	 * User/query - get Links
	 * User/Links - get Queries
	 * User/Queries - get final links
	 * 
	 * Confusing right? Tell me about it :-/
	 */
	public ResultSet getTimeSpentRowsForUserQuery(String userid, Object object) throws SQLException {
		String selectSQL = "SELECT * FROM timespent where userid = ? AND query = ?";
		
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, userid);
		p1.setString(2, object.toString());
		
		ResultSet rs = p1.executeQuery();
		
		ArrayList<String> linksFromQuery = new ArrayList<String>();
		String links = "";
		
		while (rs.next()) {
			linksFromQuery.add(rs.getString("link"));
		}
		
		String psql = "SELECT * FROM timespent where userid = ? AND link IN (";
		for (int i=0; i < linksFromQuery.size(); i++) {//linksFromQuery.get(i)
			if (i == 0) {
				links = links + "?";
				continue;
			}
			links = links + "," + "?";
		}
		psql = psql + links + ")";
		
		PreparedStatement p2 = Settings.getConn().prepareStatement(psql);
		p2.setString(1, userid);
		int i=2;
		for (String qq : linksFromQuery) {
			p2.setString(i++, qq);
		}
		ResultSet rs2 = p2.executeQuery();
		
		Set<String> queries = new HashSet<String>();
		String tmpQuery = "";
		while (rs2.next()) {
			queries.add(rs2.getString("query"));
		}
		
		String finalSql = "SELECT * FROM timespent where userid = ? AND query IN (";
		i = 0;
		for (String qq : queries) {
			if (i == 0) {
				tmpQuery = tmpQuery + "?";
				i++;
				continue;
			}
			i++;
			tmpQuery = tmpQuery + "," + "?";			
		}
		finalSql = finalSql + tmpQuery + ")";
		
		PreparedStatement p3 = Settings.getConn().prepareStatement(finalSql);
		p3.setString(1, userid);

		i=2;
		for (String qq : queries) {
			p3.setString(i++, qq);
		}
		
		ResultSet rs3 = p3.executeQuery();
		
		return rs3;
	}
	
	/*
	 * This method upvotes or downvotes rows in timespent for user/query/link
	 */
	public void updateTimeSpentRow(String userid, String query, String link, int vote, int time) throws SQLException {
		String insertTableSQL = "update timespent set vote=? where userid=? and query=? and link = ? and time = ?";
		PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
		preparedStatement.setInt(1, vote);
		preparedStatement.setString(2, userid);
		preparedStatement.setString(3, query);
		preparedStatement.setString(4, link);
		preparedStatement.setInt(5, time);
		preparedStatement .executeUpdate();

		return;
	}
	
	/*
	 * Function being used to check for explicit user upvotes for any link being processed in timespent table
	 */
	public boolean checkExplicitVote(ArrayList<TimeSpent> timeSpentRows /*, HashMap<String, ArrayList<String>> uniqUsersMap*/) throws SQLException {

//		String selectSQL = "SELECT *FROM linksTable WHERE userid IN (?) and links IN (?)";
		String psql = "SELECT *FROM linksTable WHERE links IN (";
		String userids = "";
		String links = "";
			
		for (int i=0; i < timeSpentRows.size(); i++) {
			if (i == 0) {
				links = links + "?";
				continue;
			}
			links = links + "," + "?";
		}
		psql = psql + links + ") and userid IN (";
		
		
		for (int i=0; i < timeSpentRows.size(); i++) {
			if (i == 0) {
				userids = userids + "?";
				continue;
			}
			userids = userids + "," + "?";
		}
		psql = psql + userids + ")";
		PreparedStatement preparedStatement2 = Settings.getConn().prepareStatement(psql);
		int j=1;
		for (TimeSpent row : timeSpentRows) {
			preparedStatement2.setString(j++, row.getLink());
		}
		for (TimeSpent row : timeSpentRows) {
			preparedStatement2.setString(j++, row.getUserId());
		}
		
		ResultSet rs2 = preparedStatement2.executeQuery();
		if (rs2.next()) {
			return true;	
		}
		return false;
	}
	
	/*
	 * Use this function to check and insert/upvote link in timespent_link_upvotes
	 */
	public void upVoteTimeSpentLink(String link) throws SQLException {
		String selectSQL = "SELECT * FROM timespent_link_upvotes WHERE link = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, link);
		ResultSet rs = p1.executeQuery();
		int upvotes=0;
		
		if (rs.next()) {
			upvotes=rs.getInt("upvotes");
		}
		
		if (upvotes == 0) {
			String insertTableSQL = "INSERT INTO timespent_link_upvotes (link,upvotes) VALUES"
					+ "(?,?)";
			PreparedStatement preparedStatement = Settings.getConn().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, link);
			preparedStatement.setInt(2,++upvotes);
			preparedStatement .executeUpdate();
		} else {
			String insertTableSQL = "update timespent_link_upvotes set upvotes=? where link=? ";
			PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
			preparedStatement.setInt(1, ++upvotes);
			preparedStatement.setString(2, link);
			preparedStatement .executeUpdate();		
		}
	}
	
	/*
	 * Use this function to downvote an already liked link in timespent_link_upvotes
	 */
	public void downVoteTimeSpentLink(String link) throws SQLException {
		String selectSQL = "SELECT * FROM timespent_link_upvotes WHERE link = ? ";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		p1.setString(1, link);
		ResultSet rs = p1.executeQuery();
		int upvotes=0;
		
		if (rs.next()) {
			upvotes=rs.getInt("upvotes");
		}
		
		if (upvotes >= 2) {
			String insertTableSQL = "update timespent_link_upvotes set upvotes=? where link=? ";
			PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);
			preparedStatement.setInt(1, --upvotes);
			preparedStatement.setString(2, link);
			preparedStatement .executeUpdate();
		} else {
			String deleteSQL = "delete from timespent_link_upvotes where link = ?";
			PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(deleteSQL);
			preparedStatement.setString(1, link);
			preparedStatement .executeUpdate();
		}
	}
	
	/*
	 * Get timespent metadata used for implicit likes algo
	 */
	public ResultSet getTimeSpentMetaData() throws SQLException {
		String selectSQL = "SELECT * FROM ts_metadata ORDER BY id DESC LIMIT 1";
		PreparedStatement p1 = Settings.getConn().prepareStatement(selectSQL);
		ResultSet rs = p1.executeQuery();
		
		return rs;
	}
	
	/*
	 * Use this function to check and insert/upvote link in timespent_link_upvotes
	 */
	public void setLatestTimeSpentMetaData(int lastProcessedNum, int curprocessNum, int processedNum) throws SQLException {
		String insertTableSQL = "INSERT INTO ts_metadata (lastProcessedNum, curprocessNum, processedNum) VALUES"
				+ "(?,?,?)";
		PreparedStatement preparedStatement = Settings.getUpdateConn().prepareStatement(insertTableSQL);			
		preparedStatement.setInt(1, lastProcessedNum);
		preparedStatement.setInt(2, curprocessNum);
		preparedStatement.setInt(3, processedNum);
		preparedStatement .executeUpdate();
		
		return;
	}
}
