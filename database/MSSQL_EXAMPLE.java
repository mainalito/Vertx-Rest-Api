package database;

import models.Category;
import models.Vehicle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mssqlclient.MSSQLConnectOptions;
import io.vertx.mssqlclient.MSSQLPool;

import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class MSSQL_EXAMPLE extends AbstractVerticle {

    List<Vehicle> list = new ArrayList<>();

    JsonArray array = new JsonArray();

    static String INSERT = "INSERT INTO Categories(category_id,category_name) VALUES(@p1,@p2)";
    static String DELETE = "DELETE FROM Categories WHERE category_id= @p1";
    static String FETCHALL = "select * from Categories";

    private static final Logger logger = (Logger) LoggerFactory.getLogger(MSSQL_EXAMPLE.class);
    MSSQLConnectOptions connectOptions = new MSSQLConnectOptions()
            .setPort(1433)
            .setHost("localhost")
            .setDatabase("Ebook")
            .setUser("sa")
            .setPassword("123456789");
    //pool options
    PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(100);

    //client pool
    MSSQLPool client = MSSQLPool.pool(vertx, connectOptions, poolOptions);
    

    private void ConnectiontoDb() {

    }

    private void createSomeData() {

        Vehicle v = new Vehicle("KBC32", "BENZ", "MERCEDES");
        Vehicle v1 = new Vehicle("KBC32", "BENZ", "MERCEDES");
        Vehicle v2 = new Vehicle("KBC32", "BENZ", "MERCEDES");
        list.add(v);
        list.add(v1);
        list.add(v2);
    }

    private void getAll(RoutingContext ctx) {
        ctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                .setChunked(true).end(Json.encodePrettily(list));
    }

    private void addOne(RoutingContext e) {
        final Vehicle vehicle = Json.decodeValue(e.getBodyAsString(), Vehicle.class);
        list.add(vehicle);
        e.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(vehicle));
    }

    private void deleteOne(RoutingContext ctx) {
        String id = ctx.request().getParam("id");
        if (id == null) {
            ctx.response().setStatusCode(400).end();
        } else {
            list.removeIf(t -> t.getId().equals(id));
        }
        ctx.response().setStatusCode(204).end();
    }

    private void getAllCategories(RoutingContext ctx) {
        List<Category> lists = new ArrayList<>();
        JsonObject object = new JsonObject();
        client.preparedQuery(FETCHALL)
                .execute()
                .onSuccess(rows -> {

                    for (Row row : rows) {
                        array.add(new JsonObject()
                                .put("category id", row.getString("category_id"))
                                .put("category name", row.getString("category_name")));

                    }
                    object.put("data", array);

                    System.out.println(object.encodePrettily());
//                    System.out.println(array);
                    ctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(new JsonObject().put("data", array)));

                })
                .onFailure(failure -> {
                    System.out.println("failed" + failure);
                    ctx.response().setStatusCode(500);
                }
                );

    }

    private void addCategory(RoutingContext context) {
        final Category category = Json.decodeValue(context.getBodyAsString(), Category.class);
        String category_id = category.getCategory_id();
        String category_name = category.getCategory_name();
        category.setCategory_id(category_id);
        category.setCategory_name(category_name);
        Tuple tuple = Tuple.tuple()
                .addString(category.getCategory_id())
                .addString(category.getCategory_name());
        client.preparedQuery(INSERT)
                .execute(tuple, ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        System.out.println("rows" + rows.rowCount());
                        System.out.println("works");
                        context.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(category));

                    } else {
                        System.out.println("failed...please do more research");
                        System.out.println("Failure " + ar.cause().getMessage());
                        context.response()
                                .setStatusCode(500).end();

                    }
                });
    }

    private void deleteCategory(RoutingContext context) {
        
//        final Category category = Json.decodeValue(context.getBodyAsString(), Category.class);
//        String category_id = category.getCategory_id();
       String id = context.request().getParam("category_id");
//        category.setCategory_id(category_id);
//        System.out.println(id);
//        Tuple tuple = Tuple.tuple().addString(category.getCategory_id());
            Tuple tuple = Tuple.tuple().addString(id);
        client.preparedQuery(DELETE)
                .execute(tuple, ar -> {
                    if (ar.succeeded()) {
                        System.out.println("deleted " + id);
                        context.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
                                .end("works");
                    } else {
                        context.response().setStatusCode(400).end();
                    }
                });
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);

        createSomeData();
        router.get("/api/books").handler(this::getAllCategories);
        router.route("/api/books*").handler(BodyHandler.create());
        router.post("/api/books").handler(this::addCategory);
        router.delete("/api/books/").handler(this::deleteCategory);
        router.get("/api/vehicles").handler(this::getAll);
        router.route("/api/vehicles*").handler(BodyHandler.create()); //allows reading of the request body
        router.post("/api/vehicles").handler(this::addOne);
        router.delete("/api/vehicles/:id").handler(this::deleteOne);
       

        vertx.createHttpServer().requestHandler(router::accept).listen(
                //retrieve port from the configuration file
                config().getInteger("http.port", 8888), http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

}
