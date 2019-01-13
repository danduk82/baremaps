package io.gazetteer.tileserver;

import io.netty.handler.ssl.SslContext;
import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class TileServerConfig {

    public final String host;

    public final int port;

    public final SslContext sslContext;

    public final TileServerCache tileCache;

    public final Pattern tileUri;

    public TileServerConfig(String host, int port, SslContext sslContext, TileServerCache tileCache, Pattern tileUri) {
        this.host = host;
        this.port = port;
        this.sslContext = sslContext;
        this.tileCache = tileCache;
        this.tileUri = tileUri;
    }

    public static TileServerConfig fromMBTilesFile(String mbtiles) throws SQLException {
        String host = "localhost";
        int port = 8080;
        String url = String.format("jdbc:sqlite:%s", mbtiles);
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        dataSource.setReadOnly(true);
        dataSource.setSharedCache(true);
        dataSource.setPageSize(1024);
        dataSource.setCacheSize(10000);
        TileServerCache cache = TileServerCache.fromDataSource(dataSource);
        Pattern tileUri =  Pattern.compile(String.format("/(\\d{1,2})/(\\d{1,6})/(\\d{1,6}).%s", cache.metadata.format.name()));
        return new TileServerConfig(host, port, null, cache, tileUri);
    }

}