package com.jivesoftware.jivesdk.example.db;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.node.TextNode;


public class ExternalComment extends AbstractEntity{

    protected String text;
    protected String subject;
    protected JsonNode author;

    protected String jiveURI;
    protected String id;
    protected String parentActivityID;
    protected String rootExternalID;

    public ExternalComment() {
        super();
    }

    @JsonGetter
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonGetter
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonIgnore
    public JsonNode getAuthor() {
        return author;
    }

    @JsonGetter
    public String getAuthorString() {
        return author.toString();
    }

    public void setAuthor(JsonNode author) {
        this.author = author;
    }

    public void setAuthorString(String author) {
        this.author = new TextNode(author);
    }

    @JsonGetter
    public String getJiveURI() {
        return jiveURI;
    }

    public void setJiveURI(String jiveURI) {
        this.jiveURI = jiveURI;
    }

    @JsonGetter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter
    public String getParentActivityID() {
        return parentActivityID;
    }

    public void setParentActivityID(String parentActivityID) {
        this.parentActivityID = parentActivityID;
    }

    @JsonGetter
    public String getRootExternalID() {
        return rootExternalID;
    }

    public void setRootExternalID(String rootExternalID) {
        this.rootExternalID = rootExternalID;
    }
}
