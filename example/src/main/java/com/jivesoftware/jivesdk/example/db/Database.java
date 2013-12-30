package com.jivesoftware.jivesdk.example.db;

import com.jivesoftware.jivesdk.api.Credentials;
import com.jivesoftware.jivesdk.api.TileInstance;
import com.jivesoftware.jivesdk.impl.CredentialsImpl;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.util.DefaultPrettyPrinter;

import java.io.*;
import java.util.*;

/**
 * Implementation of in-memory-only persistence for Mock Jive ID.
 */
public class Database implements Serializable {

    private static File file;

    private Map<String,AccessToken> accessTokens =
            Collections.synchronizedMap(new TreeMap<String,AccessToken>());
    private Map<String,AuthorizationCode> authorizationCodes =
            Collections.synchronizedMap(new TreeMap<String,AuthorizationCode>());
    private Map<String,Application> applications =
            Collections.synchronizedMap(new TreeMap<String,Application>());
    private Map<String,RefreshToken> refreshTokens =
            Collections.synchronizedMap(new TreeMap<String,RefreshToken>());
    private Map<String, String> ticketToUsers =
            Collections.synchronizedMap(new TreeMap<String,String>());
    private Map<String, TileInstance> tileInstances =
            Collections.synchronizedMap(new TreeMap<String,TileInstance>());
    private Map<String, TileDefinition> tileDefinitions =
            Collections.synchronizedMap(new TreeMap<String,TileDefinition>());
    private Map<String, ExternalActivity> dealroomActivity =
            Collections.synchronizedMap(new TreeMap<String,ExternalActivity>());
    private Map<String, ExternalComment> dealroomComments = //Comments with an external ID
            Collections.synchronizedMap(new TreeMap<String,ExternalComment>());
    private Map<String, ExternalComment> jiveComments = //Comments without an external ID, keyed on jive ID.
            Collections.synchronizedMap(new TreeMap<String,ExternalComment>());
    private Map<String, JiveInstance> jiveInstanceMap =
            Collections.synchronizedMap(new TreeMap<String, JiveInstance>());

    static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		SimpleModule module =
				new SimpleModule("Database", new Version(1,0,0,""));
		module.addAbstractTypeMapping(Credentials.class, CredentialsImpl.class);
		mapper.registerModule(module);
	}

	private static Database instance;

    private Database() {
	}

    public void clearAll() {
        clear();
        this.applications.clear();
        this.tileDefinitions.clear();
    }

    public void clear() {
        this.accessTokens.clear();
        this.authorizationCodes.clear();
        this.refreshTokens.clear();
        this.ticketToUsers.clear();
        this.tileInstances.clear();
        this.dealroomActivity.clear();
        this.dealroomComments.clear();
        this.jiveInstanceMap.clear();
    }

    @JsonGetter
    public Map<String,AccessToken> accessTokens() {
        return this.accessTokens;
    }

    @JsonGetter
    public Map<String,AuthorizationCode> authorizationCodes() {
        return this.authorizationCodes;
    }

    @JsonGetter
    public Map<String,Application> applications() {
        return this.applications;
    }

    @JsonGetter
    public Map<String,RefreshToken> refreshTokens() {
        return this.refreshTokens;
    }

    @JsonGetter
    public Map<String, String> ticketToUsers() {
        return ticketToUsers;
    }

    @JsonGetter
    public Map<String, TileInstance> tileInstances() {
        return tileInstances;
    }

    @JsonGetter
    public Map<String, TileDefinition> tileDefinitions() {
        return tileDefinitions;
    }

    @JsonGetter
    public Map<String, ExternalActivity> dealroomActivity() {
        return dealroomActivity;
    }

    @JsonGetter
    public Map<String, ExternalComment> dealroomComments() {
        return dealroomComments;
    }

    public Map<String, JiveInstance> getJiveInstanceMap() {
        return jiveInstanceMap;
    }

    //If the comment has a Dealroom ID (id != null) only replace it if it doesn't exist, since we are the authority on dealroom comments
    //If the comment is from Jive (id == null), then always replace, because Jive is the authority on Jive comments.
    public void updateComment(ExternalComment comment) {
        if (comment.getId() != null && dealroomComments.get(comment.getId()) == null) {
            dealroomComments.put(comment.getId(), comment);
        }
        else if (comment.getId() == null && comment.getJiveURI() != null) {
            jiveComments.put(comment.getJiveURI(), comment);
        }
        else {
            //TODO: Error condition
        }
    }

    public List<ExternalComment> commentsForActivity(String activityID) {
        List<ExternalComment> comments = new ArrayList<ExternalComment>();
        for (ExternalComment comment: dealroomComments.values()) {
            if (comment.getParentActivityID().equals(activityID)) {
                comments.add(comment);
            }
        }
        for (ExternalComment comment: jiveComments.values()) {
            if (comment.getParentActivityID().equals(activityID)) {
                comments.add(comment);
            }
        }
        Collections.sort(comments); //sort by date
        return comments;
    }

    public void dealroomActivity(Map<String, ExternalActivity> dealroomActivity) {
        this.dealroomActivity.clear();
        this.dealroomActivity = dealroomActivity;
    }

    public void setAccessTokens(Map<String, AccessToken> accessTokens) {
        this.accessTokens.clear();
        this.accessTokens.putAll(accessTokens);
    }

    public void setAuthorizationCodes(Map<String, AuthorizationCode> authorizationCodes) {
        this.authorizationCodes.clear();
        this.authorizationCodes.putAll(authorizationCodes);
    }

    public void setApplications(Map<String, Application> applications) {
        this.applications.clear();
        this.applications.putAll(applications);
    }

    public void setRefreshTokens(Map<String, RefreshToken> refreshTokens) {
        this.refreshTokens.clear();
        this.refreshTokens.putAll(refreshTokens);
    }

    public void setTicketToUsers(Map<String, String> ticketToUsers) {
        this.ticketToUsers.clear();
        this.ticketToUsers.putAll(ticketToUsers);
    }

    public void setTileInstances(Map<String, TileInstance> tileInstances) {
        this.tileInstances.clear();
        this.tileInstances.putAll(tileInstances);
    }

    public void setTileDefinitions(Map<String, TileDefinition> tileDefinitions) {
        this.tileDefinitions.clear();
        this.tileDefinitions.putAll(tileDefinitions);
    }

    public void setJiveInstanceMap(Map<String, JiveInstance> jiveInstanceMap) {
        this.jiveInstanceMap.clear();
        this.jiveInstanceMap.putAll(jiveInstanceMap);
    }

    // dumps as json
    public void save() throws IOException {
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, this);
    }

    public static Database restore() throws IOException {
        if(file.exists()) {
            return restore(new FileInputStream(file));
        }
		return new Database();
    }
    // restores from json
    public static Database restore(InputStream buffer) throws IOException {
       	return mapper.reader(Database.class).readValue(buffer);
    }


	public synchronized static Database getInstance() {
		if( instance == null ) {
			try {
				file = new File("database.json");
			 	instance = restore();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return instance;
	}

	public static DatabaseFactory getDatabaseFactory() {
		return new DatabaseFactory();
	}
	private static class DatabaseFactory implements org.glassfish.hk2.api.Factory<Database> {

		@Override
		public Database provide() {
			return getInstance();
		}

		@Override
		public void dispose(Database instance) {

		}
	}


}
