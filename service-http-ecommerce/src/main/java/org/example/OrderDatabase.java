package org.example;

import org.example.org.example.database.LocalDatabase;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

public class OrderDatabase implements Closeable {

    private final LocalDatabase database;

    OrderDatabase() throws SQLException {
        this.database = new LocalDatabase("orders_database");
        this.database.createIfNosExists("create table Orders (" +
                "uuid varchar(200) primary key)");
    }

    public boolean saveNew(Order order) throws SQLException {
        if(waProcessed(order)){
            return false;
        }
        database.update("insert into Orders (uuid) values (?)", order.getOrderId());
        return true;
    }

    private boolean waProcessed(Order order) throws SQLException {
        var result = database.query("select uui from Orders where uuid = ? limit 1", order.getOrderId());
        return result.next();
    }

    @Override
    public void close() throws IOException {
        database.close();
    }
}
