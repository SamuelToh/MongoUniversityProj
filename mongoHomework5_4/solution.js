use test;
db.zips.findOne();
db.zips.aggregate([
    {
      $project: 
        {
          "_id" : 0,
          "zipcode" : "$_id",
          "population" : "$pop",
          "cityFirstChar" : { $substr : [ "$city", 0, 1 ] }
        }   
   },
   {
     $match:
       {
          "cityFirstChar" : { $in: [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"] }
       }
   },
   {
      $group:
        {
            "_id" : { zipcode: "$zipcode", "planet": "earth" },
            "totalPop" : { $sum : "$population" }
        }
   },
   {
      $group:
        {
            "_id" : "assignment 5_4",
            "answer" : { $sum : "$totalPop"}
        }
   }
])