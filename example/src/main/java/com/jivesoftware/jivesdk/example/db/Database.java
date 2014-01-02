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
	private Map<String,RefreshToken> refreshTokens =
			Collections.synchronizedMap(new TreeMap<String,RefreshToken>());
	private Map<String, String> ticketToUsers =
			Collections.synchronizedMap(new TreeMap<String,String>());
    private Map<String, TileInstance> tileInstances =
            Collections.synchronizedMap(new TreeMap<String,TileInstance>());
    private Map<String, TileDefinition> tileDefinitions =
            Collections.synchronizedMap(new TreeMap<String,TileDefinition>());
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
        this.tileDefinitions.clear();
    }

    public void clear() {
		this.accessTokens.clear();
		this.authorizationCodes.clear();
		this.refreshTokens.clear();
		this.ticketToUsers.clear();
        this.tileInstances.clear();
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


    public Map<String, JiveInstance> getJiveInstanceMap() {
        return jiveInstanceMap;
    }
	public void setAccessTokens(Map<String, AccessToken> accessTokens) {
		this.accessTokens.clear();
		this.accessTokens.putAll(accessTokens);
	}

	public void setAuthorizationCodes(Map<String, AuthorizationCode> authorizationCodes) {
		this.authorizationCodes.clear();
		this.authorizationCodes.putAll(authorizationCodes);
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
}
