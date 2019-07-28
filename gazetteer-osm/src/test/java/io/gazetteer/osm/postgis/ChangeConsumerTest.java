package io.gazetteer.osm.postgis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.gazetteer.common.postgis.DatabaseUtils;
import io.gazetteer.osm.OSMTestUtil;
import io.gazetteer.osm.model.Info;
import io.gazetteer.osm.model.Node;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ChangeConsumerTest {

  public Connection connection;


  @BeforeEach
  public void createTable() throws SQLException, IOException {
    connection = DriverManager.getConnection(OSMTestUtil.DATABASE_URL);
    DatabaseUtils.executeScript(connection, "osm_create_extensions.sql");
    DatabaseUtils.executeScript(connection, "osm_create_tables.sql");
  }

  @Test
  @Tag("integration")
  public void insert() throws SQLException {
    Random rnd = new Random(1);
    for (int i = 0; i < 100; i++) {
      Map<String, String> map = new HashMap<>();
      map.put("key", "val");
      Node insert =
          new Node(
              new Info(rnd.nextLong(), rnd.nextInt(),
                  LocalDateTime.ofInstant(Instant.ofEpochMilli(rnd.nextLong()), TimeZone.getDefault().toZoneId()),
                  rnd.nextLong(), rnd.nextInt(), map),
              rnd.nextDouble(),
              rnd.nextDouble());
      NodeTable.insert(connection, insert);
      Node select = NodeTable.select(connection, insert.getInfo().getId());
      assertEquals(insert.getInfo(), select.getInfo());
    }
  }

  @Test
  @Tag("integration")
  public void update() throws SQLException {
    Random rnd = new Random(1);
    for (int i = 0; i < 100; i++) {
      Map<String, String> map = new HashMap<>();
      map.put("key", "val");
      Node insert =
          new Node(
              new Info(rnd.nextLong(), rnd.nextInt(),
                  LocalDateTime.ofInstant(Instant.ofEpochMilli(rnd.nextLong()), TimeZone.getDefault().toZoneId()),
                  rnd.nextLong(), rnd.nextInt(), map),
              rnd.nextDouble(),
              rnd.nextDouble());
      NodeTable.insert(connection, insert);
      Node update =
          new Node(
              new Info(insert.getInfo().getId(), rnd.nextInt(),
                  LocalDateTime.ofInstant(Instant.ofEpochMilli(rnd.nextLong()), TimeZone.getDefault().toZoneId()),
                  rnd.nextLong(), rnd.nextInt(), map),
              rnd.nextDouble(),
              rnd.nextDouble());
      NodeTable.update(connection, update);
      Node select = NodeTable.select(connection, insert.getInfo().getId());
      assertEquals(update.getInfo(), select.getInfo());
    }
  }

  @Test
  @Tag("integration")
  public void delete() throws SQLException {
    Random rnd = new Random(1);
    for (int i = 0; i < 100; i++) {
      Map<String, String> map = new HashMap<>();
      map.put("key", "val");
      Node insert =
          new Node(
              new Info(rnd.nextLong(), rnd.nextInt(),
                  LocalDateTime.ofInstant(Instant.ofEpochMilli(rnd.nextLong()), TimeZone.getDefault().toZoneId()),
                  rnd.nextLong(), rnd.nextInt(), map),
              rnd.nextDouble(),
              rnd.nextDouble());
      NodeTable.insert(connection, insert);
      NodeTable.delete(connection, insert.getInfo().getId());
      assertThrows(
          IllegalArgumentException.class, () -> NodeTable.select(connection, insert.getInfo().getId()));
    }
  }
}
