/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

class Bid {
    String m_itemId, m_userId, m_time, m_amount;

    Bid(String itemId, String userId, String time, String amount) {
        m_itemId = itemId;
        m_userId = userId;
        m_time = time;
        m_amount = amount;
    }

    public final String toCSVFormat() {
        return String.format("%s|*|%s|*|%s|*|%s\n", m_itemId, m_userId, m_time, m_amount);
    }
}

class User {
    String m_userId, m_buyerRating, m_sellerRating, m_location, m_country;

    User(String userId, String buyerRating, String sellerRating, String location, String country) {
        m_userId = userId;
        m_buyerRating = buyerRating;
        m_sellerRating = sellerRating;
        m_location = location;
        m_country = country;
    }

    public final String toCSVFormat() {
        return String.format("%s|*|%s|*|%s|*|%s|*|%s\n", m_userId, m_buyerRating, m_sellerRating, m_location, m_country);
    }
}

class Category {
    String m_itemId, m_categoryName;

    Category(String itemId, String categoryName) {
        m_itemId = itemId;
        m_categoryName = categoryName;
    }

    public final String toCSVFormat() {
        return String.format("%s|*|%s\n", m_itemId, m_categoryName);
    }
}

class Item {
    String m_itemId, m_name, m_currently, m_buyPrice, m_firstBid, m_numBids,
            m_location, m_latitude, m_longitude, m_country, m_started, m_ends, m_userId, m_description;

    Item(String itemId, String name, String currently, String buyPrice,
         String firstBid, String numBids, String location, String latitude, String longitude,
         String country, String started, String ends, String userId, String description) {
        m_itemId = itemId;
        m_name = name;
        m_currently = currently;
        m_buyPrice = buyPrice;
        m_firstBid = firstBid;
        m_numBids = numBids;
        m_location = location;
        m_latitude = latitude;
        m_longitude = longitude;
        m_country = country;
        m_started = started;
        m_ends = ends;
        m_userId = userId;
        m_description = description;
    }

    public final String toCSVFormat() {
        return String.format("%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s|*|%s\n",
                m_itemId, m_name, m_currently, m_buyPrice, m_firstBid, m_numBids,
                m_location, m_latitude, m_longitude, m_country, m_started, m_ends, m_userId, m_description);
    }
}

class MyParser {
    static int count = 0;

    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

     // Hold the information
    static Hashtable<String, Item> Items = new Hashtable<String, Item>(); // key: itemId
    static Hashtable<String, Category> Categories = new Hashtable<String, Category>(); // key: itemId + categoryName
    static Hashtable<String, User> Users = new Hashtable<String, User>(); // key: userId
    static Hashtable<String, Bid> Bids = new Hashtable<String, Bid>(); // key: itemId + userId + time
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    static String toSQLFormat(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date inputDate;
        String outputDate = "";

        try {
            inputDate = inputFormat.parse(date);
            outputDate = outputFormat.format(inputDate);
        }
        catch(ParseException e) {
            System.out.println("Error with converting date");
        }
        return outputDate;
    }

    static void storeBids(Element item) {
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
        String itemId = item.getAttribute("ItemID");

        for (int i = 0; i < bids.length; i++) {
            Element bid = bids[i];
            Element bidder = getElementByTagNameNR(bid, "Bidder");

            String time = toSQLFormat(getElementTextByTagNameNR(bid, "Time"));
            String amount = strip(getElementTextByTagNameNR(bid, "Amount"));
            String location = getElementTextByTagNameNR(bidder, "Location");
            String country = getElementTextByTagNameNR(bidder, "Country");
            String buyerRating = bidder.getAttribute("Rating");
            String userId = bidder.getAttribute("UserID");

            String hashKey = itemId + userId + time;
            Bids.put(hashKey, new Bid(itemId, userId, time, amount));

            // Check if the user already exists to get their sellerRating
            String sellerRating = Users.containsKey(userId) ? Users.get(userId).m_sellerRating : "";
            Users.put(userId, new User(userId, buyerRating, sellerRating, location, country));
        }
    }

