use test;
db.zips.aggregate([
    {
      $match: {"state": {$in: [ "CA", "NY" ]}}
    },
    {
      $group: { "_id": "$city", "cityPopulation": { $sum: "$pop"}}
    },
    {
      $match: { "cityPopulation": { "$gt": 25000 }}
    },
    {
      $group: { "_id": { "cityName": "$_id", "state": "CA|NY" }, "avgPopulation": {"$avg": "$cityPopulation" }}
    },
    {
      $group: { "_id": "$_id.state", "avgPopulation": {"$avg": "$avgPopulation" }}
    },
    {
      $project: { "_id": 0, "Average population of large cities in CA and NY=": "$avgPopulation" }
    }
]);