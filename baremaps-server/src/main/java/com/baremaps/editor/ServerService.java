package com.baremaps.editor;

import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.google.common.net.HttpHeaders.CONTENT_ENCODING;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

import com.baremaps.config.style.Style;
import com.baremaps.config.tileset.Tileset;
import com.baremaps.tile.Tile;
import com.baremaps.tile.TileStore;
import com.baremaps.tile.TileStoreException;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@javax.ws.rs.Path("/")
public class ServerService {

  private static Logger logger = LoggerFactory.getLogger(ServerService.class);

  @Inject
  private Tileset tileset;

  @Inject
  private Style style;

  @Inject
  private TileStore tileStore;

  @GET
  @Path("style.json")
  @Produces(MediaType.APPLICATION_JSON)
  public Style getStyle() throws IOException {
    // override style properties with tileset properties
    if (tileset.getCenter() != null) {
      style.setCenter(List.of(tileset.getCenter().getLon(), tileset.getCenter().getLat()));
      style.setZoom(tileset.getCenter().getZoom());
    }
    return style;
  }

  @GET
  @Path("tiles.json")
  @Produces(MediaType.APPLICATION_JSON)
  public Tileset getTileset() {
    return tileset;
  }

  @GET
  @Path("tiles/{z}/{x}/{y}.mvt")
  public Response tile(@PathParam("z") int z, @PathParam("x") int x, @PathParam("y") int y) {
    Tile tile = new Tile(x, y, z);
    try {
      byte[] bytes = tileStore.read(tile);
      if (bytes != null) {
        return Response.status(200)
            .header(CONTENT_TYPE, "application/vnd.mapbox-vector-tile")
            .header(CONTENT_ENCODING, "gzip")
            .header(ACCESS_CONTROL_ALLOW_ORIGIN, "*")
            .entity(bytes)
            .build();
      } else {
        return Response.status(204).build();
      }
    } catch (TileStoreException ex) {
      logger.error(ex.getMessage());
      return Response.status(404).build();
    }
  }

}