    static void storeCategories(Element item) {
        Element[] categories = getElementsByTagNameNR(item, "Category");
        String itemId = item.getAttribute("ItemID");

        for (int i = 0; i < categories.length; i++) {
            Element category = categories[i];
            String categoryName = getElementText(category);

            String hashKey = itemId + categoryName;
            Categories.put(hashKey, new Category(itemId, categoryName));
        }
    }

    static void storeSeller(Element item) {
        String location, country, sellerRating, userId;

        location = getElementText(getElementByTagNameNR(item, "Location"));
        country = getElementText(getElementByTagNameNR(item, "Country"));

        Element seller = getElementByTagNameNR(item, "Seller");
        sellerRating = seller.getAttribute("Rating");
        userId = seller.getAttribute("UserID");

        // Check if the user already exists to get their buyerRating
        String buyerRating = Users.containsKey(userId) ? Users.get(userId).m_buyerRating : "";
        Users.put(userId, new User(userId, buyerRating, sellerRating, location, country));
    }

    static void flushToFile() {
        final String USER_FILE = "user.csv";
        final String CATEGORY_FILE = "category.csv";
        final String BID_FILE = "bid.csv";
        final String ITEM_FILE = "item.csv";

        FileWriter fileWriter;

        try {
            File file;

            // Users
            file = new File(USER_FILE);

            if (file.exists())
                file.delete();

            file.createNewFile();
            fileWriter = new FileWriter(USER_FILE, true);

            for (String key : Users.keySet()) {
                fileWriter.write(Users.get(key).toCSVFormat());
                fileWriter.flush();
            }

            // Categories
            file = new File(CATEGORY_FILE);

            if (file.exists())
                file.delete();

            file.createNewFile();
            fileWriter = new FileWriter(CATEGORY_FILE, true);

            for (String key : Categories.keySet()){
                fileWriter.write(Categories.get(key).toCSVFormat());
                fileWriter.flush();
            }
            
            // Bids
            file = new File(BID_FILE);

            if (file.exists())
                file.delete();

            file.createNewFile();
            fileWriter = new FileWriter(BID_FILE, true);

            for (String key : Bids.keySet()) {
                fileWriter.write(Bids.get(key).toCSVFormat());
                fileWriter.flush();
            }
            
            // Items
            file = new File(ITEM_FILE);

            if (file.exists())
                file.delete();

            file.createNewFile();
            fileWriter = new FileWriter(ITEM_FILE, true);

            for (String key : Items.keySet()) {
                fileWriter.write(Items.get(key).toCSVFormat());
                fileWriter.flush();
            }

            fileWriter.close();

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
       System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        // Get root element
        Element root = doc.getDocumentElement();

        // Traverse through the children of root
        Element[] items = getElementsByTagNameNR(root, "Item");
        for (int i = 0; i < items.length; i++) {
            // Attributes to get
            String itemId, name, currently, buyPrice, firstBid, numBids,
                    location, latitude, longitude, country, started, ends, userId, description;

            Element item = items[i];

            itemId = item.getAttribute("ItemID");
            name = getElementTextByTagNameNR(item, "Name");
            currently = getElementTextByTagNameNR(item, "Currently");
            buyPrice = getElementTextByTagNameNR(item, "Buy_Price");
            firstBid = getElementTextByTagNameNR(item, "First_Bid");
            numBids = getElementTextByTagNameNR(item, "Number_of_Bids");
            location = getElementTextByTagNameNR(item, "Location");
            country = getElementTextByTagNameNR(item, "Country");

            started = toSQLFormat(getElementTextByTagNameNR(item, "Started"));
            ends = toSQLFormat(getElementTextByTagNameNR(item, "Ends"));

            userId =  getElementByTagNameNR(item, "Seller").getAttribute("UserID");
            description = getElementTextByTagNameNR(item, "Description");
            description = description.substring(0, Math.min(4000, description.length()));


            // Get Location child and attributes
            Element loc = getElementByTagNameNR(item, "Location");
            latitude = loc.getAttribute("Latitude");
            longitude = loc.getAttribute("Longitude");

            storeCategories(item);
            storeSeller(item);
            storeBids(item);

            Items.put(itemId, new Item(itemId, name, currently, buyPrice, firstBid, numBids, location, 
                latitude, longitude, country, started, ends, userId, description));
        }
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }

        flushToFile();
    }
}
