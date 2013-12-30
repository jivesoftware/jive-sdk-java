package com.jivesoftware.jivesdk.api;

import com.jivesoftware.jivesdk.api.tiles.TileData;
import org.apache.http.HttpEntity;

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
    boolean sendExternalStreamActivity(RegisteredInstance instance, @Nonnull TileInstance item,
									   @Nonnull HttpEntity data)
            throws TileUninstalledException, InvalidRequestException;

    boolean sendExternalStreamComment(RegisteredInstance instance, @Nonnull TileInstance item, @Nonnull HttpEntity data,
									  @Nonnull String parentId)
            throws TileUninstalledException, InvalidRequestException;

	boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull TileData data) throws
			IOException, InvalidRequestException, TileUninstalledException;

	boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull String data) throws
			UnsupportedEncodingException, InvalidRequestException, TileUninstalledException;

    public boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull HttpEntity data)
			throws InvalidRequestException, TileUninstalledException;

    boolean sendTilePutStatusUpdate(@Nonnull TileInstance item, @Nonnull HttpEntity data, RegisteredInstance instance)
            throws TileUninstalledException, InvalidRequestException;
}
