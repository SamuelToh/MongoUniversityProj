use test;
db.messages.aggregate([
    { $match : { "headers.From" : "andrew.fastow@enron.com" } },
    { $match : { "headers.To"   : "jeff.skilling@enron.com" } },
    { 
    	$group: { "_id" : "headers.To", "ToJeffSkillinCount" : { $sum: 1 } }
    } 
]).pretty();