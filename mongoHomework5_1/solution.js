use test;
db.blog.aggregate([
    {
      $unwind: "$comments"
    },
    {
      $group: { "_id": "$comments.author", "commentCount": { $sum: 1 } }
    },
    {
      $sort: { "commentCount": -1 }
    },
    {
      $limit: 1
    }
]);