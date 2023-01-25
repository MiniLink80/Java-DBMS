package ceng.ceng351.foodrecdb;


import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FOODRECDB implements IFOODRECDB{

    private static String user = "e2487544";
    private static String password = "VLh2XXvPHCwsx-6g";
    private static String host = "momcorp.ceng.metu.edu.tr";
    private static String database = "db2487544";
    private static int port = 8080;
    private Connection con;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public int createTables() {

        int createdTables = 0;
        String MenuItems = "CREATE TABLE MenuItems (" +
                "itemID INT NOT NULL, " +
                "itemName VARCHAR(40)," +
                "cuisine VARCHAR(20), " +
                "price INT," +
                "PRIMARY KEY(itemID)" +
                ")";
        String Ingredients = "CREATE TABLE Ingredients (" +
                "ingredientID INT NOT NULL," +
                "ingredientName VARCHAR(40)," +
                "PRIMARY KEY(ingredientID)" +
                ")";
        String Includes = "CREATE TABLE Includes (" +
                "itemID INT NOT NULL," +
                "ingredientID INT NOT NULL," +
                "PRIMARY KEY(itemID, ingredientID)," +
                "FOREIGN KEY(itemID) REFERENCES MenuItems(itemID)," +
                "FOREIGN KEY(ingredientID) REFERENCES Ingredients(ingredientID)" +
                ")";
        String Ratings = "CREATE TABLE Ratings (" +
                "ratingID INT NOT NULL," +
                "itemID INT," +
                "rating INT," +
                "ratingDate DATE," +
                "PRIMARY KEY(ratingID)," +
                "FOREIGN KEY(itemID) REFERENCES MenuItems(itemID)" +
                ")";
        String DietaryCategories = "CREATE TABLE DietaryCategories (" +
                "ingredientID INT NOT NULL," +
                "dietaryCategory VARCHAR(20)," +
                "PRIMARY KEY(ingredientID, dietaryCategory)," +
                "FOREIGN KEY(ingredientID) REFERENCES Ingredients(ingredientID)" +
                ")";
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(MenuItems);
            st.close();
            //System.out.println("MenuItems was created.");
            createdTables++;

        } catch (Exception e) {
            //System.out.println("MenuItems couldn't be created");
        }

        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(MenuItems);
            st.close();
            //System.out.println("MenuItems was created.");
            createdTables++;

        } catch (Exception e) {
            //System.out.println("MenuItems couldn't be created");
        }

        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(Ingredients);
            st.close();
            //System.out.println("Ingredients was created.");
            createdTables++;

        } catch (Exception e) {
            //System.out.println("Ingredients couldn't be created");
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(Includes);
            st.close();
            //System.out.println("Includes was created.");
            createdTables++;

        } catch (Exception e) {
            //System.out.println("Includes couldn't be created");
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(Ratings);
            st.close();
            //System.out.println("Ratings was created.");
            createdTables++;

        } catch (Exception e) {
            //System.out.println("Ratings couldn't be created");
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(DietaryCategories);
            st.close();
            //System.out.println("DietaryCategories was created.");
            createdTables++;

        } catch (Exception e) {
            //System.out.println("DietaryCategories couldn't be created");
        }



        return createdTables;
    }

    @Override
    public int dropTables() {
        int droppedTables = 0;

        String MenuItems = "DROP TABLE MenuItems";
        String Ingredients = "DROP TABLE Ingredients";
        String Includes = "DROP TABLE Includes";
        String Ratings = "DROP TABLE Ratings";
        String DietaryCategories = "DROP TABLE DietaryCategories";

        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(DietaryCategories);
            st.close();
            droppedTables++;
        }
        catch (Exception e) {
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(Ratings);
            st.close();
            droppedTables++;
        }
        catch (Exception e) {
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(Includes);
            st.close();
            droppedTables++;
        }
        catch (Exception e) {
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(MenuItems);
            st.close();
            droppedTables++;
        }
        catch (Exception e) {
        }
        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(Ingredients);
            st.close();
            droppedTables++;
        }
        catch (Exception e) {
        }



        return droppedTables;
    }

    @Override
    public int insertMenuItems(MenuItem[] items) {
        int insertedElements = 0;
        for (MenuItem el:items) {
            String insert = "INSERT INTO MenuItems VALUES (" +
                    el.getItemID() + ", '" +
                    el.getItemName() + "','" +
                    el.getCuisine() + "'," +
                    el.getPrice() + ")";
            try {
                Statement st = this.con.createStatement();
                st.executeUpdate(insert);
                st.close();
                insertedElements++;
            } catch (Exception e) {

            }
        }
        return insertedElements;
    }

    @Override
    public int insertIngredients(Ingredient[] ingredients) {
        int insertedElements = 0;
        for (Ingredient el:ingredients) {
            String insert = "INSERT INTO Ingredients VALUES (" +
                    el.getIngredientID() + ", '" +
                    el.getIngredientName() + "')";
            try {
                Statement st = this.con.createStatement();
                st.executeUpdate(insert);
                st.close();
                insertedElements++;
            } catch (Exception e) {

            }
        }
        return insertedElements;
    }

    @Override
    public int insertIncludes(Includes[] includes) {
        int insertedElements = 0;
        for (Includes el:includes) {
            String insert = "INSERT INTO Includes VALUES (" +
                    el.getItemID() + ", " +
                    el.getIngredientID() + ")";
            try {
                Statement st = this.con.createStatement();
                st.executeUpdate(insert);
                st.close();
                insertedElements++;
            } catch (Exception e) {

            }
        }
        return insertedElements;
    }

    @Override
    public int insertDietaryCategories(DietaryCategory[] categories) {
        int insertedElements = 0;
        for (DietaryCategory el:categories) {
            String insert = "INSERT INTO DietaryCategories VALUES (" +
                    el.getIngredientID() + ", '" +
                    el.getDietaryCategory() + "')";
            try {
                Statement st = this.con.createStatement();
                st.executeUpdate(insert);
                st.close();
                insertedElements++;
            } catch (Exception e) {

            }
        }
        return insertedElements;
    }

    @Override
    public int insertRatings(Rating[] ratings) {
        int insertedElements = 0;
        for (Rating el:ratings) {
            String insert = "INSERT INTO Ratings VALUES (" +
                    el.getRatingID() + ", " +
                    el.getItemID() + ", " +
                    el.getRating() + ", " +
                    "STR_TO_DATE('" + el.getRatingDate() + "','%Y-%m-%d'))";
            try {
                Statement st = this.con.createStatement();
                st.executeUpdate(insert);
                st.close();
                insertedElements++;
            } catch (Exception e) {

            }
        }
        return insertedElements;
    }

    @Override
    public MenuItem[] getMenuItemsWithGivenIngredient(String name) {
        String query = "SELECT DISTINCT M.itemID, M.itemName, M.cuisine, M.price FROM MenuItems M, Includes Incl, Ingredients I WHERE " +
                "M.itemID = Incl.itemID AND " +
                "Incl.ingredientID = I.ingredientID AND " +
                "I.ingredientName = '" + name + "' " +
                "ORDER BY M.itemID ASC";
        ArrayList<MenuItem> temp = new ArrayList<MenuItem>();

        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                MenuItem el = new MenuItem( rs.getInt("itemID"),
                                            rs.getString("itemName"),
                                            rs.getString("cuisine"),
                                            rs.getInt("price"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {
            System.out.println("amogus");
        }

        MenuItem res[] = new MenuItem[temp.size()];
        res = temp.toArray(res);
        return res;
    }

    @Override
    public MenuItem[] getMenuItemsWithoutAnyIngredient() {
        String query = "SELECT DISTINCT M.itemID, M.itemName, M.cuisine, M.price FROM MenuItems M WHERE " +
                "NOT EXISTS (SELECT * FROM Includes I WHERE I.itemID = M.itemID) " +
                "ORDER BY M.itemID ASC";
        ArrayList<MenuItem> temp = new ArrayList<MenuItem>();

        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                MenuItem el = new MenuItem( rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getString("cuisine"),
                        rs.getInt("price"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {

        }

        MenuItem res[] = new MenuItem[temp.size()];
        res = temp.toArray(res);
        return res;

    }

    @Override
    public Ingredient[] getNotIncludedIngredients() {
        String query = "SELECT DISTINCT Ingr.ingredientID, Ingr.ingredientName FROM Ingredients Ingr WHERE " +
                "NOT EXISTS (SELECT * FROM Includes I WHERE " +
                "I.ingredientID = Ingr.ingredientID) " +
                "ORDER BY Ingr.ingredientID ASC";
        ArrayList<Ingredient> temp = new ArrayList<Ingredient>();

        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Ingredient el = new Ingredient( rs.getInt("ingredientID"),
                                                rs.getString("ingredientName"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {

        }

        Ingredient res[] = new Ingredient[temp.size()];
        res = temp.toArray(res);
        return res;
    }

    @Override
    public MenuItem getMenuItemWithMostIngredients() {
        String query = "SELECT M.itemID, M.itemName, M.cuisine, M.price FROM MenuItems M WHERE\n" +
                "NOT EXISTS(SELECT I.itemID FROM Includes I WHERE \n" +
                "I.itemID = M.itemID\n" +
                "GROUP BY I.itemID\n" +
                "HAVING COUNT(I.ingredientID) < (SELECT MAX(A.cnt) FROM (SELECT COUNT(I1.ingredientID) AS cnt FROM Includes I1 WHERE\n" +
                "I1.itemID != M.itemID\n" +
                "GROUP BY I1.itemID) A))";
        MenuItem el = null;
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            el = new MenuItem(  rs.getInt("itemID"),
                                rs.getString("itemName"),
                                rs.getString("cuisine"),
                                rs.getInt("price"));
            st.close();
        } catch (Exception e) {

        }

        return el;
    }

    @Override
    public QueryResult.MenuItemAverageRatingResult[] getMenuItemsWithAvgRatings() {
        String query = "SELECT M.itemID, M.itemName, AVG(R.rating) AS avgRating FROM MenuItems M, Ratings R WHERE\n" +
                "\tM.itemID= R.itemID\n" +
                "\tGROUP BY M.itemID\n" +
                "UNION\n" +
                "SELECT M.itemID, M.itemName, NULL FROM MenuItems M WHERE\n" +
                "\tM.itemID NOT IN (SELECT R.itemID FROM Ratings R)\n" +
                "ORDER BY avgRating DESC";
        ArrayList<QueryResult.MenuItemAverageRatingResult> temp = new ArrayList<QueryResult.MenuItemAverageRatingResult>();
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                QueryResult.MenuItemAverageRatingResult el = new QueryResult.MenuItemAverageRatingResult(rs.getString("itemID"), rs.getString("itemName"), rs.getString("avgRating"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {

        }

        QueryResult.MenuItemAverageRatingResult res[] = new QueryResult.MenuItemAverageRatingResult[temp.size()];
        res = temp.toArray(res);

        return res;
    }

    @Override
    public MenuItem[] getMenuItemsForDietaryCategory(String category) {

        String query = "SELECT DISTINCT M.itemID, M.itemName, M.cuisine, M.price FROM MenuItems M WHERE\n" +
                "\t(SELECT COUNT(DISTINCT D1.ingredientID) FROM DietaryCategories D1, Includes I1 WHERE\n" +
                "\t\tI1.itemID = M.itemID AND\n" +
                "\t\tI1.ingredientID = D1.ingredientID AND\n" +
                "\t\tD1.dietaryCategory = '" + category + "') = \n" +
                "\n" +
                "\t(SELECT COUNT(DISTINCT D1.ingredientID) FROM DietaryCategories D1, Includes I1 WHERE\n" +
                "\t\tI1.itemID = M.itemID AND\n" +
                "\t\tI1.ingredientID = D1.ingredientID) AND\n" +
                "\n" +
                "\t(SELECT COUNT(DISTINCT D1.ingredientID) FROM DietaryCategories D1, Includes I1 WHERE\n" +
                "\t\tI1.itemID = M.itemID AND\n" +
                "\t\tI1.ingredientID = D1.ingredientID) != 0\n" +
                "\t\n" +
                "\tORDER BY M.itemID ASC";
        ArrayList<MenuItem> temp = new ArrayList<MenuItem>();

        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                MenuItem el = new MenuItem( rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getString("cuisine"),
                        rs.getInt("price"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {

        }

        MenuItem res[] = new MenuItem[temp.size()];
        res = temp.toArray(res);
        return res;

    }

    @Override
    public Ingredient getMostUsedIngredient() {
        String query1 = "SELECT I.IngredientID FROM Includes I\n" +
                "\tGROUP BY I.ingredientID \n" +
                "\tORDER BY COUNT(I.itemID) DESC LIMIT 1;";
        Ingredient el = null;
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query1);

            rs.next();
            String id = rs.getString("ingredientID");
            String query2 = "SELECT ingredientID, ingredientName FROM Ingredients WHERE ingredientID ='" + id + "'";
            try {
                Statement st2 = this.con.createStatement();
                ResultSet rs2 = st.executeQuery(query2);
                rs2.next();
                el = new Ingredient(rs2.getInt("ingredientID"), rs2.getString("ingredientName"));
                st2.close();
            } catch (Exception e) {

            }
            st.close();
        } catch (Exception e) {

        }

        return el;
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgRating() {
        String query = "SELECT A.cuisine, AVG(A.rating) AS amk FROM \n" +
                "(SELECT M.cuisine, R.ratingId, R.rating FROM MenuItems M, Ratings R WHERE\n" +
                "\tM.itemID = R.itemID\n" +
                "UNION \n" +
                "SELECT M.cuisine, NULL, NULL FROM MenuItems M WHERE\n" +
                "\tM.itemID NOT IN (SELECT R.itemID FROM Ratings R)) A\n" +
                "GROUP BY cuisine\n" +
                "ORDER BY amk DESC";
        ArrayList<QueryResult.CuisineWithAverageResult> temp = new ArrayList<QueryResult.CuisineWithAverageResult>();
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                QueryResult.CuisineWithAverageResult el = new QueryResult.CuisineWithAverageResult(rs.getString("cuisine"), rs.getString("amk"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {

        }

        QueryResult.CuisineWithAverageResult res[] = new QueryResult.CuisineWithAverageResult[temp.size()];
        res = temp.toArray(res);

        return res;
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgIngredientCount() {
        String query = "SELECT cuisine, AVG(A.amk) AS amogus FROM (\n" +
                "SELECT M.cuisine, M.itemID, COUNT(I.ingredientID) AS amk FROM MenuItems M, Includes I WHERE\n" +
                "\tI.itemID = M.itemID\n" +
                "\tGROUP BY M.itemID\n" +
                "UNION\n" +
                "\tSELECT M.cuisine, M.itemID, 0 AS amk FROM MenuItems M WHERE\n" +
                "\tM.itemID NOT IN (select I.itemID from Includes I)\n" +
                "\tGROUP BY M.itemID\n" +
                "\n" +
                ") A\n" +
                "\tGROUP BY A.cuisine\n" +
                "\tORDER BY amogus DESC";
        ArrayList<QueryResult.CuisineWithAverageResult> temp = new ArrayList<QueryResult.CuisineWithAverageResult>();
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                QueryResult.CuisineWithAverageResult el = new QueryResult.CuisineWithAverageResult(rs.getString("cuisine"), rs.getString("amogus"));
                temp.add(el);
            }
            st.close();

        } catch (Exception e) {

        }

        QueryResult.CuisineWithAverageResult res[] = new QueryResult.CuisineWithAverageResult[temp.size()];
        res = temp.toArray(res);

        return res;
    }

    @Override
    public int increasePrice(String ingredientName, String increaseAmount) {
        String query = "UPDATE MenuItems M, Includes I, Ingredients Ingr SET M.price = M.price + " + increaseAmount + " WHERE \n" +
                "\tM.itemID = I.itemID AND\n" +
                "\tI.ingredientID = Ingr.ingredientID AND\n" +
                "\tIngr.ingredientName = '" + ingredientName +  "'";
        int res = 0;
        try {
            Statement st = this.con.createStatement();
            res = st.executeUpdate(query);
        } catch (Exception e) {
            //System.out.println("a");
        }
        return res;
    }

    @Override
    public Rating[] deleteOlderRatings(String date) {
        String query1 = "DELETE FROM Ratings WHERE DATE(ratingDate) < '" + date + "'";
        String query2 = "SELECT * FROM Ratings WHERE DATE(ratingDate) < '" + date + "' ORDER BY ratingID ASC";
        ArrayList<Rating> temp = new ArrayList<Rating>();
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery(query2);
            while (rs.next()) {
                temp.add(new Rating(rs.getInt("ratingID"), rs.getInt("itemID"), rs.getInt("rating"), rs.getString("ratingDate")));
            }
            st.close();
        } catch (Exception e) {
            //System.out.println("b");
        }

        try {
            Statement st = this.con.createStatement();
            st.executeUpdate(query1);
            st.close();
        } catch (Exception e) {
            //System.out.println("c");
        }

        Rating res[] = new Rating[temp.size()];
        res = temp.toArray(res);
        return res;
    }
}
