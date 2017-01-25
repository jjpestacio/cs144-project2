---------------------------------------
SCHEMAS

// Notes
// - Maybe we can make a table

Item (
	ItemId PRIMARY KEY,
	Name VARCHAR(?), 
	CurrentPrice VARCHAR(?), 
	BuyPrice VARCHAR(?), 
	FirstBid VARCHAR(?), 
	NumBids INTEGER, 
	Location VARCHAR(?), 
	Latitude VARCHAR(?), 
	Longitude VARCHAR(?), 
	Country VARCHAR(?), 
	Started TIMESTAMP, 
	Ended TIMESTAMP, 
	UserId INTEGER, 
	Description VARCHAR(?),
)

Category (
	ItemId PRIMARY KEY,
	Category PRIMARY KEY,
)

Bid (
	ItemId INTEGER PRIMARY KEY,
	UserId INTEGER PRIMARY KEY,
	Time TIMESTAMP,
	Amount VARCHAR(?),
)

User (
	UserId INTEGER PRIMARY KEY, 
	BuyerRating VARCHAR(?), 
	SellerRating VARCHAR(?),
	Location VARCHAR(?), 
	Country VARCHAR(?),
)

---------------------------------------
TODO

1. write MyParserPrint::processFile
	- let's make sure we can print everything and then work on writing to a file

2. write the results to a file
	- let's separate attributes via ','

---------------------------------------
NOTES

Parsing https://www.tutorialspoint.com/java_xml/java_dom_parse_document.html
	1. create a DocumentBuilder
		- Used to create the document from the input file

	2. get the root element with doc.getDocumentElement()

	3. create a NodeList for every type of node you need to process
		- nodeList = doc.getElementsByTagName("tagName")
		- access items like nodeList.item(index)

	4. process each node in the nodeList
		- node.getName() => name
		- node.getNodeType() => type of object

		- can cast the node as an element: Element element = (element) node
			- element.getAttribute("attribute")
			- element.getElementsByTagName("tagName")
				- element.getElementsByTagName("tagName").items(0).getTextContext() => returns the text inside
