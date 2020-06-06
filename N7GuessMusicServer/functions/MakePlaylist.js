var mysql = require('mysql');
var getConnection = require('./db');
module.exports = function (genre){
    return new Promise(( resolve, reject ) => {
        getConnection(function(err, connection){
        var sql = "SELECT * FROM songs ORDER BY RAND() LIMIT 3;"
        /*
        if(genre == "random"){ sql = "SELECT * FROM songs ORDER BY RAND() LIMIT 3;"}
        else{sql = "SELECT * FROM songs where Genre ='"+genre+"' ORDER BY RAND() LIMIT 3;"} */
        
                connection.query(sql, function (err,result) {
                    var responce = []
                   for (var i = 0 ; i < result.length ; i++){
                    current = {
                           name : result[i].name,
                           artist : result[i].artist,
                           choices : JSON.parse("[" +result[i].choices+ "]")
                    }
                    responce.push(current)
                   }
                   resolve(responce)
                })
                connection.release()
            })
    })
}