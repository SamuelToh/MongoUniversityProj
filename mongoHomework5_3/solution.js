use test;
db.grades.aggregate([
    {
      $unwind: "$scores"
    },
    {
      $match: { "scores.type": { $in : [ "homework", "exam" ] } }
    },
    {
      $group: { "_id": { "studIdent": "student_id", "classIdent": "$class_id"}, "avgScore": { $avg: "$scores.score"}}
    },
    {
      $group: { "_id": "$_id.classIdent", "classAverage": {"$avg": "$avgScore"}}
    },
    {
      $sort: { "classAverage": -1 }
    },
    {
      $limit : 1
    },
    {
      $project: { "_id": 0, "Class with highest avg GPA=": "$_id", " Score=": "$classAverage"}  
    }
]);