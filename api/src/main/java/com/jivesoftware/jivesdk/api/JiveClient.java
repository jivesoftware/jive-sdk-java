package com.jivesoftware.jivesdk.api;

import com.jivesoftware.jivesdk.api.tiles.ActivityData;
import com.jivesoftware.jivesdk.api.tiles.TileData;
import org.apache.http.HttpEntity;
import org.codehaus.jackson.JsonGenerationException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 27/1/13
 * Time: 11:44 AM
 */
public interface JiveClient {
    boolean sendExternalStreamActivity(@Nonnull TileInstance item, @Nonnull String data)
            throws TileUninstalledException, InvalidRequestException;

    boolean sendExternalStreamComment(@Nonnull TileInstance item, @Nonnull String data, @Nonnull String parentId)
            throws TileUninstalledException, InvalidRequestException;

    boolean sendExternalStreamActivity(@Nonnull TileInstance item, @Nonnull ActivityData data)
			throws TileUninstalledException, InvalidRequestException, IOException;

    boolean sendExternalStreamComment(@Nonnull TileInstance item, @Nonnull ActivityData data, @Nonnull String parentId)
			throws TileUninstalledException, InvalidRequestException, IOException;

	boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull TileData data) throws
			IOException, InvalidRequestException, TileUninstalledException;

	boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull String data) throws
			UnsupportedEncodingException, InvalidRequestException, TileUninstalledException;
}
