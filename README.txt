TODO:
CSS layout
bash script
time formatting
create.sql
optionally enclosed by ""

____________________________________

Part B

1. Schemas
Item (
	ItemId PRIMARY KEY,
	Name, 
	CurrentPrice, 
	BuyPrice, 
	FirstBid, 
	NumBids, 
	Location, 
	Latitude, 
	Longitude, 
	Country, 
	Started, 
	Ended, 
	UserId, 
	Description
)

Category (
	ItemId PRIMARY KEY,
	Category PRIMARY KEY
)

Bid (
	ItemId PRIMARY KEY,
	UserId PRIMARY KEY,
	Time,
	Amount
)

User (
	UserId PRIMARY KEY, 
	BuyerRating, 
	SellerRating,
	Location, 
	Country
)


2. All our nontrivial functional dependencies effectively specify keys. 
3. Yes, all of our relations are in BCNF.
4. Yes, all of our relations are in 4NF.