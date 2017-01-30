TODO:
CSS layout
bash script
time formatting
create.sql
optionally enclosed by ""

____________________________________
NEW TODO:
1. use provided time formatter
2. format money using provided money formatter

____________________________________

Part B

1. Schemas
Item (
	ItemID PRIMARY KEY,
	Name, 
	Currently, 
	Buy_Price, 
	First_Bid, 
	Number_of_Bids, 
	Location, 
	Latitude, 
	Longitude, 
	Country, 
	Started, 
	Ends, 
	UserID, 
	Description
)

Category (
	ItemID PRIMARY KEY,
	Category PRIMARY KEY
)

Bid (
	ItemID PRIMARY KEY,
	UserID PRIMARY KEY,
	Time,
	Amount
)

User (
	UserID PRIMARY KEY, 
	Bidder_Rating, 
	Seller_Rating,
	Location, 
	Country
)


2. All our nontrivial functional dependencies effectively specify keys. 
3. Yes, all of our relations are in BCNF.
4. Yes, all of our relations are in 4NF.