use test;
db.messages.update(
	{ "headers.Message-ID" : "<8147308.1075851042335.JavaMail.evans@thyme>"},
    { $addToSet : { "headers.To" : "mrpotatohead@10gen.com" } }
);

db.messages.find({ "headers.Message-ID" : "<8147308.1075851042335.JavaMail.evans@thyme>"});