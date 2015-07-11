use test;

db.messages.aggregate([
    {
      $unwind : "$headers.To"
    },
    {
      $group : { "_id" : { "FromMailUser" : "$headers.From", "To": "$headers.To" }, "count" : { $sum : 1 } }
    },
    {
      $sort: { "count" : -1 }
    },
    {
      $limit: 5
    }
]);

