package com.frinserService.rest;

import homeAuto.Content;
import homeAuto.POST2GCM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.frinser.dto.Edge;
import com.frinser.dto.UserIds;
import com.frinserService.Track;
import com.frinserService.wowData;
import com.frinser.dto.TimeSpent;
import com.projectY.dao.DataDb;
import com.projectY.dao.user;
@Path("/extension")
public class JSONService {
	static HashMap<String,String> registeredDevices=new HashMap<String,String>();
	static HashMap<String,String> registeredDevicesIps=new HashMap<String,String>();

	/**
	 * This code runs every day (for now) and makes disconnected graphs with user ids as the nodes and the number of times they occur together
	 * as the edges, so for example if a fb node is connected to multiple gmail nodes than the node with highest number of edges will alloted to the fb id
	 */
	/**
	 * This method uses the connection id sent by the client to retrieve its thread and resumes it
	 * @param request
	 * @param track
	 * @return
	 */
	@POST
	@Path("/toggleRemotes1")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response turnOnOff(@Context HttpServletRequest request, String track)  
	{
		System.out.println(track);
		return Response.status(201).entity("qewqgertr").build();	
	}
	private void post(String regid,String toggle)
	{
	        System.out.println( "Sending POST to GCM" );

	        String apiKey = "AIzaSyBTkPMFyo_pLtmVvPfBrLQC336Vz1wDR98";
	        Content content = createContent(regid);

	        try {
				POST2GCM.post(apiKey, content,toggle);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static Content createContent(String regid){
        Content c = new Content();
        c.addRegId(regid);
        return c;
    }
	@POST
	@Path("/postTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerDevice(@Context HttpServletRequest request, String track) throws JSONException  
	{
//		JSONObject j = new JSONObject(track);
		
		JSONObject jObj = new JSONObject();
		jObj.put("ip", "192.168.4.9");
		jObj.put("id", "slave1");
		System.out.println(track);
		jObj.put("url", "/toggle?id=SMARTHOME_M_1&slaveid=SLave2&relay=2&togState=1");
		return Response.status(201).entity("http://192.168.4.2/").build();	
	}
	@POST
	@Path("/registerDeviceIp")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerDeviceIp(@Context HttpServletRequest request, String track)  
	{
		String id="",ip="";
		try{
		JSONObject jObj= new JSONObject(track);
		 id = jObj.getString("id");
		 ip = jObj.getString("ip");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		registeredDevicesIps.put(id,ip);
		return Response.status(201).entity(new JSONObject()).build();	
	}
	@POST
	@Path("/getDeviceIp")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDeviceIp(@Context HttpServletRequest request, String track) throws JSONException  
	{
		String id="",ip="";
		try{
		JSONObject jObj= new JSONObject(track);
		 id = jObj.getString("id");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		ip=registeredDevicesIps.get(id)==null || registeredDevicesIps.get(id).trim().length()==0?"1.1.1.1":registeredDevicesIps.get(id);
		JSONObject jObj =new JSONObject();
		jObj.put("ip",ip);
		return Response.status(201).entity(jObj).build();	
	}

	protected static DataDb dataDb = null;
	protected static HashMap<String, String> megrgedUuids = new HashMap<String, String>();

	@GET
	@Path("/toggleRemotes")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTrackInJSON() {

		System.out.println("dfsgsdfhsfhsnfjdgfjdjdmd,mf,fg,fgh");
		return "slaves=2&slave1=192.168.4.104,SMARTHOME_M_1,0^0^1^0&slave2=192.168.4.2,slave1,0^1^0^0";
}
	
	
	@GET
	@Path("/get")
	public Response get(@Context HttpServletRequest request,String track) throws JSONException,
			SQLException {
		System.out.println(request.getRemoteAddr());
		JSONObject res =new JSONObject();
		res.put("site", "SDSD");
		
		System.out.println("res ::: \n\n\n" + res + "\n\n\n");
		return Response.status(201).entity(res).build();	}
	@POST
	@Path("/getLinkUpvotes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(String track) throws JSONException,
			SQLException {
		// {"links":["http://www.cs.utexas.edu/users/djimenez/utsa/cs1723/lecture2.html",
		// "http://dl.acm.org/citation.cfm?id=73040","http://en.wikipedia.org/wiki/Heap_(data_structure)",
		// "http://en.wikipedia.org/wiki/Heap_(data_structure)"],"user":"singh.ishupreet@gmail.com","site":"twitter"}
		long start=System.currentTimeMillis();
		printConsoleLog(getMethodName(2) +" INITIAL START TIME "+start +" ",new Date(),track + " track");
		printConsoleLog(getMethodName(2),new Date(),track + " track");
		JSONObject obj = new JSONObject(track);
		JSONArray linksArray = obj.getJSONArray("links");
		ArrayList<String> links = new ArrayList<String>();
		for (int i = 0; i < linksArray.length(); i++) {
			links.add(linksArray.getString(i));
		}
		JSONArray friendsArray = obj.getJSONArray("friends");
		String site = obj.getString("site");

		//String user = obj.getString("user");
		ArrayList<String> friends = new ArrayList<String>();
		//String uuid = dataDb.getUserId(user, checkType(user));
//		if (site.equalsIgnoreCase("selfLike")) {
//			friends.add(uuid);
//		} else {
//			friendsArray = dataDb.getFriends(uuid);
//			for (int i = 0; i < friendsArray.length(); i++) {
//				friends.add(friendsArray.getString(i).toLowerCase());
//			}
//
//		}
		for (int i = 0; i < friendsArray.length(); i++) {
			friends.add(friendsArray.getString(i).toLowerCase());
		}
		
		printConsoleLog(getMethodName(2),new Date(),links + " \n " + friends);
		JSONObject res = new DataDb().readLinksProfile(links, friends, site);
		res.put("site", site);
		
		printConsoleLog(getMethodName(2),new Date(),"res ::: \n\n\n" + res + "\n\n\n");
		long end=System.currentTimeMillis();
		printConsoleLog(getMethodName(2) +" INITIAL END TIME "+(end-start)/1000 +" ",new Date(),track + " track");
		return Response.status(201).entity(res).build();	
		}
	@POST
	@Path("/storeTimeSpent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeTimeSpent(@Context HttpServletRequest request, String track)  
	{
		String id="",link="",query;
		long timeTaken,uuid;
		try{
		JSONObject jObj= new JSONObject(track);
		 id = jObj.getString("userid");
		 link = jObj.getString("link");
		 query = jObj.getString("query");
		 timeTaken = jObj.getLong("timeTaken");
		 uuid = jObj.getLong("uuid");
		 
		 if("undefined@fb.com".equalsIgnoreCase(id))
		 {
			 return Response.status(201).entity(new JSONObject()).build();	
		 }
		 dataDb.storeTimeTakenInDb(id,link,query,timeTaken,uuid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return Response.status(201).entity(new JSONObject()).build();	
	}
	
	@POST
	@Path("/storeFriends")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeFriends(String track) throws JSONException,
			SQLException {
		printConsoleLog(getMethodName(2),new Date(),track + " track");
		JSONObject obj = new JSONObject(track);
		final String user = obj.getString("user");
		final JSONArray friendsArray = new JSONArray(obj.getString("friends"));
		final String friends = (obj.getString("friends"));
		friends.replace('[', '(');
		friends.replace(']', ']');

		Runnable r = new Runnable() {
			public void run() {
				try {
					dataDb.storeFriends(user, friends, friendsArray);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		new Thread(r).start();
		JSONObject res = new JSONObject();
		res.put("result", "Saving the data in separate thread");
		return Response.status(201).entity(res).build();

	}

	@POST
	@Path("/storeUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeUser(String track) throws JSONException, SQLException {
		boolean mergeFlag = false;
		String oldUuId = "";
		printConsoleLog(getMethodName(2),new Date(),track + " track");
		JSONObject obj = new JSONObject(track);
		String uuid = obj.getString("uuid");
		String user = obj.getString("user");
		String type = checkType(user);
		user userPresentinDb = dataDb.checkSavedEntryForUserId(user, type);
		// mergedUUIds has the pairs of the ids that are represeting the same
		// row that is being at
		// different times ,like same user is trying to signup twice
		// if(megrgedUuids.containsKey(uuid))
		// {
		// printConsoleLog(getMethodName(2),new Date(),"ALREADY MERGED");
		// //this method merges 2 uuids because in the scenario when gmail is
		// not present reaches backend first then the already stored
		// // uuids get there, and they tell the tom that the just stored uuid
		// is same as before, so we need to merge these uuids, now here
		// // conflict can occur.
		// mergeAnyStoredRowsfor2Uuids(uuid,userPresentinDb.getId());
		// }
		// else
		// {
		printConsoleLog(getMethodName(2),new Date(),"User: " + userPresentinDb);
		if (userPresentinDb != null) {
			megrgedUuids.put(uuid, userPresentinDb.getId());
			printConsoleLog(getMethodName(2),new Date(),"Putting in MERGED");
			String userNameOfType = userNameOfType(userPresentinDb, type);
			if (userNameOfType == null || user.equals(userNameOfType)) {
				dataDb.storeOldUuidUser(userPresentinDb.getId(), user, type);
			} else {
				// CONFLICCTTTTTTTTT
			}
		} else {
			dataDb.storeUuidUser(uuid, user, type);
		}
		if (megrgedUuids.containsKey(uuid)) {
			printConsoleLog(getMethodName(2),new Date(),"ALREADY MERGED");
			// this method merges 2 uuids because in the scenario when gmail is
			// not present reaches backend first then the already stored
			// uuids get there, and they tell the tom that the just stored uuid
			// is same as before, so we need to merge these uuids, now here
			// conflict can occur.
			mergeAnyStoredRowsfor2Uuids(uuid, userPresentinDb.getId());
		}
		// }
		// user oldRow=dataDb.checkSavedEntryForUserId(user,type);
		// if(megrgedUuids.containsKey(uuid))
		// {
		//
		// boolean
		// mergeConflict=dataDb.checkConflict(megrgedUuids.get(uuid),user,type);
		// dataDb.rollBack(uuid);
		// if(mergeConflict)
		// {
		// JSONObject res=new JSONObject();
		// res.put("result", "Merge Conflict");
		// return Response.status(201).entity(res).build();
		// }
		// else
		// {
		// JSONObject res=new JSONObject();
		// res.put("result", "Data saved successfully");
		// return Response.status(201).entity(res).build();
		// }
		//
		// }
		// if(oldRow!=null)
		// {
		// oldUuId=oldRow.getString("id");
		// mergeFlag=!uuid.equals(oldUuId);
		// if(megrgedUuids.containsKey(uuid))
		// {
		// boolean mergeConflict=dataDb.checkConflict(oldUuId,user,type);
		// dataDb.rollBack(uuid);
		// if(mergeConflict)
		// {
		// JSONObject res=new JSONObject();
		// res.put("result", "Merge Conflict");
		// return Response.status(201).entity(res).build();
		// }
		// else
		// {
		// JSONObject res=new JSONObject();
		// res.put("result", "Data saved successfully");
		// return Response.status(201).entity(res).build();
		// }
		// }
		// else
		// {
		// megrgedUuids.put(uuid,oldUuId);
		// }
		// }
		//
		// user newRow=dataDb.checkUsedUuid(uuid);
		// if(newRow==null)
		// {
		// dataDb.storeUuidUser(uuid,user,type);
		// }
		// else
		// { String storedUserId=newRow.getString(type+"_user_name");
		// if(storedUserId==null || storedUserId.equals(user))
		// {
		// dataDb.storeOldUuidUser(uuid,user,type);
		// }
		// }
		// if(mergeFlag)
		// {
		// megrgedUuids.put(uuid,oldUuId);
		// boolean mergeable=dataDb.mergeUuids(oldRow,oldUuId,uuid);
		// if(!mergeable)
		// {
		// dataDb.rollBack(uuid);
		// JSONObject res=new JSONObject();
		// res.put("result", "Problem saving data");
		// return Response.status(201).entity(res).build();
		// }
		// }
		JSONObject res = new JSONObject();
		res.put("result", "Data saved successfully");
		return Response.status(201).entity(res).build();

	}

	private String userNameOfType(user user, String type) {
		if (type.contains("fb"))
			return user.getFb_user_name();
		else if (type.contains("gm"))
			return user.getGm_user_name();
		else
			return user.getTw_user_name();
	}

	private void mergeAnyStoredRowsfor2Uuids(String uuid, String id) {

	}

	private Response checkTwo(ArrayList<JSONObject> jObjs) throws SQLException,
			JSONException {
		String storedId = null;
		JSONObject res = new JSONObject();
		ArrayList<String> idsToBeStored = new ArrayList<String>();
		for (JSONObject jObj : jObjs) {
			if (jObj.has("user")) {
				String userId = jObj.getString("user");
				String newId = dataDb.getUserId(userId, checkType(userId));
				if (storedId != null && newId != null
						&& !storedId.equals(newId)) {
					res.put("result", "Conflicting data");
					return Response.status(201).entity(res).build();
				}
				if (newId == null) {
					idsToBeStored.add(userId);
				} else {
					storedId = newId;
				}
			}
		}
		for (String id : idsToBeStored) {
			long newStoredId = dataDb.storeId(id, checkType(id), storedId);
			if (newStoredId > 0) {
				storedId = String.valueOf(newStoredId);
				res.put("site", "Data stored");
				;
			} else if (newStoredId == -1) {
				res.put("result", "Conflicting data");
			}
		}

		if (idsToBeStored.size() == 0) {
			res.put("site", "Data already there");
			;
		}

		return Response.status(201).entity(res).build();
	}

	private Response checkOne(ArrayList<JSONObject> jObjs)
			throws JSONException, SQLException {
		String id = "";
		for (JSONObject jObj : jObjs) {
			if (jObj.has("user")) {
				id = jObj.getString("user");
				break;
			}
		}
		String type = checkType(id);
		String storedId = dataDb.getUserId(id, type);
		JSONObject res = new JSONObject();
		if (storedId == null) {
			long rowsStored = dataDb.storeId(id, type, null);
			if (rowsStored != 0) {
				res.put("result", "Data stored");
			} else {
				res.put("result", "Problem saving data");
			}
		} else {
			res.put("site", "Data already there");
		}
		return Response.status(201).entity(res).build();

	}

	private String checkType(String user) {

		if (user.contains("fb"))
			return "fb";
		else if (user.contains("gmail"))
			return "gm";
		else
			return "tw";

	}

	@POST
	@Path("/getNonFriendLinkUpvotes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNonFriendLinkUpvotes(String track) throws JSONException,
			SQLException {
		// {"links":["http://www.cs.utexas.edu/users/djimenez/utsa/cs1723/lecture2.html",
		// "http://dl.acm.org/citation.cfm?id=73040","http://en.wikipedia.org/wiki/Heap_(data_structure)",
		// "http://en.wikipedia.org/wiki/Heap_(data_structure)"],"friends":["arushi","sumit","vrinda","ishu"],"site":"twitter"}
		printConsoleLog(getMethodName(2),new Date(),track + " track");
		JSONObject obj = new JSONObject(track);
		JSONArray linksArray = obj.getJSONArray("links");
		ArrayList<String> links = new ArrayList<String>();
		for (int i = 0; i < linksArray.length(); i++) {
			links.add(linksArray.getString(i));
		}
		String site = obj.getString("site");

		JSONObject res = new JSONObject(dataDb.getNonFriendLinkData(links));
		res.put("site", site);
		return Response.status(201).entity(res).build();
	}
	@POST
	@Path("/debugLog")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response debugLog(String track) throws JSONException,
 SQLException,
			IOException {
		String user = "";
		String logData = "";
		String localId="";
		System.out.println(getMethodName(2)+"::"+new Date()+"::"+"track " + track);
		try{
		JSONObject obj = new JSONObject(track);
		localId = obj.getString("idLocal");
		user = obj.getString("user");
		logData = " " + obj.getString("log");
		}
		catch(Exception e)
		{
			//user should be the first param
			System.out.println(getMethodName(2)+"::"+new Date()+"::"+"exception " + e);
			String tempUser=track.split(",")[0]+"}";
			JSONObject tempObj = new JSONObject(tempUser);
			user=tempObj.getString("user");
			writeToFile(user, "Exception "+track);
			System.out.println(getMethodName(2)+"::"+new Date()+"::"+"exception occured\n"+ new Date() +track);
		}
		System.out.println(getMethodName(2)+"::"+new Date()+"::"+"ID: "+localId+" "+user + " " + logData);
		
		writeToFile(user, "ID: "+localId+" "+logData);
		JSONObject res = new JSONObject();
		res.put("log", "ok");
		return Response.status(201).entity(res).build();
	}

	private static void writeToFile(final String user,final String logData) throws IOException {
		Thread printThread = new Thread(new Runnable() {

	        @Override
	        public void run() {
	            try { 
		String filePath = ServiceConstants.debugLogFilePath+user+".txt";
		File file= new File(filePath);
		if(!file.exists())
		{
			file.createNewFile();
		}
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true))) ;
		out.println(logData);
		out.close();
	            }
	            catch(Exception e)
	            {
	            e.printStackTrace();	
	            }
	            }

	        });
	    	printThread.start();
		
	}
	public static String getMethodName(final int depth)
	{
	  final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	  return ste[depth].getMethodName(); //Thank you Tom Tresansky
	}
	public static void printConsoleLog(final String methodName,final Date d, final Object data)
	{     		int yr=d.getYear()+1900;
        		String fileName=d.getDate()+"-"+d.getMonth()+"-"+yr;
        		System.out.println(fileName+":::: "+d.getDate()+" "+methodName+": "+data);
//        		try {
//					writeToFile(fileName,d.toString()+": "+ methodName+": "+data);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	}
	
	@POST
	@Path("/upVote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upVote(String track) throws JSONException, SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","user":"ishu"}
		JSONObject obj = new JSONObject(track);
		printConsoleLog(getMethodName(2),new Date(),track);
		String user =obj.getString("user");
		if("undefined@fb.com".equalsIgnoreCase(user))
		{
			JSONObject result = new JSONObject();
			result.put("result", "undefined found");
			return Response.status(201).entity(result).build();
		}
		int res = dataDb.Vote(obj.getString("link"), user, obj.getInt("vote"),
				obj.getString("query"), obj.getInt("tty"));
		printConsoleLog(getMethodName(2),new Date(),res);
		JSONObject result = new JSONObject();

		if (res == -1) {
			result.put("result", "alreadyThere");
		} else if (res == 1) {
			result.put("result", "success");
		} else {
			result.put("result", "failure");
		}
		return Response.status(201).entity(result).build();
	}
	@POST
	@Path("/upvoteLinkGlobal")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upVoteLink(String track) throws JSONException, SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","user":"ishu"}
		JSONObject obj = new JSONObject(track);
		printConsoleLog(getMethodName(2),new Date(),track);
		String link = obj.getString("link"); 
		dataDb.upVoteLink(link);
		JSONObject result = new JSONObject();

		return Response.status(201).entity(result).build();
	}

	@POST
	@Path("/getWow")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getWow(String track) throws JSONException, SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","user":"ishu","site":"wow","query":"football"}
		printConsoleLog(getMethodName(2),new Date(),track);
		JSONObject obj = new JSONObject(track);
		String user = obj.getString("user");
		String uuid = dataDb.getUserId(user, checkType(user));
		JSONArray friendsArray = dataDb.getFriends(uuid);
		JSONArray linksArray = obj.getJSONArray("links");
		String query = obj.getString("query");
		// printConsoleLog(getMethodName(2),new Date(),"AKSHAY1 linksArray:" + linksArray);
		// printConsoleLog(getMethodName(2),new Date(),"AKSHAY2 friendsArray:" + friendsArray);
		// printConsoleLog(getMethodName(2),new Date(),"AKSHAY3 query:" + obj.getString("query"));

		ArrayList<String> links = new ArrayList<String>();
		for (int i = 0; i < linksArray.length(); i++) {
			links.add(linksArray.getString(i));
		}
		String site = obj.getString("site");

		ArrayList<String> friends = new ArrayList<String>();
		for (int i = 0; i < friendsArray.length(); i++) {
			friends.add(friendsArray.getString(i).toLowerCase());
		}
		// printConsoleLog(getMethodName(2),new Date(),"AKSHAY4: links+friends arraylist" + links +
		// " \n " + friends);

		List<wowData> wd = new ArrayList<wowData>(dataDb.getWowData(links,
				friends));
		new wowData();
		for (wowData iterator : wd) {
			printConsoleLog(getMethodName(2),new Date(),"AKSHAY7 " + iterator);
		}

		ArrayList<String> friends2 = new ArrayList<String>();
		List<wowData> wd2 = new ArrayList<wowData>(dataDb.getWowData(links,
				friends2));
		for (wowData iterator : wd2) {
			printConsoleLog(getMethodName(2),new Date(),"AKSHAY8 " + iterator);
		}

		// for each link
		// step 1
		// call function by sending individual link {(String)linksArray.get(i)}
		// and friends of particular website
		// db query should get search query and minutes spent on that link
		// return number1

		// step 2
		// call function by sending individual link {(String)linksArray.get(i)}
		// to get all results back
		// db query should get search query and minutes spent on that link
		// return number2

		// r1 = (n1*2 + n2*1)/30
		// return r1+r2+r3/3

		// initialize wow factors in result hashmap to zero
		HashMap<String, Float> linksFriendsMap = new HashMap<String, Float>();
		for (int i = 0; i < linksArray.length(); i++) {
			linksFriendsMap.put((String) linksArray.get(i), (float) 0.0);
		}

		HashMap<String, Float> linksMap = new HashMap<String, Float>();
		for (int i = 0; i < linksArray.length(); i++) {
			linksMap.put((String) linksArray.get(i), (float) 0.0);
		}

		HashMap<String, Float> linksFriendsTotalTimeMap = new HashMap<String, Float>();
		for (int i = 0; i < linksArray.length(); i++) {
			linksFriendsTotalTimeMap.put((String) linksArray.get(i),
					(float) 0.0);
		}

		HashMap<String, Float> linksTotalTimeMap = new HashMap<String, Float>();
		for (int i = 0; i < linksArray.length(); i++) {
			linksTotalTimeMap.put((String) linksArray.get(i), (float) 0.0);
		}

		HashMap<String, Integer> res = new HashMap<String, Integer>();
		for (int i = 0; i < linksArray.length(); i++) {
			res.put((String) linksArray.get(i), 0);
		}

		calculateMaps(linksArray, query, wd, linksFriendsMap,
				linksFriendsTotalTimeMap);
		calculateMaps(linksArray, query, wd2, linksMap, linksTotalTimeMap);
		//
		for (int i = 0; i < linksArray.length(); i++) {
			printConsoleLog(getMethodName(2),new Date(),(String) linksArray.get(i) + " : "
					+ linksFriendsMap.get((String) linksArray.get(i)));
		}

		for (int i = 0; i < linksArray.length(); i++) {
			printConsoleLog(getMethodName(2),new Date(),(String) linksArray.get(i) + " : "
					+ linksFriendsTotalTimeMap.get((String) linksArray.get(i)));
		}

		for (int i = 0; i < linksArray.length(); i++) {
			printConsoleLog(getMethodName(2),new Date(),(String) linksArray.get(i) + " : "
					+ linksMap.get((String) linksArray.get(i)));
		}

		for (int i = 0; i < linksArray.length(); i++) {
			printConsoleLog(getMethodName(2),new Date(),(String) linksArray.get(i) + " : "
					+ linksTotalTimeMap.get((String) linksArray.get(i)));
		}

		for (int i = 0; i < linksArray.length(); i++) {
			int output = 0;
			int friendsToAllFactor = 2;

			float temp1 = (linksFriendsMap.get((String) linksArray.get(i)) / linksFriendsTotalTimeMap
					.get((String) linksArray.get(i))) * friendsToAllFactor;
			float temp2 = (linksMap.get((String) linksArray.get(i)) / linksTotalTimeMap
					.get((String) linksArray.get(i)));

			float temp3 = (temp1 + temp2) / 3;
			output = (int) (temp3 * 100);
			res.put((String) linksArray.get(i), output);
		}

		JSONObject result = new JSONObject(res);
		Iterator it = res.entrySet().iterator();
		System.out
				.println("##############################################################################################################");
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			printConsoleLog(getMethodName(2),new Date(),pairs.getKey() + " = " + pairs.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
		System.out
				.println("--------------------------------------------------------------------------------------------------------------");
		result.put("site", site);
		return Response.status(201).entity(result).build();
	}

	public String[] getWordsInQuery(String query) {
		String[] words = query.split("\\s+");
		return words;
	}

	public void calculateMaps(JSONArray linksArray, String query,
			List<wowData> wd, HashMap<String, Float> linksFriendsMap,
			HashMap<String, Float> linksFriendsTotalTimeMap)
			throws JSONException {
		for (wowData iterator : wd) {
			printConsoleLog(getMethodName(2),new Date(),"AKSHAY9 " + iterator);
			// printConsoleLog(getMethodName(2),new Date(),"AKSHAY9 " + query);
			float matchCount = 0;
			// find number of words
			String[] words = getWordsInQuery(iterator.getQuery());
			for (int i = 0; i < words.length; i++) {
				if (query.contains(words[i])) {
					matchCount++;
				}
				// printConsoleLog(getMethodName(2),new Date(),words[i] + query.contains(words[i]));
			}
			String[] queryWords = getWordsInQuery(query);
			// printConsoleLog(getMethodName(2),new Date(),"AKSHAY10: matchCount: " + matchCount);
			float matchFactor = matchCount / (float) queryWords.length;
			// printConsoleLog(getMethodName(2),new Date(),"AKSHAY10: matchfator: " + matchFactor);
			float rowOutput = matchFactor * iterator.getTime();
			printConsoleLog(getMethodName(2),new Date(),"AKSHAY11: rowOutput: " + rowOutput);

			if (iterator.getVote() == 1) {
				// add to result
				linksFriendsMap.put(iterator.getLink(),
						linksFriendsMap.get(iterator.getLink()) + rowOutput);
			} else {
				// subtract from result
				// linksFriendsMap.put(iterator.getLink(),
				// linksFriendsMap.get(iterator.getLink()) - rowOutput);
			}

			linksFriendsTotalTimeMap.put(
					iterator.getLink(),
					linksFriendsTotalTimeMap.get(iterator.getLink())
							+ iterator.getTime());
		}

		// for(int i=0;i<linksArray.length();i++)
		// {
		// printConsoleLog(getMethodName(2),new Date(),(String)linksArray.get(i) + " : " +
		// linksFriendsMap.get((String)linksArray.get(i)));
		// }
		//
		// for(int i=0;i<linksArray.length();i++)
		// {
		// printConsoleLog(getMethodName(2),new Date(),(String)linksArray.get(i) + " : " +
		// linksFriendsTotalTimeMap.get((String)linksArray.get(i)));
		// }
	}

	@POST
	@Path("/storeTimeTaken")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeTimeTaken(String track) throws JSONException,
			SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","user":"ishu"}
		track.replace("\\", " ");
		JSONObject obj = new JSONObject(track);
		printConsoleLog(getMethodName(2),new Date(),track);
		int res = dataDb.storeTimTaken(obj.getString("link"),
				obj.getString("twitter"), obj.getString("facebook"),
				obj.getString("gapi"), obj.getInt("tty"));
		printConsoleLog(getMethodName(2),new Date(),res);
		JSONObject result = new JSONObject();

		if (res == -1) {
			result.put("result", "alreadyThere");
		} else if (res == 1) {
			result.put("result", "success");
		} else {
			result.put("result", "failure");
		}
		return Response.status(201).entity(result).build();
	}

	@POST
	@Path("/downVote")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downVote(String track) throws JSONException, SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","user":"ishu"}
		JSONObject obj = new JSONObject(track);
		String user = dataDb.getUserId(obj.getString("user"),
				checkType(obj.getString("user")));
		int res = dataDb.downVote(obj.getString("link"), user,
				obj.getInt("vote"), obj.getString("query"));
		printConsoleLog(getMethodName(2),new Date(),res);
		JSONObject result = new JSONObject();

		if (res == -1) {
			result.put("result", "alreadyThere");
		} else if (res == 1) {
			result.put("result", "success");
		} else {
			result.put("result", "failure");
		}
		return Response.status(201).entity(result).build();
	}

	@POST
	@Path("/upVotingFriends")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upVotingFriends(String track) throws JSONException,
			SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","friends":["ishu","arushi"]}
		JSONObject obj = new JSONObject(track);
		JSONArray friendsArray = obj.getJSONArray("friends");
		ArrayList<String> friends = new ArrayList<String>();
		for (int i = 0; i < friendsArray.length(); i++) {
			friends.add(friendsArray.getString(i));
		}
		JSONArray res = new JSONArray(dataDb.upVotingFriends(
				obj.getString("link"), friends));
		printConsoleLog(getMethodName(2),new Date(),res);
		return Response.status(201).entity(res).build();
	}

	@POST
	@Path("/isVoted")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response isVoted(String track) throws JSONException, SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","friends":["ishu","arushi"]}
		JSONObject obj = new JSONObject(track);
		JSONObject res = new JSONObject();
		DataDb dataDb2 = new DataDb();
		if (dataDb2.checkIfUpVoted(
				obj.getString("link"),
				obj.getString("user"))) {
			res.put("result", 1);
		} else {
			res.put("result", -1);
		}
		return Response.status(201).entity(res).build();
	}

	@POST
	@Path("/storeQuery")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeQuery(String track) throws JSONException, SQLException {
		// {"link":"http://www.mkyong.com/jdbc/jdbc-sasa-example-insert-a-record","user":"ishu"}
		track.replace("\\", " ");
		JSONObject obj = new JSONObject(track);
		printConsoleLog(getMethodName(2),new Date(),track);
		int res = dataDb.storeQuery(obj.getString("link"),
				obj.getString("user"));
		printConsoleLog(getMethodName(2),new Date(),res);
		JSONObject result = new JSONObject();

		if (res == -1) {
			result.put("result", "alreadyThere");
		} else if (res == 1) {
			result.put("result", "success");
		} else {
			result.put("result", "failure");
		}
		return Response.status(201).entity(result).build();
	}

}