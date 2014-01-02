package com.jivesoftware.jivesdk.api;

import com.jivesoftware.jivesdk.api.tiles.ActivityData;
import com.jivesoftware.jivesdk.api.tiles.TileData;
import org.apache.http.HttpEntity;
import org.codehaus.jackson.JsonGenerationException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The API to send data to jive.  To get an instance of this use JiveSDKManager
 * @see com.jivesoftware.jivesdk.api.JiveSDKManager
 */
public interface JiveClient {
	/**
	 * Send external stream data to Jive.
	 *
	 * @param tileInstance The tile instance to send data to.
	 * @param data Activity Data as object.
	 * @return
	 * @throws TileUninstalledException
	 * @throws InvalidRequestException
	 * @throws IOException
	 */
	boolean sendExternalStreamActivity(@Nonnull TileInstance tileInstance, @Nonnull ActivityData data)
			throws TileUninstalledException, InvalidRequestException, IOException;

	/**
	 * Send external stream data to jive (as JSON string)
	 * @param tileInstance The tile instance to send data to.
	 * @param data  A json string containing the data to send.
	 * @return
	 * @throws TileUninstalledException
	 * @throws InvalidRequestException
	 */
	boolean sendExternalStreamActivity(@Nonnull TileInstance tileInstance, @Nonnull String data)
            throws TileUninstalledException, InvalidRequestException;


	/**
	 *  Send a comment to an existing external stream entry.
	 *
 	 * @param tileInstance  The tile instance to send data to.
	 * @param data  Activity Data as object.
	 * @param parentId The external stream id (from ActivityData.externalID)
	 * @return
	 * @throws TileUninstalledException
	 * @throws InvalidRequestException
	 * @throws IOException
	 */
    boolean sendExternalStreamComment(@Nonnull TileInstance tileInstance, @Nonnull ActivityData data, @Nonnull String parentId)
			throws TileUninstalledException, InvalidRequestException, IOException;

	/**
	 * Send a comment to an existing external stream entry.
	 *
	 * @param tileInstance The tile instance to send data to.
	 * @param data A json string containing the data to send.
	 * @param parentId The external stream id (from ActivityData.externalID)
	 * @return
	 * @throws TileUninstalledException
	 * @throws InvalidRequestException
	 */
	boolean sendExternalStreamComment(@Nonnull TileInstance tileInstance, @Nonnull String data, @Nonnull String parentId)
			throws TileUninstalledException, InvalidRequestException;

	/**
	 * Send a tile update.
	 *
	 * @param tileInstance  The tile instance to send data to.
	 * @param data The tile data as an object.
	 * @return
	 * @throws IOException
	 * @throws InvalidRequestException
	 * @throws TileUninstalledException
	 */
	boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull TileData data) throws
			IOException, InvalidRequestException, TileUninstalledException;

	/**
	 * Send a tile update.
	 *
	 * @param tileInstance The tile instance to send data to.
	 * @param data A json string containing the data to send.
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws InvalidRequestException
	 * @throws TileUninstalledException
	 */
	boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull String data) throws
			UnsupportedEncodingException, InvalidRequestException, TileUninstalledException;
}
