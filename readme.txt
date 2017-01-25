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
